package net.zeeraa.novacore.spigot.gameengine.module.modules.game.mapselector.selectors.guivoteselector;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.events.GameStartEvent;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.GameMapData;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.mapselector.MapSelector;
import net.zeeraa.novacore.spigot.gameengine.module.modules.gamelobby.events.PlayerJoinGameLobbyEvent;
import net.zeeraa.novacore.spigot.language.LanguageManager;
import net.zeeraa.novacore.spigot.module.modules.customitems.CustomItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GUIMapVote extends MapSelector implements Listener {
	private static GUIMapVote instance;

	private Map<UUID, String> votes;
	private Map<UUID, Inventory> playerVoteInventory;

	private String forcedMap;

	public static GUIMapVote getInstance() {
		return instance;
	}

	public GUIMapVote() {
		GUIMapVote.instance = this;

		this.votes = new HashMap<>();
		this.playerVoteInventory = new HashMap<>();

		this.forcedMap = null;

		if (!CustomItemManager.getInstance().hasCustomItem(GUIMapVoteMenuIcon.class)) {
			try {
				CustomItemManager.getInstance().addCustomItem(GUIMapVoteMenuIcon.class);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public void showPlayer(Player player) {
			List<GameMapData> maps = getMaps();

			int slots = 9;

			while (slots < maps.size()) {
				slots += 9;
			}

			Inventory voteInventory = Bukkit.createInventory(new MapVoteInventoryHolder(), slots, ChatColor.GOLD + "" + ChatColor.BOLD + LanguageManager.getString(player, "novacore.game.lobby.map_vote.vote_for_map"));
			playerVoteInventory.put(player.getUniqueId(), voteInventory);

			this.updateInventory(player);

		player.openInventory(playerVoteInventory.get(player.getUniqueId()));
	}

	private void updateInventory(Player player) {
		List<GameMapData> maps = getMaps();

		if (playerVoteInventory.containsKey(player.getUniqueId())) {
			Inventory voteInventory = playerVoteInventory.get(player.getUniqueId());
			MapVoteInventoryHolder holder = (MapVoteInventoryHolder) voteInventory.getHolder();

			holder.getMapSlots().clear();

			int slot = 0;

			for (GameMapData map : maps) {
				ItemStack voteItem = new ItemStack(map.isEnabled() ? Material.EMERALD : Material.COAL);

				ItemMeta meta = voteItem.getItemMeta();

				boolean selected = false;

				if (votes.containsKey(player.getUniqueId())) {
					if (map.getMapName().equals(votes.get(player.getUniqueId()))) {
						selected = true;
					}
				}

				List<String> lore = new ArrayList<>();

				Collections.addAll(lore, map.getDescription().split("\n"));

				if (!map.isEnabled()) {
					lore.add("");
					lore.add(ChatColor.RESET + "" + ChatColor.DARK_RED + LanguageManager.getString(player, "novacore.game.lobby.map_vote.disabled"));
				}

				meta.setLore(lore);

				meta.setDisplayName((selected ? ChatColor.GREEN : ChatColor.GOLD) + "" + ChatColor.BOLD + map.getDisplayName());

				if (selected) {
					meta.addEnchant(Enchantment.DURABILITY, 1, false);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}

				voteItem.setItemMeta(meta);

				voteInventory.setItem(slot, voteItem);

				if (map.isEnabled()) {
					holder.getMapSlots().put(slot, map.getMapName());
				}

				slot++;

				if (voteInventory.getSize() < slot) {
					break;
				}
			}
		}
	}

	@Override
	public GameMapData getMapToUse() {
		if (forcedMap != null) {
			Log.info("Using forced map " + forcedMap);
			return this.getMap(forcedMap);
		}

		List<VoteEntry> entries = new ArrayList<>();
		List<GameMapData> maps = getMaps();

		votes.forEach((uuid, name) -> {
			VoteEntry entry = entries.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
			if (entry == null) {
				entry = new VoteEntry(name);
				entries.add(entry);
			}
			entry.addVote();
		});

		if (votes.size() == 0) {
			Log.info("No votes. Using random map");
			Random random = new Random();
			return maps.get(random.nextInt(maps.size()));
		}

		Log.info("Got at least 1 vote. Finding top map");
		entries.sort(new VoteEntryComparator());

		entries.forEach(e -> Log.trace("Map " + e.getName() + " got " + e.getVotes() + " votes"));

		VoteEntry first = entries.stream().findFirst().get();

		Log.info("Map to use: " + first.getName());

		return this.getMap(first.getName());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent e) {
		votes.remove(e.getPlayer().getUniqueId());

		playerVoteInventory.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onGameStart(GameStartEvent e) {
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinGameLobby(PlayerJoinGameLobbyEvent e) {
		// got a wierd glitch here where it dupes?
		e.getPlayer().getInventory().addItem(CustomItemManager.getInstance().getCustomItemStack(GUIMapVoteMenuIcon.class, e.getPlayer()));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent e) {
		if (GameManager.getInstance().hasActiveGame()) {
			if (GameManager.getInstance().getActiveGame().hasStarted()) {
				// Prevent spectators in game from opening gui
				return;
			}
		}

		if (e.getInventory().getHolder() instanceof MapVoteInventoryHolder) {
			e.setCancelled(true);
			if (e.getClickedInventory() != null) {
				if (e.getClickedInventory().getHolder() instanceof MapVoteInventoryHolder) {
					MapVoteInventoryHolder holder = (MapVoteInventoryHolder) e.getClickedInventory().getHolder();

					Player player = (Player) e.getWhoClicked();

					if (holder.getMapSlots().containsKey(e.getSlot())) {
						String mapName = holder.getMapSlots().get(e.getSlot());

						boolean changed = false;
						boolean forced = false;

						if (e.getClick() == ClickType.MIDDLE) {
							if (player.hasPermission("novacore.gamelobby.forcemap")) {
								if (forcedMap != null && forcedMap.equals(mapName)) {
									forcedMap = null;
									Bukkit.getServer().broadcast(ChatColor.GREEN + "Set forced map to null", "novacore.gamelobby.forcemap");
									Log.info("Set forced map to null");
								} else {
									forcedMap = mapName;
									Bukkit.getServer().broadcast(ChatColor.GREEN + "Set forced map to " + mapName, "novacore.gamelobby.forcemap");
									Log.info("Set forced map to " + mapName);
									forced = true;
								}
							}
						}

						if (votes.containsKey(player.getUniqueId())) {
							if (votes.get(player.getUniqueId()).equals(mapName)) {
								return;
							}
							votes.remove(player.getUniqueId());
							changed = true;
						}

						votes.put(player.getUniqueId(), mapName);

						GameMapData map = getMaps().stream().filter(m -> m.getMapName().equalsIgnoreCase(mapName)).findFirst().orElse(null);
						if (map != null) {
							GUIMapSelectorPlayerVotedEvent event = new GUIMapSelectorPlayerVotedEvent(player, forced, map);
							Bukkit.getServer().getPluginManager().callEvent(event);
						}

						VersionIndependentUtils.get().playSound(player, player.getLocation(), VersionIndependentSound.ORB_PICKUP, 1F, 1F);
						player.sendMessage(ChatColor.GREEN + (changed ? LanguageManager.getString(player, "novacore.game.lobby.map_vote.vote.changed") : LanguageManager.getString(player, "novacore.game.lobby.map_vote.vote.for")) + this.getMap(mapName).getDisplayName());

						updateInventory(player);
					}
				}
			}
		}
	}
}

class VoteEntryComparator implements Comparator<VoteEntry> {
	@Override
	public int compare(VoteEntry o1, VoteEntry o2) {
		return o2.getVotes() - o1.getVotes();
	}
}

class VoteEntry {
	private String name;
	private int votes;

	public VoteEntry(String name) {
		this.name = name;
		this.votes = 0;
	}

	public String getName() {
		return name;
	}

	public int getVotes() {
		return votes;
	}

	public void addVote() {
		votes++;
	}
}