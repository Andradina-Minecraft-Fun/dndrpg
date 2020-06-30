package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Grab 
{

	Player player;
	int level = 0;
	
	public Grab(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.level = level;
		this.player.sendMessage("Grab " + level);
	}
	
	public void removeEffect()
	{
		
	}
	
	public boolean doGrab()
	{
		return (this.level > 0);
	}
}
