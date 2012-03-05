package com.niccholaspage.Vanilla.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.niccholaspage.Vanilla.Phrase;
import com.niccholaspage.Vanilla.Vanilla;

public class VanillaCommand implements CommandExecutor {
	private final Vanilla plugin;
	
	public VanillaCommand(Vanilla plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		boolean reloadPermission = sender.hasPermission("Vanilla.reload");
		
		if (args.length < 1){
			if (!sender.hasPermission("Vanilla.version")){
				return true;
			}
			
			sender.sendMessage(ChatColor.BLUE + Phrase.VANILLA_COMMAND_CREDIT.parse(plugin.getDescription().getVersion()));
			
			if (reloadPermission){
				sender.sendMessage(ChatColor.BLUE + Phrase.VANILLA_CONFIG_RELOAD_HOW_TO.parse(cmd.getName()));
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reload")){
			if (!reloadPermission){
				return true;
			}
			
			plugin.loadConfig();
			
			sender.sendMessage(ChatColor.BLUE + Phrase.VANILLA_CONFIG_RELOADED.parse());
		}
		return true;
	}
}
