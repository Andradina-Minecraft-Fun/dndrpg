package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireResistance
{
	Player player;
	
	public FireResistance(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("FireResistance 1");
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000*20, 0, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
	}
}
