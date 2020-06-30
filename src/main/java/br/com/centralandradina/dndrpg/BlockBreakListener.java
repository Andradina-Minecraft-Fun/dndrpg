package br.com.centralandradina.dndrpg;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

public class BlockBreakListener implements Listener
{
	private Plugin plugin = main.getPlugin(main.class);
	
	/**
	 * 
	 */
	@EventHandler()
	public void onBlockBreak(final BlockBreakEvent event) 
	{
		
		
		for (int i = 0; i < main.getPlugin(main.class).getPlayers().size(); i++) {
			if(event.getPlayer().getUniqueId() == main.getPlugin(main.class).getPlayers().get(i).getUUID()) {
				main.getPlugin(main.class).getPlayers().get(i).addXp(10.5);
			}
		}
	}
}
