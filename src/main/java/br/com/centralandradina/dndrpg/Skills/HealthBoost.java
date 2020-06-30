package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealthBoost 
{
	Player player;
	
	public HealthBoost(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if((level <= 5) && (level > 0)) {
			this.player.sendMessage("HealthBoost " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1000000*20, level-1, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
	}
}
