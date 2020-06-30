package br.com.centralandradina.dndrpg;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

/**
 * Quando crafit 2 espadas, ele recria o lore. Verificar qual p atack maior e aplicar o maior
 * 
 * @author scorninpc
 *
 */
public class CustomCraft implements Listener
{
	private Plugin plugin = main.getPlugin(main.class);
	private main m;
	
	public CustomCraft(main m)
	{
		this.m = m;
	}
	
	@EventHandler
	public void Craft(CraftItemEvent event) 
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList();
		
		Random gerador = new Random();
		
		float patk = 0;
		int patk_mod = 0;
		
		float pdef = 0;
		int pdef_mod = 0;
		
		float player_level = 0;
		
		// Adiciona o separador
		lore.add("");
		
		// Recupera o player que ta criando o item
		for (int i=this.m.getPlayers().size()-1; i>=0; i--) {
			if(player.getUniqueId() == this.m.getPlayers().get(i).getUUID()) {
				player_level = this.m.getPlayers().get(i).getLevel();
			}
		}
		
		// Calcula o modificador P Atack
		if(event.getCurrentItem().getType() == Material.BOW) {
			patk_mod = 2;
		}
		else if(event.getCurrentItem().getType() == Material.WOODEN_SWORD) {
			patk_mod = 2;
		}
		else if(event.getCurrentItem().getType() == Material.IRON_SWORD) {
			patk_mod = 3;
		}
		else if(event.getCurrentItem().getType() == Material.GOLDEN_SWORD) {
			patk_mod = 3;
		}
		else if(event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
			patk_mod = 4;
		}
//		else if(event.getCurrentItem().getType() == Material.SWORD) {
//			patk_mod = 4;
//		}
		if(patk_mod > 0) {
			patk = (player_level + (patk_mod*-1) + gerador.nextInt(patk_mod*2));
			if(patk > 0) {
				lore.add(this.color("&6&oP. Atk " + patk));
			}
		}
		
		// Calcula o modificador P Def
		if(
			(event.getCurrentItem().getType() == Material.DIAMOND_HELMET) || 
			(event.getCurrentItem().getType() == Material.DIAMOND_BOOTS) ||
			(event.getCurrentItem().getType() == Material.DIAMOND_LEGGINGS)
		) {
			pdef_mod = 5;
		}
		else if(event.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE) {
			pdef_mod = 8;
		}
		else if(
			(event.getCurrentItem().getType() == Material.IRON_HELMET) || 
			(event.getCurrentItem().getType() == Material.IRON_BOOTS) ||
			(event.getCurrentItem().getType() == Material.IRON_LEGGINGS)
		) {
			pdef_mod = 3;
		}
		else if(event.getCurrentItem().getType() == Material.IRON_CHESTPLATE) {
			pdef_mod = 5;
		}
		else if(
			(event.getCurrentItem().getType() == Material.GOLDEN_HELMET) || 
			(event.getCurrentItem().getType() == Material.GOLDEN_BOOTS) ||
			(event.getCurrentItem().getType() == Material.GOLDEN_LEGGINGS)
		) {
			pdef_mod = 3;
		}
		else if(event.getCurrentItem().getType() == Material.GOLDEN_CHESTPLATE) {
			pdef_mod = 5;
		}
		else if(
			(event.getCurrentItem().getType() == Material.CHAINMAIL_HELMET) || 
			(event.getCurrentItem().getType() == Material.CHAINMAIL_BOOTS) ||
			(event.getCurrentItem().getType() == Material.CHAINMAIL_LEGGINGS)
		) {
			pdef_mod = 3;
		}
		else if(event.getCurrentItem().getType() == Material.CHAINMAIL_CHESTPLATE) {
			pdef_mod = 5;
		}
		else if(
			(event.getCurrentItem().getType() == Material.LEATHER_HELMET) || 
			(event.getCurrentItem().getType() == Material.LEATHER_BOOTS) ||
			(event.getCurrentItem().getType() == Material.LEATHER_LEGGINGS)
		) {
			pdef_mod = 2;
		}
		else if(event.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE) {
			pdef_mod = 3;
		}
		
		// Adiciona o P Def
		if(pdef_mod > 0) {
			pdef = (player_level + (pdef_mod*-1) + gerador.nextInt(pdef_mod*2));
			if(pdef > 0) {
				lore.add(this.color("&6&oP. Def " + pdef));
			}
		}
		
		// Adiciona o lore
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	/**
	 * 
	 */
	private String color(String msg)
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
