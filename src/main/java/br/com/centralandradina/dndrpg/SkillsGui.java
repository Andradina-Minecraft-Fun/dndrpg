package br.com.centralandradina.dndrpg;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import br.com.centralandradina.dndrpg.Skills.ConduitPower;
import br.com.centralandradina.dndrpg.Skills.DolphinsGrace;
import br.com.centralandradina.dndrpg.Skills.Durability;
import br.com.centralandradina.dndrpg.Skills.FireResistance;
import br.com.centralandradina.dndrpg.Skills.Grab;
import br.com.centralandradina.dndrpg.Skills.Haste;
import br.com.centralandradina.dndrpg.Skills.HealthBoost;
import br.com.centralandradina.dndrpg.Skills.JumpBoost;
import br.com.centralandradina.dndrpg.Skills.NightVision;
import br.com.centralandradina.dndrpg.Skills.PrevineDrop;
import br.com.centralandradina.dndrpg.Skills.Regeneration;
import br.com.centralandradina.dndrpg.Skills.Resistence;
import br.com.centralandradina.dndrpg.Skills.SlowFalling;
import br.com.centralandradina.dndrpg.Skills.Speed;
import br.com.centralandradina.dndrpg.Skills.Stock;
import br.com.centralandradina.dndrpg.Skills.Strength;

public class SkillsGui
{
	private final Inventory invSkills;
	private DndPlayer player;
	private Plugin plugin = main.getPlugin(main.class);
	
	ConduitPower s_conduitpower;
	DolphinsGrace s_dolphinsgrace;
	FireResistance s_fireresistance;
	Haste s_haste;
	HealthBoost s_healthboost;
	JumpBoost s_jumpboost;
	NightVision s_nightvision;
	Regeneration s_regeneration;
	Resistence s_resistence;
	Speed s_speed;
	SlowFalling s_slowfalling;
	Strength s_strength;
	PrevineDrop s_previnedrop;
	Durability s_durability;
	Grab s_grab;
	Stock s_stock;
	
	int l_conduitpower = 0;
	int l_dolphinsgrace = 0;
	int l_fireresistance = 0;
	int l_haste = 0;
	int l_healthboost = 0;
	int l_jumpboost = 0;
	int l_nightvision = 0;
	int l_regeneration = 0;
	int l_resistence = 0;
	int l_speed = 0;
	int l_slowfalling = 0;
	int l_strength = 0;
	int l_previnedrop = 0;
	int l_durability = 0;
	int l_grab = 0;
	int l_stock = 0;
	
	
	public SkillsGui(DndPlayer player)
	{
		this.player = player;
		
		// Monta o GUI
		invSkills = Bukkit.createInventory(null, 9*5, "Skills");
		
		// Cria os objetos das skills
		s_conduitpower = new ConduitPower(this.player.player);
		s_dolphinsgrace = new DolphinsGrace(this.player.player);
		s_fireresistance = new FireResistance(this.player.player);
		s_haste = new Haste(this.player.player);
		s_healthboost = new HealthBoost(this.player.player);
		s_jumpboost = new JumpBoost(this.player.player);
		s_nightvision = new NightVision(this.player.player);
		s_regeneration = new Regeneration(this.player.player);
		s_resistence = new Resistence(this.player.player);
		s_slowfalling = new SlowFalling(this.player.player);
		s_speed = new Speed(this.player.player);
		s_strength = new Strength(this.player.player);
		s_previnedrop = new PrevineDrop(this.player.player);
		s_durability = new Durability(this.player.player);
		s_grab = new Grab(this.player.player);
		s_stock = new Stock(this.player.player);
		
		// Get levels
		l_conduitpower = getDbSkill("conduitpower");
		s_conduitpower.applyEffect(l_conduitpower);
		
		l_dolphinsgrace = getDbSkill("dolphinsgrace");
		s_dolphinsgrace.applyEffect(l_dolphinsgrace);
		
		l_fireresistance = getDbSkill("fireresistance");
		s_fireresistance.applyEffect(l_fireresistance);
		
		l_haste = getDbSkill("haste");
		s_haste.applyEffect(l_haste);
		
		l_healthboost = getDbSkill("healthboost");
		s_healthboost.applyEffect(l_healthboost);
		
		l_jumpboost = getDbSkill("jumpboost");
		s_jumpboost.applyEffect(l_jumpboost);
		
		l_nightvision = getDbSkill("nightvision");
		s_nightvision.applyEffect(l_nightvision);
		
		l_regeneration = getDbSkill("regeneration");
		s_regeneration.applyEffect(l_regeneration);
		
		l_resistence = getDbSkill("resistence");
		s_resistence.applyEffect(l_resistence);
		
		l_slowfalling = getDbSkill("slowfalling");
		s_slowfalling.applyEffect(l_slowfalling);
		
		l_speed = getDbSkill("speed");
		s_speed.applyEffect(l_speed);
		
		l_strength = getDbSkill("strength");
		s_strength.applyEffect(l_strength);
		
		l_previnedrop = getDbSkill("previnedrop");
		s_previnedrop.applyEffect(l_previnedrop);
		
		l_durability = getDbSkill("durability");
		s_durability.applyEffect(l_durability);
		
		l_grab = getDbSkill("grab");
		s_grab.applyEffect(l_grab);
		
		l_stock = getDbSkill("stock");
		s_stock.applyEffect(l_stock);
		
		// Cria a GUI
		this.createItems();
		
//		// Salva
//		try {
//			this.player.player_config.save(this.player.player_file);
//			this.player.player_config.load(this.player.player_file);
//		} catch (IOException e) {
//			this.plugin.getLogger().info(e.getMessage() + "!");
//		} catch (InvalidConfigurationException e) {
//			this.plugin.getLogger().info(e.getMessage() + "!");
//		}
	}
	
	public void reset()
	{
		// Get levels
		l_conduitpower = 0;
		s_conduitpower.applyEffect(l_conduitpower);
		
		l_dolphinsgrace = 0;
		s_dolphinsgrace.applyEffect(l_dolphinsgrace);
		
		l_fireresistance = 0;
		s_fireresistance.applyEffect(l_fireresistance);
		
		l_haste = 0;
		s_haste.applyEffect(l_haste);
		
		l_healthboost = 0;
		s_healthboost.applyEffect(l_healthboost);
		
		l_jumpboost = 0;
		s_jumpboost.applyEffect(l_jumpboost);
		
		l_nightvision = 0;
		s_nightvision.applyEffect(l_nightvision);
		
		l_regeneration = 0;
		s_regeneration.applyEffect(l_regeneration);
		
		l_resistence = 0;
		s_resistence.applyEffect(l_resistence);
		
		l_slowfalling = 0;
		s_slowfalling.applyEffect(l_slowfalling);
		
		l_speed = 0;
		s_speed.applyEffect(l_speed);
		
		l_strength = 0;
		s_strength.applyEffect(l_strength);
		
		l_previnedrop = 0;
		s_previnedrop.applyEffect(l_previnedrop);
		
		l_durability = 0;
		s_durability.applyEffect(l_durability);
		
		l_grab = 0;
		s_grab.applyEffect(l_grab);
		
		l_stock = 0;
		s_stock.applyEffect(l_stock);
	}
	
	public int getDbSkill(String skill_name)
	{
		
		if(!this.player.player_config.isSet("skills." + skill_name)) {
			this.player.player_config.set("skills." + skill_name, 0);
		}
		else {
			return this.player.player_config.getInt("skills." + skill_name);
		}
		
		return 0;
		
		
//		// Recupera as skills do player
//		try { 
//			ResultSet result = player.getDatabase().Query("SELECT level FROM users_skills WHERE uuid = '" + player.getUUID().toString() + "' AND skill = '" + skill_name + "'");
//			
//			if(result.getInt("level") >= 0) {
//				return result.getInt("level");
//			}
//			
//			result.close();
//		}
//		catch (SQLException e) {
//			try {
//				plugin.getLogger().info("NAO ACHOU, CADASTROU!");
//				player.getDatabase().Execute("INSERT INTO users_skills (uuid, level, skill) VALUES ('" + player.getUUID().toString() + "', 0, '" + skill_name + "')");
//			}
//			catch (SQLException e1) {
//				plugin.getLogger().info(e.getMessage()+"! INSERT INTO users_skills (uuid, level, skill) VALUES ('" + player.getUUID().toString() + "', 0, '" + skill_name + "')");
//			}
//		}
		
//		return 0;
	}
	
	/**
	 * 
	 */
	public void createItems()
	{
		invSkills.clear();
		
		invSkills.setItem(0, this.createGuiSpace());
		invSkills.setItem(1, this.createGuiSpace());
		invSkills.setItem(2, this.createGuiSpace());
		invSkills.setItem(3, this.createGuiSpace());
		invSkills.setItem(4, this.createGuiSpace());
		invSkills.setItem(5, this.createGuiSpace());
		invSkills.setItem(6, this.createGuiSpace());
		invSkills.setItem(7, this.createGuiSpace());
		invSkills.setItem(8, this.createGuiSpace());
		invSkills.setItem(9, this.createGuiSpace());
		invSkills.setItem(10, this.createGuiItem(Material.HEART_OF_THE_SEA, 1,
				"§f§lConduit", "", 
				"§a§lEncherga e respira de baixo da agua.",
				"§a§lDá mineração mais rápida no level 2", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_conduitpower), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(11, this.createGuiItem(Material.CYAN_DYE, 1,
				"§f§lDolphin's Grace", "", 
				"§a§lConcede velocidade de nadar mais rapido", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_dolphinsgrace), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(12, this.createGuiItem(Material.CHAINMAIL_CHESTPLATE, 1,
				"§f§lFire Resistance", "", 
				"§a§lConcede resistencia à fogo", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_fireresistance), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(13, this.createGuiSpace());
		invSkills.setItem(14, this.createGuiItem(Material.DIAMOND_PICKAXE, 1,
				"§f§lHaste", "", 
				"§a§lAumenta velocidade de mineração", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_haste), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(15, this.createGuiItem(Material.APPLE, 1,
				"§f§lHealth Boost", "", 
				"§a§lAdiciona 2 corações de vida por level", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_healthboost), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(16, this.createGuiItem(Material.IRON_BOOTS, 1,
				"§f§lJump Boost", "", 
				"§a§lConcede bonus de pulo", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_jumpboost), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(17, this.createGuiSpace());
		invSkills.setItem(18, this.createGuiSpace());
		invSkills.setItem(19, this.createGuiItem(Material.ENDER_EYE, 1,
				"§f§lNight Vision", "", 
				"§a§lTe da o poder da visão noturna", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_nightvision), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(20, this.createGuiItem(Material.BEACON, 1,
				"§f§lRegeneration", "", 
				"§a§lRegeneral sua vida mais rapidamente", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_regeneration), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(21, this.createGuiItem(Material.DIAMOND_CHESTPLATE, 1,
				"§f§lResistence", "", 
				"§a§lAumenta sua resistencia", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_resistence), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(22, this.createGuiSpace());
		invSkills.setItem(23, this.createGuiItem(Material.FEATHER, 1,
				"§f§lSlow Falling", "", 
				"§a§lCai mais lento prevenindo dano de queda", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_slowfalling), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(24, this.createGuiItem(Material.DIAMOND_BOOTS, 1,
				"§f§lSpeed", "", 
				"§a§lSe movimenta mais rapido", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_speed), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(25, this.createGuiItem(Material.DIAMOND_SWORD, 1,
				"§f§lStrength", "", 
				"§a§lConcede bonus de força", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_strength), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(26, this.createGuiSpace());
		invSkills.setItem(27, this.createGuiSpace());
		invSkills.setItem(28, this.createGuiItem(Material.CHEST, 1,
				"§f§lPrevine Drop", "", 
				"§a§lAdiciona 20% a menos de chance de",
				"§a§lseus itens dropar ao morrer", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_previnedrop), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(29, this.createGuiItem(Material.IRON_BLOCK, 1,
				"§f§lDurability", "", 
				"§a§lAdiciona 20% a mais de durabilidade a cada level",
				"§a§laos seus itens", "", 
				"§6§lLevel atual: " + String.format("%02d", this.l_durability), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(30, this.createGuiItem(Material.HOPPER, 1,
				"§f§lGrab", "", 
				"§a§lColoca os itens diretamente no inventário", "", 
				"§6§lLevel atual: " + String.format("%02d", l_grab), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(31, this.createGuiSpace());
		invSkills.setItem(32, this.createGuiItem(Material.RAIL, 1,
				"§f§lStock", "", 
				"§a§lSe houver item fora da hotbar",
				"§a§lé adicionado automaticamente",
				"§a§lassim que ele acabar", "", 
				"§6§lLevel atual: " + String.format("%02d", l_stock), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points_skill)
		));
		invSkills.setItem(33, this.createGuiSpace());
		invSkills.setItem(34, this.createGuiSpace());
		invSkills.setItem(35, this.createGuiSpace());
		invSkills.setItem(36, this.createGuiSpace());
		invSkills.setItem(37, this.createGuiSpace());
		invSkills.setItem(38, this.createGuiSpace());
		invSkills.setItem(39, this.createGuiSpace());
		invSkills.setItem(40, this.createGuiSpace());
		invSkills.setItem(41, this.createGuiSpace());
		invSkills.setItem(42, this.createGuiSpace());
		invSkills.setItem(43, this.createGuiSpace());
		invSkills.setItem(44, this.createGuiSpace());
	}
	
	/**
	 * 
	 */
	protected ItemStack createGuiItem(final Material material, int quant, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, quant);
		final ItemMeta meta = item.getItemMeta();
		
		// Set the name of the item
		meta.setDisplayName(name);
		
		// Set the lore of the item
		meta.setLore(Arrays.asList(lore));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(meta);
		item.setAmount(quant);
		
		return item;
	}
	
	/**
	 * 
	 */
	protected ItemStack createGuiSpace() {
		final ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		final ItemMeta meta = item.getItemMeta();
		
		meta.setCustomModelData(new Random().nextInt(9999999));
		meta.setDisplayName(" ");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public Inventory getInventory()
	{
		return invSkills;
	}
	
	/**
	 * 
	 */
	public void openInventory()
	{
		this.createItems();

		this.player.player.openInventory(this.getInventory());
	}
	
	/**
	 * 
	 */
	public void onClick(int slot)
	{
		if(this.player.points_skill == 0) {
			this.player.player.sendMessage("Você não possui pontos de skills para distribuir");
			return;
		}
		
		if(slot == 10) // Conduit Power
		{
			if(this.l_conduitpower == 2) {
				this.player.player.sendMessage("Level máximo do Conduit Power é 2");
				return;
			}
			this.l_conduitpower++;
			s_conduitpower.applyEffect(l_conduitpower);
		}
		else if(slot == 11) // Dolphin's Grace
		{
			if(this.l_dolphinsgrace == 1) {
				this.player.player.sendMessage("Level máximo do Dolphin's Grace é 1");
				return;
			}
			this.l_dolphinsgrace++;
			s_dolphinsgrace.applyEffect(this.l_dolphinsgrace);
		}
		else if(slot == 12) // Fire Resistance
		{
			if(this.l_fireresistance == 1) {
				this.player.player.sendMessage("Level máximo do Fire Resistance é 1");
				return;
			}
			this.l_fireresistance++;
			s_fireresistance.applyEffect(l_fireresistance);
		}
		else if(slot == 14) // Haste
		{
			if(this.l_haste == 3) {
				this.player.player.sendMessage("Level máximo do Haste é 3");
				return;
			}
			this.l_haste++;
			s_haste.applyEffect(l_haste);
		}
		else if(slot == 15) // Health Boost
		{
			if(this.l_healthboost == 5) {
				this.player.player.sendMessage("Level máximo do Health Boost é 5");
				return;
			}
			this.l_healthboost++;
			s_healthboost.applyEffect(l_healthboost);
		}
		else if(slot == 16) // Jump Boost
		{
			if(this.l_jumpboost == 2) {
				this.player.player.sendMessage("Level máximo do Jump Boost é 2");
				return;
			}
			this.l_jumpboost++;
			s_jumpboost.applyEffect(l_jumpboost);
		}
		else if(slot == 19) // Night Vision
		{
			if(this.l_nightvision == 1) {
				this.player.player.sendMessage("Level máximo do Night Vision é 1");
				return;
			}
			this.l_nightvision++;
			s_nightvision.applyEffect(l_nightvision);
		}
		else if(slot == 20) // Regeneration
		{
			if(this.l_regeneration == 1) {
				this.player.player.sendMessage("Level máximo do Regeneration é 1");
				return;
			}
			this.l_regeneration++;
			s_regeneration.applyEffect(l_regeneration);
		}
		else if(slot == 21) // Resistence
		{
			if(this.l_resistence == 3) {
				this.player.player.sendMessage("Level máximo do Resistence é 3");
				return;
			}
			this.l_resistence++;
			s_resistence.applyEffect(l_resistence);
		}

		else if(slot == 23) // Slow Falling
		{
			if(this.l_slowfalling == 1) {
				this.player.player.sendMessage("Level máximo do Slow Falling é 1");
				return;
			}
			this.l_slowfalling++;
			s_slowfalling.applyEffect(l_slowfalling);
		}
		else if(slot == 24) // Speed
		{
			if(this.l_speed == 4) {
				this.player.player.sendMessage("Level máximo do Speed é 4");
				return;
			}
			this.l_speed++;
			s_speed.applyEffect(l_speed);
		}
		else if(slot == 25) // Strength
		{
			if(this.l_strength == 3) {
				this.player.player.sendMessage("Level máximo do Strength é 3");
				return;
			}
			this.l_strength++;
			s_strength.applyEffect(l_strength);
		}
		else if(slot == 28) // Previne Drop
		{
			if(this.l_previnedrop == 4) {
				this.player.player.sendMessage("Level máximo do Previne Drop é 4");
				return;
			}
			this.l_previnedrop++;
			s_previnedrop.applyEffect(l_previnedrop);
		}
		else if(slot == 29) // Durability
		{
			if(this.l_durability == 3) {
				this.player.player.sendMessage("Level máximo do Durability é 3");
				return;
			}
			this.l_durability++;
			s_durability.applyEffect(l_durability);
		}
		else if(slot == 30) // Grab
		{
			if(this.l_grab == 1) {
				this.player.player.sendMessage("Level máximo do Grab é 1");
				return;
			}
			this.l_grab++;
			s_grab.applyEffect(l_grab);
		}
		else if(slot == 32) // Stock
		{
			if(this.l_stock == 1) {
				this.player.player.sendMessage("Level máximo do Stock é 1");
				return;
			}
			this.l_stock++;
			s_stock.applyEffect(l_stock);
		}
		
		this.player.points_skill--;
		
		this.createItems();
	}
	
	/**
	 * 
	 */
	public void onDeath(PlayerDeathEvent event)
	{
		if(!s_previnedrop.willDrop()) {
			event.getDrops().clear();
			event.setKeepInventory(true);
			event.setKeepLevel(true);
		}
		else {
			event.setKeepInventory(false);
			event.setKeepLevel(false);
		}
	}
	
	/**
	 * 
	 */
	public void onDamageItem(PlayerItemDamageEvent event)
	{
		if(!s_durability.willDamage()) {
			event.setCancelled(true);
		}
		else {
			event.setCancelled(false);
		}
	}
	
	/**
	 * 
	 */
	public void onBreakBlock(BlockBreakEvent e)
	{
		if(s_grab.doGrab()) {
			Block block = e.getBlock();
			
			for (ItemStack item : block.getDrops(this.player.player.getInventory().getItemInMainHand())){
				HashMap<Integer, ItemStack> leftOver = this.player.player.getInventory().addItem(item);
				for(ItemStack leftoverItem : leftOver.values())
					this.player.player.getWorld().dropItem(this.player.player.getLocation(),leftoverItem);
			}
			e.setCancelled(true);
			block.setType(Material.AIR);
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void OnBlockPlace(BlockPlaceEvent event)
	{
		if(s_stock.doStock()) {
			ItemStack item = event.getItemInHand();
			
			// Verifica se o stack acabou
			if(item.getAmount() == 1) {
				// Procura o mesmo item no inventory
				Inventory inv = this.player.player.getInventory();
				for(int x=0; x<=inv.getSize(); x++) {
					if(inv.getItem(x) != null) {
						// Se achou outro stack do mesmo tipo
						if((inv.getItem(x).getType() == item.getType()) && (this.player.player.getInventory().getHeldItemSlot() != x)) {
							// Remove o atual e move o novo stack para a mão
							inv.clear(this.player.player.getInventory().getHeldItemSlot());
							inv.setItem(this.player.player.getInventory().getHeldItemSlot(), inv.getItem(x));
							inv.clear(x);
							break;
						}
					}
				}
			}
		}
	}
	
}
