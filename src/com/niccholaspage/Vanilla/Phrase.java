package com.niccholaspage.Vanilla;

public enum Phrase {
	VANILLA_COMMAND_CREDIT("Vanilla $1 by niccholaspage"),
	VANILLA_CONFIG_RELOAD_HOW_TO("Type /$1 reload to reload the configuration"),
	VANILLA_CONFIG_RELOADED("The Vanilla configuration has been reloaded.");

	private String defaultMessage;
	
	private String message;

	private Phrase(String defaultMessage){
		this.defaultMessage = defaultMessage;
		
		message = defaultMessage + "";
	}

	public void setMessage(String message){
		this.message = message;
	}

	private String getMessage(){
		return message;
	}
	
	public void reset(){
		message = defaultMessage + "";
	}
	
	public String getConfigName(){
		return name().toLowerCase();
	}
	
	public String parse(String... params){
		String parsedMessage = getMessage();

		if (params != null){
			for (int i = 0; i < params.length; i++){
				parsedMessage = parsedMessage.replace("$" + (i + 1), params[i]);
			}
		}

		return parsedMessage;
	}
}