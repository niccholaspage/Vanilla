package com.niccholaspage.Vanilla.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.niccholaspage.Vanilla.ConfigHandler;
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
		
		String[] realArgs;
		
		if (split.length < 2){
			realArgs = new String[0];
		}else {
			realArgs = new String[split.length - 2];
			
			for (int i = 2; i < split.length; i++){
				realArgs[i - 2] = split[i];
			}
		}
		
		boolean handled = true;
		
		ConfigHandler configHandler = plugin.getConfigHandler();
		
		if (pluginsCommandAliases.contains(cmdName) && !player.hasPermission("Vanilla.default.plugins")){
			if (!configHandler.isHidingPluginsCommand()){
				plugin.getPluginsCommand().onCommand(player, null, null, realArgs);
			}
		}else if (versionCommandAliases.contains(cmdName) && !player.hasPermission("Vanilla.default.version")){
			if (!configHandler.isHidingVersionCommand()){
				plugin.getVersionCommand().onCommand(player, null, null, realArgs);
			}
		}else {
			handled = false;
		}
		
		if (handled){
			event.setCancelled(true);
		}
	}
}
