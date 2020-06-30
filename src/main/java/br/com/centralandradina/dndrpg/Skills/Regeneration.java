package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Regeneration 
{
	Player player;
	
	public Regeneration(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("Regeneration " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000*20, 0, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.REGENERATION);
	}
}
