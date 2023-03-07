package net.zeeraa.novacore.spigot.gameengine.module.modules.game;

import java.io.File;

import org.json.JSONObject;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.GameMapData;

/**
 * This class is used to load game maps
 * 
 * @author Zeeraa
 */
public abstract class MapReader {
	private String loaderName;

	public MapReader(String loaderName) {
		this.loaderName = loaderName;
	}

	/**
	 * Read {@link GameMapData} from a {@link JSONObject}
	 * 
	 * @param json           The {@link JSONObject} to read
	 * @param worldDirectory The directory containing the worlds
	 * @param owner          The {@link Game} owner of the map
	 * @return Instance of the {@link GameMapData} or <code>null</code> on failure
	 */
	public abstract GameMapData readMap(JSONObject json, File worldDirectory, Game owner);

	public String getLoaderName() {
		return this.loaderName;
	}
}