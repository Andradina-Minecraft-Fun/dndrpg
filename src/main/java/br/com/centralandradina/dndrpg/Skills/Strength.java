package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Strength
{
	Player player;
	
	public Strength(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if((level <= 3) && (level > 0)) {
			this.player.sendMessage("Strength" + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000*20, level-1, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
	}

}
