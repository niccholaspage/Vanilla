package com.niccholaspage.Vanilla;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
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
	
	private void writeNode(String node, Object value, Configuration config){
		if (config.getProperty(node) == null) config.setProperty(node, value);
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
    private boolean hasPermission(CommandSender sender, String node){
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
    	setupPermissions();
    	readConfig();
    	getServer().getPluginManager().registerEvent(this, this);
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
    
    private void readConfig(){
    	getDataFolder().mkdir();
    	File file = new File(getDataFolder(), "config.yml");
    	file.mkdir();
    	Configuration config = new Configuration(file);
    	config.load();
    	writeNode("Vanilla", "", config);
    	writeNode("Vanilla.hiddenplugins", "", config);
    	writeNode("Vanilla.hideplugincommand", false, config);
    	writeNode("Vanilla.hideversioncommand", false, config);
    	config.save();
    	hideVersionCommand = config.getBoolean("Vanilla.hideversioncommand", false);
    	hidePluginCommand = config.getBoolean("Vanilla.hideplugincommand", false);
    	String[] split = config.getString("Vanilla.hiddenplugins", "").split(",");
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
    
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    			Player player = event.getPlayer();
    			String[] split = event.getMessage().split("\\s+");
    			String cmdName = split[0].substring(1);
    			if (cmdName.equalsIgnoreCase("plugins") || cmdName.equalsIgnoreCase("pl")){
    				if (hasPermission(player, "Vanilla.def.plugins")) return;
    				event.setCancelled(true);
    				if (hidePluginCommand) return;
        	    	Plugin[] plugins = getServer().getPluginManager().getPlugins();
        	    	String list = "Plugins: ";
        	    	for (int i = 0; i < plugins.length; i++){
        	    		if (!hiddenPlugins.contains(plugins[i].getDescription().getName().toLowerCase()))
        	    			list += plugins[i].isEnabled() ? ChatColor.GREEN : ChatColor.RED + plugins[i].getDescription().getName() + ChatColor.WHITE + ", ";
        	    	}
        	    	player.sendMessage(list.substring(0, list.length() - 2));
    			}else if (cmdName.equalsIgnoreCase("ver") || cmdName.equalsIgnoreCase("version")){
    				if (hasPermission(player, "Vanilla.def.version")) return;
    				if (hideVersionCommand){
    					event.setCancelled(true);
    					return;
    				}
    				if (split.length > 1){
    					String pluginName = split[1].toLowerCase();
    					if (hiddenPlugins.contains(pluginName)){
    						player.sendMessage("This server is not running any plugin by that name.");
    						player.sendMessage("Use /plugins to get a list of plugins.");
    						event.setCancelled(true);
    					}
    				}
    			}
    		}
}