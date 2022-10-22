package net.zeeraa.novacore.spigot.language;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Language {
	private String languageCode;
	private String displayName;
	private Map<String, String> content;

	public Language(String languageCode, String displayName) {
		this.languageCode = languageCode;
		this.displayName = displayName;

		this.content = new HashMap<>();
	}

	/**
	 * Get the language code
	 * 
	 * @return language code
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * Get the display name of the language
	 * 
	 * @return display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Get a map containing all the content
	 * 
	 * @return map with the content
	 */
	public Map<String, String> getContent() {
		return content;
	}
	
	public void addLanguage(JSONObject json) {
		
	}
}