package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ConduitPower
{
	Player player;
	
	public ConduitPower(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("ConduitPower 1");
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 1000000*20, 1, false, false));
		}
		else if(level == 2) {
			this.player.sendMessage("ConduitPower 2");
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 1000000*20, 4, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
	}
}
