package com.niccholaspage.Vanilla;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;


public class BukkitListener implements Listener {
    private final Vanilla plugin;

    public BukkitListener(Vanilla instance) {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String[] split = event.getMessage().split("\\s+");
		String cmdName = split[0].substring(1);
		if (cmdName.equalsIgnoreCase("plugins") || cmdName.equalsIgnoreCase("pl")){
			if (plugin.hasPermission(player, "Vanilla.def.plugins")) return;
			event.setCancelled(true);
			if (plugin.hidePluginCommand) return;
	    	Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
	    	String list = "Plugins: ";
	    	for (int i = 0; i < plugins.length; i++){
	    		if (!plugin.hiddenPlugins.contains(plugins[i].getDescription().getName().toLowerCase()))
	    			list += plugins[i].isEnabled() ? ChatColor.GREEN : ChatColor.RED + plugins[i].getDescription().getName() + ChatColor.WHITE + ", ";
	    	}
	    	player.sendMessage(list.substring(0, list.length() - 2));
		}else if (cmdName.equalsIgnoreCase("ver") || cmdName.equalsIgnoreCase("version")){
			if (plugin.hasPermission(player, "Vanilla.def.version")) return;
			if (plugin.hideVersionCommand){
				event.setCancelled(true);
				return;
			}
			if (split.length > 1){
				String pluginName = split[1].toLowerCase();
				if (plugin.hiddenPlugins.contains(pluginName)){
					player.sendMessage("This server is not running any plugin by that name.");
					player.sendMessage("Use /plugins to get a list of plugins.");
					event.setCancelled(true);
				}
			}
		}
	}
}
