package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;

public class Durability 
{
	Player player;
	int level = 0;
	
	public Durability(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.level = level;
		this.player.sendMessage("Durability " + level);
	}
	
	public void removeEffect()
	{
		
	}
	
	public boolean willDamage()
	{
		int prob = (int) (Math.random() * (10 - 1 + 1) + 1);
		
		return (prob > (this.level * 2));
	}

}
