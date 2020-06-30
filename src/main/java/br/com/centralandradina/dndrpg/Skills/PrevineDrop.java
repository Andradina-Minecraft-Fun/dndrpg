package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;

public class PrevineDrop
{
	Player player;
	int level = 0;
	
	public PrevineDrop(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.level = level;
		this.player.sendMessage("PrevineDrop " + level);
	}
	
	public void removeEffect()
	{
		
	}
	
	public boolean willDrop()
	{
		int prob = (int) (Math.random() * (10 - 1 + 1) + 1);
		
//		player.sendMessage("Probabilidade: " + prob + " de " + (this.level * 2));
		
		return (prob > (this.level * 2));
	}
}
