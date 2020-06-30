package br.com.centralandradina.dndrpg;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class PointsGui 
{
	private final Inventory invPoints;
	private DndPlayer player;
	private Plugin plugin = main.getPlugin(main.class);
	
	public PointsGui(DndPlayer player)
	{
		this.player = player;
		
		// Monta o GUI
		invPoints = Bukkit.createInventory(null, 9*1, "Atributos");
//		invPoints.setMaxStackSize(1);
		
		this.createItems();
	}
	
	/**
	 * 
	 */
	protected ItemStack createGuiItem(final Material material, final int quant, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, quant);
		final ItemMeta meta = item.getItemMeta();
		
		// Set the name of the item
		meta.setDisplayName(name);
		
		// Set the lore of the item
		meta.setLore(Arrays.asList(lore));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	/**
	 * 
	 */
	public void createItems()
	{
		invPoints.clear();
		
		invPoints.setItem(0, this.createGuiSpace());
		invPoints.setItem(1, this.createGuiItem(Material.DIAMOND_SWORD, this.player.attr_str,
				"§f§lForça", "", 
				"§a§lA força influencia diretamente no seu dano", "", 
				"§6§lForça atual: " + String.format("%02d", this.player.attr_str), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points)
		));
		invPoints.setItem(2, this.createGuiSpace());
		invPoints.setItem(3, this.createGuiItem(Material.BOW, this.player.attr_dex,
				"§f§lDestreza", "", 
				"§a§lA destreza influencia em manuseio",
				"§a§lmanual, como ferramentas e arco", "",
				"§6§lDestreza atual: " + String.format("%02d", this.player.attr_dex), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points)
		));
		invPoints.setItem(4, this.createGuiSpace());
		invPoints.setItem(5, this.createGuiItem(Material.DIAMOND_CHESTPLATE, this.player.attr_con,
				"§f§lConstituição", "", 
				"§a§lA constituição te dará mais",
				"§a§lresistência e saúde", "",
				"§6§lConstituição atual: " + String.format("%02d", this.player.attr_con), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points)
		));
		invPoints.setItem(6, this.createGuiSpace());
		invPoints.setItem(7, this.createGuiItem(Material.ENCHANTED_BOOK, this.player.attr_int,
				"§f§lInteligência", "", 
				"§a§lA inteligência te ajudará na criação",
				"§a§lde poções e encantamento de itens", "", 
				"§6§lConstituição atual: " + String.format("%02d", this.player.attr_int), 
				"§6§lPontos disponível: " + String.format("%02d", this.player.points)
		));
		invPoints.setItem(8, this.createGuiSpace());
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
	
	/**
	 * 
	 */
	public Inventory getInventory()
	{
		return invPoints;
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
		if(this.player.points == 0) {
			this.player.player.sendMessage("Você não possui pontos de atributos para distribuir");
			return;
		}
		
		if(slot == 1) // Str
		{
			this.player.attr_str++;
			this.player.player.setMetadata("rpg_str", new FixedMetadataValue(this.plugin, this.player.attr_str));
		}
		else if(slot == 3) // Dex
		{
			this.player.attr_dex++;
			this.player.player.setMetadata("rpg_dex", new FixedMetadataValue(this.plugin, this.player.attr_dex));
		}
		else if(slot == 5) // Con
		{
			this.player.attr_con++;
			this.player.player.setMetadata("rpg_con", new FixedMetadataValue(this.plugin, this.player.attr_con));
		}
		else if(slot == 7) // Int
		{
			this.player.attr_int++;
			this.player.player.setMetadata("rpg_int", new FixedMetadataValue(this.plugin, this.player.attr_int));
		}
		
		this.player.points--;
		
		this.createItems();
		this.player.updateScoreboard();
	}

}
