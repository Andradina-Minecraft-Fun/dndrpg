package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpBoost 
{
	Player player;
	
	public JumpBoost(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.removeEffect();
		
		if(level == 1) {
			this.player.sendMessage("JumpBoost " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000*20, 0, false, false));
		}
		else if(level == 2) {
			this.player.sendMessage("JumpBoost " + level);
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000*20, 1, false, false));
		}
	}
	
	public void removeEffect()
	{
		this.player.removePotionEffect(PotionEffectType.JUMP);
	}

}
