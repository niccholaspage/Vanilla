package com.niccholaspage.Vanilla.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import com.niccholaspage.Vanilla.Vanilla;

public class VersionCommand implements CommandExecutor {
	private final Vanilla vanillaPlugin;

	public VersionCommand(Vanilla vanillaPlugin){
		this.vanillaPlugin = vanillaPlugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (args.length == 0){
			sender.sendMessage("This server is running " + Bukkit.getName() + " version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")");
		} else {
			StringBuilder name = new StringBuilder();

			for (String arg : args) {
				if (name.length() > 0) {
					name.append(' ');
				}

				name.append(arg);
			}

			Plugin plugin = Bukkit.getPluginManager().getPlugin(name.toString());
			
			if (plugin == null || vanillaPlugin.getConfigHandler().getHiddenPlugins().contains(plugin.getDescription().getName().toLowerCase())){
				sender.sendMessage("This server is not running any plugin by that name.");
				sender.sendMessage("Use /plugins to get a list of plugins.");
			}else {
				PluginDescriptionFile desc = plugin.getDescription();

				sender.sendMessage(ChatColor.GREEN + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());

				if (desc.getDescription() != null) {
					sender.sendMessage(desc.getDescription());
				}

				if (desc.getWebsite() != null) {
					sender.sendMessage("Website: " + ChatColor.GREEN + desc.getWebsite());
				}

				if (!desc.getAuthors().isEmpty()) {
					if (desc.getAuthors().size() == 1) {
						sender.sendMessage("Author: " + getAuthors(desc));
					} else {
						sender.sendMessage("Authors: " + getAuthors(desc));
					}
				}
			}
		}
		return true;
	}

	private String getAuthors(final PluginDescriptionFile desc) {
		StringBuilder result = new StringBuilder();
		List<String> authors = desc.getAuthors();

		for (int i = 0; i < authors.size(); i++) {
			if (result.length() > 0) {
				result.append(ChatColor.WHITE);

				if (i < authors.size() - 1) {
					result.append(", ");
				} else {
					result.append(" and ");
				}
			}

			result.append(ChatColor.GREEN);
			result.append(authors.get(i));
		}

		return result.toString();
	}
}
