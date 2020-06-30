package br.com.centralandradina.dndrpg.Skills;

import org.bukkit.entity.Player;

public class Stock 
{

	Player player;
	int level = 0;
	
	public Stock(Player player)
	{
		this.player = player;
	}
	
	public void applyEffect(int level)
	{
		this.level = level;
		this.player.sendMessage("Stock " + level);
	}
	
	public void removeEffect()
	{
		
	}
	
	public boolean doStock()
	{
		return (this.level > 0);
	}

}
