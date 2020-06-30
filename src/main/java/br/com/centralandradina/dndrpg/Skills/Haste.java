package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Haste 
{
	Player player;
	
	public Haste(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("Haste 1");
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000*20, 1, false, false));
		}
		else if(level == 2) {
			this.player.sendMessage("Haste 2");
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000*20, 4, false, false));
		}
		else if(level == 3) {
			this.player.sendMessage("Haste 3");
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000*20, 10, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.FAST_DIGGING);
	}
}
