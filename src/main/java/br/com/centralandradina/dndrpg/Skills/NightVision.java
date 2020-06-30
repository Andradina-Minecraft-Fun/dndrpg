package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVision 
{
	Player player;
	
	public NightVision(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("NightVision " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000*20, 0, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.NIGHT_VISION);
	}


}
