package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.readers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.MapReader;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.GameMapData;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModuleManager;
import net.zeeraa.novacore.spigot.gameengine.utils.HologramData;
import net.zeeraa.novacore.spigot.utils.LocationData;

/**
 * A slightly improved map reader
 * 
 * @author Zeeraa
 * @since 2.0.0
 */
public class NovaMapReader extends MapReader {
	public static final String NAME = "nova";

	public NovaMapReader() {
		super(NAME);
	}

	@Override
	public GameMapData readMap(JSONObject json, File worldDirectory) {
		String mapName = json.getString("map_name");
		String displayName = json.getString("display_name");
		String worldFileName = json.getString("world_file");
		String description = "";

		boolean enabled = true;

		File worldFile = new File(worldDirectory.getPath() + File.separator + worldFileName);

		if (json.has("description")) {
			description = json.getString("description");
		}

		if (json.has("enabled")) {
			enabled = json.getBoolean("enabled");
		}

		LocationData spectatorLocation = null;
		if (json.has("spectator_location")) {
			spectatorLocation = new LocationData(json.getJSONObject("spectator_location"));
		}

		List<MapModule> mapModules = new ArrayList<MapModule>();
		if (json.has("map_modules")) {
			JSONObject modulesJSON = json.getJSONObject("map_modules");

			for (String key : modulesJSON.keySet()) {
				boolean critical = false;

				JSONObject moduleData = modulesJSON.getJSONObject(key);
				if (moduleData.has("is_critical")) {
					critical = moduleData.getBoolean("is_critical");
				}

				if (MapModuleManager.hasMapModule(key)) {
					Class<? extends MapModule> clazz = MapModuleManager.getMapModule(key);
					MapModule mapModule;
					try {
						mapModule = MapModuleManager.loadMapModule(clazz, modulesJSON.getJSONObject(key));

						mapModule.setName(key);

						mapModules.add(mapModule);
					} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | JSONException e) {
						Log.error("Failed to load MapModule " + clazz.getName() + " from map " + mapName + ". Caused by " + e.getClass().getName());
						e.printStackTrace();
						if (e.getCause() != null) {
							Log.info("Exception cause printed below");
							e.getCause().printStackTrace();
						}

						if (critical) {
							Log.error("NovaMapReader", "Failed to read map " + mapName + " because map module " + key + " could not be loaded");
							return null;
						}
					}
				} else {
					Log.warn("NovaMapReader", "Map module " + key + " was not found. Map name: " + mapName);
					if (critical) {
						Log.error("NovaMapReader", "Failed to read map " + mapName + " because map module " + key + " was not found");
						return null;
					}
				}
			}
		}

		ArrayList<LocationData> starterLocations = new ArrayList<>();
		if (json.has("starter_locations")) {
			JSONArray starterLocationsJSON = json.getJSONArray("starter_locations");
			for (int i = 0; i < starterLocationsJSON.length(); i++) {
				JSONObject starterLocationJSON = starterLocationsJSON.getJSONObject(i);

				LocationData location = new LocationData(starterLocationJSON);

				location.center();

				starterLocations.add(location);
			}
		}

		List<HologramData> holograms = new ArrayList<>();

		if (json.has("holograms")) {
			JSONArray hologramsJson = json.getJSONArray("holograms");

			for (int i = 0; i < hologramsJson.length(); i++) {
				JSONObject hologramJson = hologramsJson.getJSONObject(i);

				LocationData location = new LocationData(hologramJson.getJSONObject("location"));

				List<String> lines = new ArrayList<>();

				JSONArray linesJson = hologramJson.getJSONArray("lines");

				for (int j = 0; j < linesJson.length(); i++) {
					lines.add(linesJson.getString(j));
				}

				holograms.add(new HologramData(lines, location));
			}
		}

		return new GameMapData(mapModules, starterLocations, spectatorLocation, mapName, displayName, description, worldFile, enabled, holograms);
	}
}