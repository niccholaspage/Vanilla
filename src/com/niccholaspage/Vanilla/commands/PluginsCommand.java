package com.niccholaspage.Vanilla.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.niccholaspage.Vanilla.Vanilla;

public class PluginsCommand implements CommandExecutor {
	private final Vanilla vanillaPlugin;

	public PluginsCommand(Vanilla plugin){
		this.vanillaPlugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (vanillaPlugin.getBuildVersion() == -1 || vanillaPlugin.getBuildVersion() > 1421){
			sender.sendMessage("Plugins " + getPluginList());
			
			return true;
		}
		
		sender.sendMessage("Plugins: " + getPluginList());

		return true;
	}

	private String getPluginList(){
		StringBuilder pluginList = new StringBuilder();

		Plugin[] plugins = vanillaPlugin.getServer().getPluginManager().getPlugins();

		int pluginCount = 0;

		for (Plugin plugin : plugins){
			String name = plugin.getDescription().getName();

			if (vanillaPlugin.getConfigHandler().getHiddenPlugins().contains(name.toLowerCase())){
				continue;
			}

			if (pluginList.length() > 0){
				pluginList.append(ChatColor.WHITE);
				pluginList.append(", ");
			}

			pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);

			pluginList.append(name);
			
			pluginCount++;
		}
		
		if (vanillaPlugin.getBuildVersion() == -1 || vanillaPlugin.getBuildVersion() > 1421){
			return "(" + pluginCount + "): " + pluginList.toString();
		}
		
		return pluginList.toString();
	}
}
