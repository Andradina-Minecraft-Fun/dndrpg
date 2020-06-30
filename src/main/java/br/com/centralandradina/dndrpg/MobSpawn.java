package br.com.centralandradina.dndrpg;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class MobSpawn implements Listener
{
	private Plugin plugin = main.getPlugin(main.class);
	
	/**
	 * @todo Parametrizar o valor do atributo inicial (está chumbado 8)
	 * @todo Parametrizar o valor de variação do level (está chumbado -2 / +2)
	 * @todo Parametrizar o valor de variação dos modificadores (está chumbado -4 / +4)
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onMobSpawn(CreatureSpawnEvent e)
	{
		if(!e.isCancelled()) {
			if(e.getEntity() instanceof Monster) {
				Monster mob = (Monster) e.getEntity();
				Random gerador = new Random();
				
				double distance = mob.getLocation().distance(new Location(mob.getWorld(), 0, 0, 0));
				int level = (int)(distance / 400) - 2;
				level += gerador.nextInt(4);
				if(level <= 0) {
					level = 1;
				}

				float mob_str = 8+gerador.nextInt(level);
				float mob_dex = 8+gerador.nextInt(level);
				float mob_con = 8+gerador.nextInt(level);
				float mob_int = 8+gerador.nextInt(level);
				float mob_pdef = level -4 + gerador.nextInt(8);
				float mob_patk = level -4 + gerador.nextInt(8);
				
				if(mob_pdef<=0) {
					mob_pdef = 1;
				}
				
				if(mob_patk<=0) {
					mob_patk = 1;
				}
				
				
				// Adiciona os atributos
				mob.setMetadata("rpg_str", new FixedMetadataValue(this.plugin, mob_str));
				mob.setMetadata("rpg_dex", new FixedMetadataValue(this.plugin, mob_dex));
				mob.setMetadata("rpg_con", new FixedMetadataValue(this.plugin, mob_con));
				mob.setMetadata("rpg_int", new FixedMetadataValue(this.plugin, mob_int));
				
				mob.setMetadata("rpg_level", new FixedMetadataValue(this.plugin, level));
				
				mob.setMetadata("rpg_pdef", new FixedMetadataValue(this.plugin, mob_pdef));
				mob.setMetadata("rpg_patk", new FixedMetadataValue(this.plugin, mob_patk));
				
				String name = mob.getName();
				mob.setCustomName(ChatColor.translateAlternateColorCodes('&', "&7" + name + " &8[&cLVL " + String.format("%02d", level) + "&8]"));
				
				
				ItemStack item = mob.getEquipment().getItemInMainHand();
				if(item != null) {
					if(item.getType().toString().toLowerCase().contains("sword")) {
						ItemMeta meta = item.getItemMeta();
						ArrayList<String> lore = new ArrayList<String>();
						
						int patk = 15;
						
						lore.add("P. Atk " + patk);
						
						meta.setLore(lore);
						item.setItemMeta(meta);
						
						mob_patk += patk;
					}
				}
//				else {
//					item = new ItemStack(Material.DIAMOND_SWORD);
//					
//					ItemMeta meta = item.getItemMeta();
//					ArrayList<String> lore = new ArrayList<String>();
//					
//					int patk = 15;
//					
//					lore.add("P. Atk " + patk);
//					
//					meta.setLore(lore);
//					item.setItemMeta(meta);
//					
//					mob_patk += patk;
//					
//					mob.getEquipment().setItemInMainHand(item);
//				}
				
				if(false) {
					plugin.getLogger().info("--------------------------------------------------------------------");
					plugin.getLogger().info("[D] Mob Type: " + mob.getType());
					plugin.getLogger().info("[D] Mob Distance: " + distance);
					plugin.getLogger().info("[D] Mob Level: " + level);
					plugin.getLogger().info("[D] Mob STR: " + mob_str);
					plugin.getLogger().info("[D] Mob DEX: " + mob_dex);
					plugin.getLogger().info("[D] Mob CON: " + mob_con);
					plugin.getLogger().info("[D] Mob INT: " + mob_int);
					plugin.getLogger().info("[D] Mob P. DEF: " + mob_pdef);
					plugin.getLogger().info("[D] Mob P. ATK: " + mob_patk);
				}
				
				
			}
		}
	}
}
