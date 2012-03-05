package com.niccholaspage.Vanilla;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.niccholaspage.Vanilla.commands.VanillaCommand;

public class Vanilla extends JavaPlugin {
	private ConfigHandler configHandler;
	
	public void onEnable(){
		//Listeners
		
		loadConfig();
		
		getCommand("vanilla").setExecutor(new VanillaCommand(this));
	}
	
	public ConfigHandler getConfigHandler(){
		return configHandler;
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