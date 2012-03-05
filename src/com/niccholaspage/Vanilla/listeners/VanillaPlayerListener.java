package com.niccholaspage.Vanilla.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.niccholaspage.Vanilla.Vanilla;

public class VanillaPlayerListener implements Listener {
	private final Vanilla plugin;
	
	private final Set<String> pluginsCommandAliases;
	
	private final Set<String> versionCommandAliases;

	public VanillaPlayerListener(Vanilla plugin){
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		pluginsCommandAliases = new HashSet<String>();
		
		pluginsCommandAliases.add("plugins");
		pluginsCommandAliases.add("plugin");
		
		versionCommandAliases = new HashSet<String>();
		
		versionCommandAliases.add("version");
		versionCommandAliases.add("ver");
		versionCommandAliases.add("about");
	}

	@EventHandler()
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		
		String[] split = event.getMessage().split("\\s+");

		String cmdName = split[0].substring(1).toLowerCase();
		
		if (!player.hasPermission("Vanilla.default.plugins") && pluginsCommandAliases.contains(cmdName)){
			plugin.getPluginsCommand().onCommand(player, null, null, null);
		}
		
		if (!player.hasPermission("Vanilla.default.version") && versionCommandAliases.contains(cmdName)){
			plugin.getVersionCommand().onCommand(player, null, null, null);
		}
	}
}
