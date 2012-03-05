package com.niccholaspage.Vanilla;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Vanilla extends JavaPlugin {
	public List<String> hiddenPlugins = new ArrayList<String>();
	public PermissionHandler Permissions;
	public boolean hidePluginCommand = false;
	public boolean hideVersionCommand = false;
    @Override
	public void onDisable() {
		System.out.println("Vanilla Disabled");
		
	}

    private void setupPermissions(){
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
        if (Permissions == null) {
            if (test != null) {
                Permissions = ((Permissions)test).getHandler();
            } else {
            	System.out.println("Permissions system not detected, only OPs will be able to use the command.");
            }
        }
    }
    boolean hasPermission(CommandSender sender, String node){
    	if (sender instanceof Player){
    		if (Permissions == null){
    			return ((Player)sender).isOp();
    		}else {
    			return Permissions.has((Player)sender, node);
    		}
    	}else {
    		return true;
    	}
    }
    @Override
	public void onEnable() {
    	BukkitListener playerlistener = new BukkitListener(this);
    	setupPermissions();
    	readConfig();
    	getServer().getPluginManager().registerEvents(playerlistener, this);
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
    
    private void readConfig(){
    	getDataFolder().mkdir();
    	File file = new File(getDataFolder(), "config.yml");
    	file.mkdir();
    	getConfig().addDefault("Vanilla", "");
    	getConfig().addDefault("Vanilla.hiddenplugins", "");
    	getConfig().addDefault("Vanilla.hideplugincommand", false);
    	getConfig().addDefault("Vanilla.hideversioncommand", false);
		getConfig().options().copyDefaults(true);
		saveConfig();
		
    	hideVersionCommand = getConfig().getBoolean("Vanilla.hideversioncommand", false);
    	hidePluginCommand = getConfig().getBoolean("Vanilla.hideplugincommand", false);
    	String[] split = getConfig().getString("Vanilla.hiddenplugins", "").split(",");
    	hiddenPlugins.clear();
    	for (int i = 0; i < split.length; i++){
    		hiddenPlugins.add(split[i].toLowerCase());
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	if (args.length < 1){
    		if (!hasPermission(sender, "Vanilla.version")) return true;
    		sender.sendMessage(ChatColor.GREEN + "Vanilla version " + getDescription().getVersion());
    		return true;
    	}else {
    		if (args[0].equalsIgnoreCase("reload")){
        		if (!hasPermission(sender, "Vanilla.reload")) return true;
        		readConfig();
        		sender.sendMessage(ChatColor.GREEN + "Vanilla configuration has been reloaded.");
    		}
    	}
    	return true;
    }
}