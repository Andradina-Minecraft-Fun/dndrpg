package br.com.centralandradina.dndrpg;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class Damage implements Listener 
{
	private Plugin plugin = main.getPlugin(main.class);
	private main m;
	
	
	public Damage(main m)
	{
		this.m = m;
	}
	
	
	/**
	 * 
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent event)
	{
		// Recupera quem deu o hit
		LivingEntity atacante = null;
		
		if(event.getDamager() instanceof Player) {
			atacante = (LivingEntity) event.getDamager();
		}
		else if(event.getDamager() instanceof Monster) {
			atacante = (LivingEntity) event.getDamager();
		}
		else {
			if(event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
				atacante =  (LivingEntity) ((Projectile) event.getDamager()).getShooter();
			}
		}
		
		if(atacante == null) {
			return;
		}
		
		if(!(event.getEntity() instanceof LivingEntity)) {
			return;
		}

		// Recupera as infos da vitima
		LivingEntity vitima = (LivingEntity) event.getEntity();
		
		// Retorna se for NPC 
		if(vitima.hasMetadata("NPC")) {
			event.setDamage(0);
			return;
		}
		
		// Verifica se foi uma flecha, para usar DX como modificador
		boolean using_bow = false;
		if (event.getDamager() instanceof Arrow) {
			using_bow = true;
		}
		
		// Variaveis do atacante
		float atacante_str = this.getStr(atacante);
		float atacante_dex = this.getDex(atacante);
//		float atacante_con = this.getCon(atacante);
//		float atacante_int = this.getInt(atacante);
//		float atacante_pdef = this.getPDef(atacante);
		float atacante_patk = this.getPAtk(atacante);
		float atacante_level = 1;
		float class_weapon = 1;
		
		// Recupera a classe e os atributos da arma
		ItemStack item = atacante.getEquipment().getItemInMainHand();
		if(item != null) {
			ItemMeta meta = item.getItemMeta();
			if((meta != null) && (meta.hasLore())) {
				for(String lore : meta.getLore()) {
					if(lore.contains("P. Atk ")) {
						atacante_patk += Float.parseFloat(lore.substring(lore.indexOf("Atk")+4));
					}
				}
			}
			
			if(item.getType().toString().toLowerCase().contains("sword")) {
				class_weapon = 10;
			}
			else if(item.getType().toString().toLowerCase().contains("pickaxe")) {
				class_weapon = 6;
			}
			else if(item.getType().toString().toLowerCase().contains("axe")) {
				class_weapon = 8;
			}
			else if(item.getType().toString().toLowerCase().contains("arrow")) {
				class_weapon = 15;
			}
		}
		
		// Recupera o atacante
		if(atacante instanceof Player) {
			for (int i=this.m.getPlayers().size()-1; i>=0; i--) {
				if(atacante.getUniqueId() == this.m.getPlayers().get(i).getUUID()) {
					atacante_level = this.m.getPlayers().get(i).getLevel();
					atacante_str += (this.m.getPlayers().get(i).getSkillsGUI().l_strength * 2);
					break;
				}
			}
		}
		else {
			atacante_level = atacante.getMetadata("rpg_level").get(0).asFloat();
		}
		
		// Variaveis da vitima
//		float vitima_str = this.getStr(vitima);
//		float vitima_dex = this.getDex(vitima);
		float vitima_con = this.getCon(vitima);
//		float vitima_int = this.getInt(vitima);
		float vitima_pdef = this.getPDef(vitima);
//		float vitima_patk = this.getPAtk(vitima);
//		float vitima_level = 0;
		if(atacante instanceof Player) {
			for (int i=this.m.getPlayers().size()-1; i>=0; i--) {
				if(atacante.getUniqueId() == this.m.getPlayers().get(i).getUUID()) {
//					vitima_level = this.m.getPlayers().get(i).getLevel();
					vitima_pdef += (this.m.getPlayers().get(i).getSkillsGUI().l_resistence * 2);
					break;
				}
			}
		}
		
		// Modificador de força caso for arco
		float attr_modifier = atacante_str;
		if(using_bow) {
			attr_modifier = atacante_dex;
		}
		
		// Calcula o dano
//		float damage = (float)(((atacante_patk + attr_modifier + class_weapon) * 2) * (  (Math.log10(atacante_level)/2)+1  )  )       /          (vitima_pdef + vitima_con);
//		float damage = (float) ((atacante_patk + attr_modifier + class_weapon) * 4)-((vitima_pdef + vitima_con) * 2);
//		float damage = (float) ((atacante_patk + attr_modifier + class_weapon) / (1+((vitima_pdef + vitima_con)/100)));
		float damage = (float) ( (atacante_patk + ((int) ((attr_modifier-10)/2)) + class_weapon) / (vitima_pdef + vitima_con) );
		
		// Remove a proteção do armor
		try {
			event.setDamage(DamageModifier.ARMOR, 0);
		} catch(UnsupportedOperationException e) { }
		
		
		event.setDamage(damage);
		
		// Debug
		if(true) {
			plugin.getLogger().info("--------------------------------------------------------------------");
			plugin.getLogger().info("Weapon: " + item.getType());
			plugin.getLogger().info("Atacante Level: " + String.format("%1$,.2f", atacante_level));
			plugin.getLogger().info("Atacante STR: " + String.format("%1$,.2f", atacante_str));
			plugin.getLogger().info("Atacante DEX: " + String.format("%1$,.2f", atacante_dex));
			plugin.getLogger().info("Atacante P ATK: " + String.format("%1$,.2f", atacante_patk));
			plugin.getLogger().info("Weapon Class: " + String.format("%1$,.2f", class_weapon));
			plugin.getLogger().info("Vitima CON: " + String.format("%1$,.2f", vitima_con));
			plugin.getLogger().info("Vitima P DEF: " + String.format("%1$,.2f", vitima_pdef));
			plugin.getLogger().info("Damage: " + String.format("%1$,.2f", damage));
			plugin.getLogger().info("Vitima Life: " + vitima.getHealth());
		}
		
		
		
	}
	
	public float getStr(LivingEntity entity)
	{
		try {
			return entity.getMetadata("rpg_str").get(0).asFloat();
		} catch (IndexOutOfBoundsException e) {
			return 1;
		}
	}
	
	public float getDex(LivingEntity entity)
	{
		try {
			return entity.getMetadata("rpg_dex").get(0).asFloat();
		} catch (IndexOutOfBoundsException e) {
			return 1;
		}
	}
	
	public float getCon(LivingEntity entity)
	{
		try {
			return entity.getMetadata("rpg_con").get(0).asFloat();
		} catch (IndexOutOfBoundsException e) {
			return 1;
		}
	}
	
	public float getInt(LivingEntity entity)
	{
		try {
			return entity.getMetadata("rpg_int").get(0).asFloat();
		} catch (IndexOutOfBoundsException e) {
			return 1;
		}
	}
	
	/**
	 * Calcula o patack do entity
	 */
	public float getPAtk(LivingEntity entity)
	{
		float patk = 0;
		
		try {
			patk += entity.getMetadata("rpg_patk").get(0).asFloat();
		}
		catch(IndexOutOfBoundsException e) { }
		
		return patk;
	}
	
	/**
	 * Calcula o pdef do entity
	 */
	public float getPDef(LivingEntity entity)
	{
		float pdef = 0;
		ItemStack item;
		ItemMeta meta;
		
		// Verifica se é um monstro
		if(entity instanceof Monster) {
			pdef += entity.getMetadata("rpg_pdef").get(0).asFloat();
		}
		
		// Capacete
		item = entity.getEquipment().getHelmet();
		if(item != null) {
			meta = item.getItemMeta();
			if((meta != null) && (meta.hasLore())) {
				for(String lore : meta.getLore()) {
					if(lore.contains("P. Def ")) {
						pdef += Float.parseFloat(lore.substring(lore.indexOf("Def")+4));
					}
				}
			}
			
			// Recupera o material
			if(item.getType() == Material.DIAMOND_HELMET) {
				pdef += 4;
			}
			else if((item.getType() == Material.IRON_HELMET) || (item.getType() == Material.GOLDEN_HELMET)) {
				pdef += 3;
			}
			else if(item.getType() == Material.CHAINMAIL_HELMET) {
				pdef += 2;
			}
			else if (item.getType() == Material.LEATHER_HELMET) {
				pdef += 2;
			}
		}
		
		// Peitoral
		item = entity.getEquipment().getChestplate();
		if(item != null) {
			meta = item.getItemMeta();
			if((meta != null) && (meta.hasLore())) {
				for(String lore : meta.getLore()) {
					if(lore.contains("P. Def ")) {
						pdef += Float.parseFloat(lore.substring(lore.indexOf("Def")+4));
					}
				}
			}
			
			// Recupera o material
			if(item.getType() == Material.DIAMOND_CHESTPLATE) {
				pdef += 4;
			}
			else if((item.getType() == Material.IRON_CHESTPLATE) || (item.getType() == Material.GOLDEN_CHESTPLATE)) {
				pdef += 3;
			}
			else if(item.getType() == Material.CHAINMAIL_CHESTPLATE) {
				pdef += 2;
			}
			else if (item.getType() == Material.LEATHER_CHESTPLATE) {
				pdef += 2;
			}
		}
		
		// Calça
		item = entity.getEquipment().getLeggings();
		if(item != null) {
			meta = item.getItemMeta();
			if((meta != null) && (meta.hasLore())) {
				for(String lore : meta.getLore()) {
					if(lore.contains("P. Def ")) {
						pdef += Float.parseFloat(lore.substring(lore.indexOf("Def")+4));
					}
				}
			}
			
			// Recupera o material
			if(item.getType() == Material.DIAMOND_LEGGINGS) {
				pdef += 4;
			}
			else if((item.getType() == Material.IRON_LEGGINGS) || (item.getType() == Material.GOLDEN_LEGGINGS)) {
				pdef += 3;
			}
			else if(item.getType() == Material.CHAINMAIL_LEGGINGS) {
				pdef += 2;
			}
			else if (item.getType() == Material.LEATHER_LEGGINGS) {
				pdef += 2;
			}
		}
		
		// Bota
		item = entity.getEquipment().getBoots();
		if(item != null) {
			meta = item.getItemMeta();
			if((meta != null) && (meta.hasLore())) {
				for(String lore : meta.getLore()) {
					if(lore.contains("P. Def ")) {
						pdef += Float.parseFloat(lore.substring(lore.indexOf("Def")+4));
					}
				}
			}
			
			// Recupera o material
			if(item.getType() == Material.DIAMOND_BOOTS) {
				pdef += 4;
			}
			else if((item.getType() == Material.IRON_BOOTS) || (item.getType() == Material.GOLDEN_BOOTS)) {
				pdef += 3;
			}
			else if(item.getType() == Material.CHAINMAIL_BOOTS) {
				pdef += 2;
			}
			else if (item.getType() == Material.LEATHER_BOOTS) {
				pdef += 2;
			}
		}
		
		// Retorna
		return pdef;
	}
	
	
	/**
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerShoot(EntityShootBowEvent e)
	{
//		Arrow a = (Arrow) e.getEntity();
		LivingEntity shooter = (LivingEntity) e.getEntity();

		if(!(e.getEntity() instanceof Player)){
			shooter.sendMessage("Test");
		}
	}
}
