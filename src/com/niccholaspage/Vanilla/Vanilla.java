package com.niccholaspage.Vanilla;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.niccholaspage.Vanilla.commands.PluginsCommand;
import com.niccholaspage.Vanilla.commands.VanillaCommand;
import com.niccholaspage.Vanilla.commands.VersionCommand;
import com.niccholaspage.Vanilla.listeners.VanillaPlayerListener;

public class Vanilla extends JavaPlugin {
	private ConfigHandler configHandler;
	
	private CommandExecutor pluginsCommand;
	
	private CommandExecutor versionCommand;
	
	private int buildVersion;
	
	public void onEnable(){
		new VanillaPlayerListener(this);
		
		getDataFolder().mkdirs();
		
		loadConfig();
		
		try {
			buildVersion = Integer.parseInt(getServer().getBukkitVersion());
		}catch (NumberFormatException e){
			buildVersion = -1;
		}
		
		getCommand("vanilla").setExecutor(new VanillaCommand(this));
		
		pluginsCommand = new PluginsCommand(this);
		
		versionCommand = new VersionCommand(this);
	}
	
	public ConfigHandler getConfigHandler(){
		return configHandler;
	}
	
	public int getBuildVersion(){
		return buildVersion;
	}
	
	public CommandExecutor getPluginsCommand(){
		return pluginsCommand;
	}
	
	public CommandExecutor getVersionCommand(){
		return versionCommand;
	}
	
	public void loadConfig(){
		File configFile = new File(getDataFolder(), "config.yml");

		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File phrasesFile = new File(getDataFolder(), "phrases.yml");

		configHandler = new ConfigHandler(configFile, phrasesFile);
	}
}