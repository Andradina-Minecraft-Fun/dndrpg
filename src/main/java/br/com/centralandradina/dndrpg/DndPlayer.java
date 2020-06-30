package br.com.centralandradina.dndrpg;

import java.awt.image.ReplicateScaleFilter;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class DndPlayer
{
	private Plugin plugin = main.getPlugin(main.class);
	private static DndPlayer instance;
	
	public Player player;
	
	private UUID uuid;
	private String name;
	
	private double xp;
	private int level;
	
	public int points_skill = 0;
	public int points = 0;
	
	public int attr_str = 0;
	public int attr_dex = 0;
	public int attr_con = 0;
	public int attr_int = 0;
	
	public int total_damage = 0;
	
	public BossBar xpBossBar;
	
	Scoreboard scoreboard;
	
	private final SkillsGui invSkills;
	private final PointsGui invPoints;
	
	public ArrayList<ItemStack> inv_items = new ArrayList<ItemStack>();
	
	public FileConfiguration player_config;
	File player_file;
	
	/**
	 * Construtor
	 */
	public DndPlayer(Player player) 
	{
		this.instance = this;
		this.player= player;
		
		// Verifica se o usuario existe
		this.uuid = player.getUniqueId();
		this.name = player.getName();
		
		// Cria ou carrega o arquivo do player
		player_file = new File(this.plugin.getDataFolder() + "/players/" + this.player.getUniqueId() + ".yml");
		if(!player_file.exists()) {
			try {
				player_file.createNewFile();
			} catch (IOException e) {
				this.plugin.getLogger().info(e.getMessage() + "!");
			}
		}
		
		try {
			player_config = YamlConfiguration.loadConfiguration(player_file);
			player_config.load(player_file);
			
			if(!player_config.isSet("xp")) {
				player_config.set("xp", 0);
				player_config.set("level", 1);
				player_config.set("points", 0);
				player_config.set("points_skill", 0);
				player_config.set("attr.str", 8);
				player_config.set("attr.dex", 8);
				player_config.set("attr.con", 8);
				player_config.set("attr.int", 8);
			}
			
		} catch (IOException | InvalidConfigurationException e) { this.plugin.getLogger().info(e.getMessage() + "!"); }
		
		level = player_config.getInt("level");
		xp = player_config.getDouble("xp");
		points = player_config.getInt("points");
		points_skill = player_config.getInt("points_skill");
		attr_str = player_config.getInt("attr.str");
		attr_dex = player_config.getInt("attr.dex");
		attr_con = player_config.getInt("attr.con");
		attr_int = player_config.getInt("attr.int");
		
		// Adiciona os atributos ao player
		this.player.setMetadata("rpg_str", new FixedMetadataValue(this.plugin, attr_str));
		this.player.setMetadata("rpg_dex", new FixedMetadataValue(this.plugin, attr_dex));
		this.player.setMetadata("rpg_con", new FixedMetadataValue(this.plugin, attr_con));
		this.player.setMetadata("rpg_int", new FixedMetadataValue(this.plugin, attr_int));
		
		// Cria o score board
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");
		
		objective.setDisplayName("");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		player.setScoreboard(scoreboard);
		
		// Cria o GUI das skills
		invSkills = new SkillsGui(this);
		
		// Cria o GUI dos atributos
		invPoints = new PointsGui(this);
		
		// Adiciona a barra de XP
		this.xpBossBar = Bukkit.createBossBar(this.level + "", BarColor.GREEN, BarStyle.SEGMENTED_20);
		this.xpBossBar.addPlayer(this.player);
		
		// Atualiza os dados
		this.updateScoreboard();
	}
	
	/**
	 * 
	 */
	public void openSkillsGUI()
	{
		invSkills.openInventory();
	}
	
	/**
	 * 
	 */
	public SkillsGui getSkillsGUI()
	{
		return invSkills;
	}
	
	/**
	 * 
	 */
	public void openPointsGUI()
	{
		invPoints.openInventory();
	}
	
	/**
	 * 
	 */
	public PointsGui getPointsGUI()
	{
		return invPoints;
	}
	
	/**
	 * 
	 * @return
	 */
	public static DndPlayer getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	public void updateScoreboard()
	{
		if (!Bukkit.getOnlinePlayers().contains(player) || (player.getScoreboard() == null)) {
			return;
		}
		
		for (String str : player.getScoreboard().getEntries()) {
			player.getScoreboard().resetScores(str);
		}
		
		Scoreboard score = player.getScoreboard();
		score.getObjective(DisplaySlot.SIDEBAR).getScore(this.color(""
				+ "&f FOR: " + String.format("%02d", this.attr_str) + " (" + this.modificador(this.attr_str) + ")    "
				+ "DES: " + String.format("%02d", this.attr_dex) + "(" + this.modificador(this.attr_dex) + ")"
		)).setScore(6);
		score.getObjective(DisplaySlot.SIDEBAR).getScore(this.color(""
				+ "&f CON: " + String.format("%02d", this.attr_con) + " (" + this.modificador(this.attr_con) + ")    "
				+ "INT: " + String.format("%02d", this.attr_int) + "(" + this.modificador(this.attr_int) + ")"
		)).setScore(5);
		score.getObjective(DisplaySlot.SIDEBAR).getScore(" ").setScore(4);
		
		// Atualiza a barra
		try {
			this.xpBossBar.setProgress(this.xp/100);
			this.xpBossBar.setTitle(this.color("&2&l" + String.format("%02d", this.level)));
		}
		catch (IllegalArgumentException e) {
			
		}
	}
	
	
	/**
	 * 
	 */
	public void onExit()
	{
		this.xpBossBar.removePlayer(this.player);
		
		this.save();
	}
	
	/**
	 * 
	 */
	public void save()
	{
		player_config.set("attr.str", this.attr_str);
		player_config.set("attr.dex", this.attr_dex);
		player_config.set("attr.int", this.attr_int);
		player_config.set("attr.con", this.attr_con);
		
		player_config.set("xp", this.xp);
		player_config.set("level", this.level);
		player_config.set("points", this.points);
		player_config.set("points_skill", this.points_skill);

		player_config.set("skills.conduitpower", this.getSkillsGUI().l_conduitpower);
		player_config.set("skills.dolphinsgrace", this.getSkillsGUI().l_dolphinsgrace);
		player_config.set("skills.fireresistance", this.getSkillsGUI().l_fireresistance);
		player_config.set("skills.haste", this.getSkillsGUI().l_haste);
		player_config.set("skills.healthboost", this.getSkillsGUI().l_healthboost);
		player_config.set("skills.jumpboost", this.getSkillsGUI().l_jumpboost);
		player_config.set("skills.nightvision", this.getSkillsGUI().l_nightvision);
		player_config.set("skills.regeneration", this.getSkillsGUI().l_regeneration);
		player_config.set("skills.resistence", this.getSkillsGUI().l_resistence);
		player_config.set("skills.slowfalling", this.getSkillsGUI().l_slowfalling);
		player_config.set("skills.speed", this.getSkillsGUI().l_speed);
		player_config.set("skills.strength", this.getSkillsGUI().l_strength);
		player_config.set("skills.previnedrop", this.getSkillsGUI().l_previnedrop);
		player_config.set("skills.durability", this.getSkillsGUI().l_durability);
		player_config.set("skills.grab", this.getSkillsGUI().l_grab);
		player_config.set("skills.stock", this.getSkillsGUI().l_stock);
		
		try {
			player_config.save(player_file);
		}
		catch (IOException e) {
			plugin.getLogger().info(e.getMessage()+"!");
		}
	}
	
	/**
	 * 
	 */
	public UUID getUUID()
	{
		return this.uuid;
	}
	
	/**
	 * 
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * 
	 */
	public void addXp(double quantity)
	{
		if(this.level >= 30) {
			
		}
		else {
		
			this.xp += quantity;
			
			if(this.xp >= 100) {
				double multiple = this.xp / 100;
				
				this.addLevel((int)multiple);
				
				this.xp = (multiple - (int)multiple) * 100;
				
				if(this.xp == 100) {
					this.xp = 0;
				}
				
				if(this.level >= 30) {
					this.xp = 100;
				}
			}
			
			this.updateScoreboard();
		}
	}
	
	/**
	 * 
	 */
	public void addLevel(int quantity)
	{
		if(this.level < 30) {
			this.level += quantity;
			this.points += quantity;
			this.points_skill += quantity;
			
			player.sendMessage(this.color("Level UP to " + this.level));
			
			Location loc = player.getLocation();
			Location loc1 = loc.clone();
			Location loc2 = loc.clone();
			Location loc3 = loc.clone();
			Location loc4 = loc.clone();
			loc1.getWorld().playEffect(loc1, Effect.STEP_SOUND, 51);
			
			loc1.setX(loc.getX()+1);
			loc2.setX(loc.getX()-1);
			loc3.setZ(loc.getZ()+1);
			loc4.setZ(loc.getZ()-1);
			
			for(int i=0; i<2; i++) {
				
				loc1.setY(loc1.getY()+1);
				loc2.setY(loc2.getY()+1);
				loc3.setY(loc3.getY()+1);
				loc4.setY(loc4.getY()+1);
				
				player.getWorld().spawnParticle(Particle.REDSTONE, loc1, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.RED, 2));
				player.getWorld().spawnParticle(Particle.REDSTONE, loc2, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.RED, 2));
				player.getWorld().spawnParticle(Particle.REDSTONE, loc3, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.RED, 2));
				player.getWorld().spawnParticle(Particle.REDSTONE, loc4, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.RED, 2));
			}
			
			// Salva o usuário
			this.save();
		}
		
	}
	
	public float getLevel()
	{
		return (float)this.level;
	}
	
	/**
	 * 
	 */
	public void reset()
	{
		this.level = 1;
		this.xp = 0;
		this.points = 0;
		this.points_skill = 0;
		this.attr_str = 8;
		this.attr_dex = 8;
		this.attr_con = 8;
		this.attr_int = 8;
		
		this.invSkills.reset();
		
		this.updateScoreboard();
		
		this.save();
		
	}
	
	/**
	 * 
	 */
	private String color(String msg)
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	/**
	 * 
	 */
	private String modificador(float valor)
	{
		int modificador = ((int)(valor-10)/2);
		if(modificador < 0) {
			return "" + modificador;
		}
		else {
			return "+" + modificador;
		}
	}
}
