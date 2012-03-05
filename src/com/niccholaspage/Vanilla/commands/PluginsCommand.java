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
		sender.sendMessage("Plugins: " + getPluginList());

		return true;
	}

	private String getPluginList(){
		StringBuilder pluginList = new StringBuilder();

		Plugin[] plugins = vanillaPlugin.getServer().getPluginManager().getPlugins();

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
		}

		return pluginList.toString();
	}
}
