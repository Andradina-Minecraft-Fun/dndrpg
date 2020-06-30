package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DolphinsGrace
{
	Player player;
	
	public DolphinsGrace(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("DolphinsGrace 1");
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1000000*20, 0, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
	}
}
