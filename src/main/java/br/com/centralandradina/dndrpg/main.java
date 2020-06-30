package br.com.centralandradina.dndrpg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener
{
//	private Database db;
	
	private ArrayList<DndPlayer> players = new ArrayList<DndPlayer>();
	
	private ArrayList<String> xp_blocks = new ArrayList<String>();
	private ArrayList<Double> xp_blocks_values = new ArrayList<Double>();
	
	private ArrayList<String> xp_mobs = new ArrayList<String>();
	private ArrayList<Double> xp_mobs_values = new ArrayList<Double>();
	
	public FileConfiguration config_xp;
	
	public SkillsGui skillGUI;
	
	/**
	 * 
	 */
	@Override
	public void onEnable()
	{
		// TODO Insert logic to be performed when the plugin is enabled
		getLogger().info("Iniciando carregamento.");
		
		// Registra o listenner
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new Damage(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new MobSpawn(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new CustomCraft(this), this);
		
		
		// Cria o diretório
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		
		// Cria o diretório dos players
		File players_dir = new File(this.getDataFolder() + "/players/");
		if(!players_dir.exists()) {
			players_dir.mkdirs();
		}
		
		// Carrega as configurações de XP
		File file_xp = new File(this.getDataFolder() + "/xp.yml");
		config_xp = YamlConfiguration.loadConfiguration(file_xp);
		try {
			config_xp.save(file_xp);
			config_xp.load(file_xp);
		} catch (IOException | InvalidConfigurationException e) {
			getLogger().info(e.getMessage() + "!");
		}
		
		// Carrega o vetor de XPs de blocos
		for(String key : config_xp.getConfigurationSection("blocks").getKeys(false)){
			xp_blocks.add(key);
			xp_blocks_values.add(config_xp.getDouble("blocks."+key));
		}
		getLogger().info(xp_blocks_values.size() + " blocks loaded");
		
		// Carrega o vetor de XPs de mobs
		for(String key : config_xp.getConfigurationSection("mobs").getKeys(false)){
			xp_mobs.add(key);
			xp_mobs_values.add(config_xp.getDouble("mobs."+key));
		}
		getLogger().info(xp_mobs_values.size() + " mobs loaded");
		
		// Recupera os players online
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			DndPlayer player = new DndPlayer(p);
			players.add(player);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void onDisable()
	{
		// TODO Insert logic to be performed when the plugin is disabled
		getLogger().info("Descarregando.");
		
		// Salva os players
		for (DndPlayer p : players) {
			getLogger().info("[D] Call Exit For " + p.getName());
			p.onExit();
		}
		
		players.clear();
	}
	
	/**
	 * 
	 */
	@EventHandler
	public void onDamageItem(PlayerItemDamageEvent event)
	{
		Player p = event.getPlayer();
		
		for (DndPlayer player : players) {
			if(p.getUniqueId() == player.getUUID()) {
				
				if(p.getGameMode() == GameMode.CREATIVE) {
					break;
				}
				
				player.getSkillsGUI().onDamageItem(event);
				
				break;
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player p = event.getPlayer();
		
		for (DndPlayer player : players) {
			if(p.getUniqueId() == player.getUUID()) {
				
				if(p.getGameMode() == GameMode.CREATIVE) {
					break;
				}
				
				player.getSkillsGUI().OnBlockPlace(event);
				
				break;
			}
		}
		
	}
	
	/**
	 * 
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerBreakBlock(BlockBreakEvent e)
	{
		Block block = e.getBlock();
		final Material roto = block.getType();
		Player p = e.getPlayer();
		
		if((p.getGameMode() == GameMode.CREATIVE) || e.isCancelled()){
			//return;
		}
		
		// Recupera a quantidade de XP padrão
		double xp_value = config_xp.getDouble("default_block");
		
		// Verifica se o bloco está configurado
		int block_index = xp_blocks.indexOf(roto.toString());
		if(block_index >= 0) {
			xp_value = xp_blocks_values.get(block_index);
		}
		
//		for (int i = 0; i < players.size(); i++) {
		for (DndPlayer player : players) {
			if(p.getUniqueId() == player.getUUID()) {
				player.addXp( xp_value );
				player.getSkillsGUI().onBreakBlock(e);
			}
		}
	}
	
	/**
	 * Phantom, slime, cavalo esqueleto, cubo de magma, gash (not handle)
	 * 
	 * Se der 1 hit com a mão, e o mob morrer por queda, o player ta ganhando xp. Verificar o last hit pra ganhar a xp, ou dividir entre os que deu hit
	 * Quando os mobs se matam (esqueletos), o player ganha XP do nada (algumas vezes)
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		
		if (entity instanceof Creature) {
			Creature creature = (Creature) entity;
			EntityDamageEvent lastDamage = entity.getLastDamageCause();
			
			if (lastDamage != null && creature.getTarget() instanceof Player) {
				
				Player p = (Player) creature.getTarget();
				
				if(p.getGameMode() == GameMode.CREATIVE) {
					return;
				}
				
				String killed = creature.getName();
				
//				getLogger().info("[D] Mob: " + killed);
				
				// Recupera a quantidade de XP padrão
				double xp_value = config_xp.getDouble("default_mob");
				
				// Verifica se o mob está configurado
				int mob_index = xp_mobs.indexOf(killed);
				if(mob_index >= 0) {
					xp_value = xp_mobs_values.get(mob_index);
				}
				
				for (DndPlayer player : players) {
//				for (int i = 0; i < players.size(); i++) {
					if(p.getUniqueId() == player.getUUID()) {
						player.addXp( xp_value );
					}
				}
			}
		}
		
		
	}
	
	/**
	 * 
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		DndPlayer player = new DndPlayer(p);
		
		players.add(player);
		
//		getLogger().info("[D] Players: " + players.size());
	}
	
	/**
	 * 
	 */
	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		
		for (int i=players.size()-1; i>=0; i--) {
			if(p.getUniqueId() == players.get(i).getUUID()) {
				players.get(i).onExit();
				players.remove(i);
				break;
			}
		}
		
//		getLogger().info("[D] Players: " + players.size());
	}
	
	/**
	 * 
	 * @param e
	 */
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		String invTitle = e.getView().getTitle();
		if (invTitle.contains("Skills")) {
			e.setCancelled(true);
			
			final ItemStack clickedItem = e.getCurrentItem();
			
			// Verify current item is not null
			if (clickedItem == null || clickedItem.getType() == Material.AIR || clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) return;
			
			final Player p = (Player) e.getWhoClicked();
			for (int i=players.size()-1; i>=0; i--) {
				if(p.getUniqueId() == players.get(i).getUUID()) {
					players.get(i).getSkillsGUI().onClick(e.getRawSlot());
					break;
				}
			}
		}
		
		else if (invTitle.contains("Atributos")) {
			e.setCancelled(true);
			
			final ItemStack clickedItem = e.getCurrentItem();
			
			// Verify current item is not null
			if (clickedItem == null || clickedItem.getType() == Material.AIR || clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) return;
			
			final Player p = (Player) e.getWhoClicked();
			for (int i=players.size()-1; i>=0; i--) {
				if(p.getUniqueId() == players.get(i).getUUID()) {
					players.get(i).getPointsGUI().onClick(e.getRawSlot());
					break;
				}
			}
		}
	}
	
	/**
	 * @todo Talvez verificar se é OP ou tem determinada permissão
	 */
	@EventHandler()
	public void onKill(EntityDeathEvent e)
	{
		// Mob
		if(e.getEntity() instanceof Monster) {
//			Monster mob = (Monster) e.getEntity();
		}
		
		// Player
		else if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			for (int i=players.size()-1; i>=0; i--) {
				if(p.getUniqueId() == players.get(i).getUUID()) {
					
					if(p.getGameMode() == GameMode.CREATIVE) {
						break;
					}
					
					players.get(i).getSkillsGUI().onDeath((PlayerDeathEvent) e);
					
					break;
				}
			}
		}
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		String commandName = cmd.getName().toLowerCase();

		if (!(sender instanceof Player)) {
			sender.sendMessage("O comando deve ser executado por um player");
			return false;
		}
		
		Player player = (Player)sender;
		
		// discord
		if(commandName.equals("rpg")) {
			
			// Verifica se possui argumentos
			if(args.length == 0) {
//				showHelp(sender);
				return false;
			}
			
			// Verifica se é reset
			if(args[0].equalsIgnoreCase("reset")) {
				for (int i=players.size()-1; i>=0; i--) {
					if(args[1].equals(players.get(i).getName())) {
						players.get(i).reset();
						break;
					}
				}
			}
			
			// Verifica se é skills
			if(args[0].equalsIgnoreCase("skills")) {
				for (int i=players.size()-1; i>=0; i--) {
					if(player.getName().equals(players.get(i).getName())) {
						players.get(i).openSkillsGUI();
						break;
					}
				}
			}
			
			// Verifica se é atributos
			if(args[0].equalsIgnoreCase("attr")) {
				for (int i=players.size()-1; i>=0; i--) {
					if(player.getName().equals(players.get(i).getName())) {
						players.get(i).openPointsGUI();
						break;
					}
				}
			}
			
			// Verifica se é atributos
			if(args[0].equalsIgnoreCase("pdef")) {
				ItemStack item = player.getEquipment().getItemInMainHand();
				ItemMeta meta = item.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("P. Def " + args[1]);
				
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
			
			// Verifica se é atributos
			if(args[0].equalsIgnoreCase("patk")) {
				ItemStack item = player.getEquipment().getItemInMainHand();
				ItemMeta meta = item.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("P. Atk " + args[1]);
				
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
			
			// Verifica se é reload
			if(args[0].equalsIgnoreCase("reload")) {
				
				this.reloadConfig();
//				config = this.getConfig();
				return false;
			}
			
			
//			showHelp(sender);
			
		}
		
		return false;
	}
	
	/**
	 * 
	 */
	public ArrayList<DndPlayer> getPlayers()
	{
		return players;
	}
}
