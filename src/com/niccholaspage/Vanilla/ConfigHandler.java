package com.niccholaspage.Vanilla;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {
	private final File configFile;
	
	private final File phrasesFile;
	
	private final YamlConfiguration config;
	
	private Set<String> hiddenPlugins;
	
	private boolean hidePluginsCommand;
	
	private boolean hideVersionCommand;
	
	public ConfigHandler(File configFile, File phrasesFile){
		this.configFile = configFile;
		
		this.phrasesFile = phrasesFile;
		
		this.config = YamlConfiguration.loadConfiguration(configFile);
		
		hiddenPlugins = new HashSet<String>();
		
		load();
	}
	
	public void load(){
		config.setDefaults(getDefaultConfig());
		
		config.options().copyDefaults(true);
		
		//Converting of old config
		ConfigurationSection VanillaSection = config.getConfigurationSection("Vanilla");
		
		if (VanillaSection != null){
			for (String key : VanillaSection.getKeys(false)){
				config.set(key, VanillaSection.get(key));
			}
			
			config.set("Vanilla", null);
		}
		
		//Converts old hidden plugins
		if (config.isString("hiddenplugins")){
			String[] oldHiddenPlugins = config.getString("hiddenplugins").split(",");
			
			List<String> newHiddenPlugins = new ArrayList<String>();
			
			for (String oldHiddenPlugin : oldHiddenPlugins){
				newHiddenPlugins.add(oldHiddenPlugin);
			}
			
			config.set("hiddenplugins", newHiddenPlugins);
		}
		
		//Convert hideplugincommand boolean to hidepluginscommand
		if (config.isBoolean("hideplugincommand")){
			config.set("hidepluginscommand", config.getBoolean("hideplugincommand"));
			
			config.set("hideplugincommand", null);
		}
		
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<String> hiddenPluginsList = config.getStringList("hiddenplugins");
		
		if (hiddenPluginsList != null){
			for (String hiddenPlugin : hiddenPluginsList){
				hiddenPlugins.add(hiddenPlugin.toLowerCase());
			}
		}
		
		hidePluginsCommand = config.getBoolean("hidepluginscommand");
		
		hideVersionCommand = config.getBoolean("hideversioncommand");
		
		for (Phrase phrase : Phrase.values()){
			phrase.reset();
		}
		
		if (!phrasesFile.exists()){
			return;
		}
		
		YamlConfiguration phrasesConfig = YamlConfiguration.loadConfiguration(phrasesFile);
		
		Set<String> keys = phrasesConfig.getKeys(false);
		
		for (Phrase phrase : Phrase.values()){
			String phraseConfigName = phrase.getConfigName();
			
			if (keys.contains(phraseConfigName)){
				phrase.setMessage(phrasesConfig.getString(phraseConfigName));
			}
		}
	}
	
	private YamlConfiguration getDefaultConfig(){
		YamlConfiguration defaultConfig = new YamlConfiguration();
		
		defaultConfig.set("hiddenplugins", "");
		defaultConfig.set("hidepluginscommand", false);
		defaultConfig.set("hideversioncommand", false);
		
		return defaultConfig;
	}
	
	public Set<String> getHiddenPlugins(){
		return hiddenPlugins;
	}
	
	public boolean isHidingPluginsCommand(){
		return hidePluginsCommand;
	}
	
	public boolean isHidingVersionCommand(){
		return hideVersionCommand;
	}
}
