package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Resistence 
{
	Player player;
	
	public Resistence(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("Resistence " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000*20, 0, false, false));
		}
		else if(level == 2) {
			this.player.sendMessage("Resistence " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000*20, 1, false, false));
		}
		else if(level == 3) {
			this.player.sendMessage("Resistence " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000*20, 3, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	}

}
