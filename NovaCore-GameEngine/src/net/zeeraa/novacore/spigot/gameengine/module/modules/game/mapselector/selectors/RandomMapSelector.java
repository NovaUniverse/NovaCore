package net.zeeraa.novacore.spigot.gameengine.module.modules.game.mapselector.selectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.GameMapData;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.mapselector.MapSelector;

/**
 * This {@link MapSelector} selects a random map from the map list
 * 
 * @author Zeeraa
 */
public class RandomMapSelector extends MapSelector {
	@Override
	public List<GameMapData> getMaps() {
		return new ArrayList<>(GameManager.getInstance().getAllLoadedMaps().values());
	}

	@Override
	public GameMapData getMapToUse() {
		List<GameMapData> maps = getMaps();
		if (maps.size() == 0) {
			return null;
		}
		
		return maps.get(new Random().nextInt(maps.size()));
	}
}