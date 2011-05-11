package com.niccholaspage.Vanilla;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private boolean createFile(String file){
		File f = new File(file);
		if (!f.exists()){
			if (file.endsWith("/")){
				f.mkdir();
			}else {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}else {
			return false;
		}
    }
	
	private void writeDefaultNode(String node, Object value, Configuration config){
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
    @Override
	public void onEnable() {
    	PlayerListener playerListener = new PlayerListener(){
    		public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    			Player player = event.getPlayer();
    			String cmdName = event.getMessage().split("\\s+")[0].substring(1);
    			if (cmdName.equalsIgnoreCase("plugins") || cmdName.equalsIgnoreCase("pl")){
    				event.setCancelled(true);
        	    	Plugin[] plugins = getServer().getPluginManager().getPlugins();
        	    	String list = "Plugins: ";
        	    	for (int i = 0; i < plugins.length; i++){
        	    		if (!hiddenPlugins.contains(plugins[i].getDescription().getName().toLowerCase()))
        	    			list += "¤a" + plugins[i].getDescription().getName() + "¤f, ";
        	    	}
        	    	player.sendMessage(list.substring(0, list.length() - 2));
    			}
    		}
    	};
    	setupPermissions();
    	createFile("plugins/Vanilla/");
    	createFile("plugins/Vanilla/config.yml");
    	Configuration config = new Configuration(new File("plugins/Vanilla/config.yml"));
    	config.load();
    	writeDefaultNode("Vanilla", "", config);
    	writeDefaultNode("Vanilla.hiddenplugins", "", config);
    	writeDefaultNode("Vanilla.hideplugincommand", false, config);
    	writeDefaultNode("Vanilla.hideversioncommand", false, config);
    	config.save();
    	String[] split = config.getString("Vanilla.hiddenplugins", "").split(",");
    	for (int i = 0; i < split.length; i++){
    		hiddenPlugins.add(split[i].toLowerCase());
    	}
    	getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	return true;
    }
}