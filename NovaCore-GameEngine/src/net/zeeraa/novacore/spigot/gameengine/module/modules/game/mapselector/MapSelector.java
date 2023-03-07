package net.zeeraa.novacore.spigot.gameengine.module.modules.game.mapselector;

import java.util.List;
import java.util.stream.Collectors;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.GameMapData;

/**
 * This class can be used to create map selection systems for games
 * 
 * @author Zeeraa
 */
public abstract class MapSelector {
	/**
	 * Get a list with all maps
	 * 
	 * @return {@link List} with {@link GameMapData}
	 */
	public List<GameMapData> getMaps() {
		return GameManager.getInstance().getLoadedMapsForActiveGame();
	}

	/**
	 * Get a map by name
	 * 
	 * @param name The name of the map
	 * @return The {@link GameMapData} or <code>null</code> if the map was not found
	 */
	public GameMapData getMap(String name) {
		return GameManager.getInstance().getAllLoadedMaps().get(name);
	}

	/**
	 * Get the map to use for the game
	 * 
	 * @return {@link GameMapData} to use
	 */
	public abstract GameMapData getMapToUse();
}