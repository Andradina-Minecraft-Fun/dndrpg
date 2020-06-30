package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Speed
{
	Player player;
	
	public Speed(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if((level <= 4) && (level > 0)) {
			this.player.sendMessage("Speed " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000*20, level-1, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.SPEED);
	}

}
