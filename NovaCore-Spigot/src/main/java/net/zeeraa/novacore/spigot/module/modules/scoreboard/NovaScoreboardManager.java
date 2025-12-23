package net.zeeraa.novacore.spigot.module.modules.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.commons.utils.DelayedRunner;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.NovaCoreGameVersion;
import net.zeeraa.novacore.spigot.module.MissingPluginDependencyException;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.text.ScoreboardLine;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.title.ScoreboardTitle;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.title.StaticScoreboardTitle;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class NovaScoreboardManager extends NovaModule implements Listener {
	public static final List<ChatColor> VALID_COLORS = new ArrayList<>();

	private static NovaScoreboardManager instance;

	private Task tickTask;

	public static NovaScoreboardManager getInstance() {
		return instance;
	}

	static {
		VALID_COLORS.add(ChatColor.AQUA);
		VALID_COLORS.add(ChatColor.BLACK);
		VALID_COLORS.add(ChatColor.BLUE);
		VALID_COLORS.add(ChatColor.DARK_AQUA);
		VALID_COLORS.add(ChatColor.DARK_BLUE);
		VALID_COLORS.add(ChatColor.DARK_GRAY);
		VALID_COLORS.add(ChatColor.DARK_GREEN);
		VALID_COLORS.add(ChatColor.DARK_PURPLE);
		VALID_COLORS.add(ChatColor.DARK_RED);
		VALID_COLORS.add(ChatColor.GOLD);
		VALID_COLORS.add(ChatColor.GRAY);
		VALID_COLORS.add(ChatColor.GREEN);
		VALID_COLORS.add(ChatColor.LIGHT_PURPLE);
		VALID_COLORS.add(ChatColor.RED);
		VALID_COLORS.add(ChatColor.WHITE);
		VALID_COLORS.add(ChatColor.YELLOW);
	}

	private int defaultLineCount;

	private ScoreboardTitle defaultTitle;

	private Map<UUID, NovaScoreboardPlayerBoard> boards;

	private Map<Player, ChatColor> globalPlayerNameColors;
	private Map<Integer, ScoreboardLine> globalLines;

	private ScoreboardLibrary scoreboardLibrary = null;

	public NovaScoreboardManager() {
		super("Novacore.ScoreboardManager");
	}

	@Override
	public void onLoad() {
		boards = new HashMap<>();
		globalPlayerNameColors = new HashMap<>();
		defaultTitle = new StaticScoreboardTitle("Score");
		globalLines = new HashMap<>();
		NovaScoreboardManager.instance = this;
		defaultLineCount = 15;
		tickTask = new SimpleTask(getPlugin(), this::tick, 1L);
	}

	@Override
	public void onEnable() throws Exception {
		if (VersionIndependentUtils.get().getNovaCoreGameVersion() == NovaCoreGameVersion.V_1_16) {
			scoreboardLibrary = new NoopScoreboardLibrary();
			Log.error("NovaScoreboardManager", "1.16 is not supported by this scoreboard implementation due to a bug. This module will still be loaded but all scoreboard calls will be no-oped");
		} else {
			if (VersionIndependentUtils.get().getNovaCoreGameVersion().matchesAny(NovaCoreGameVersion.V_1_12, NovaCoreGameVersion.V_1_16)) {
				if (Bukkit.getServer().getPluginManager().getPlugin("packetevents") == null) {
					Log.error("NovaScoreboardManager", "To use scoreboard on 1.12 or 1.16 you need to install packetevents https://github.com/retrooper/packetevents");
					throw new MissingPluginDependencyException("packetevents");
				}
			}

			try {
				scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(getPlugin());
			} catch (NoPacketAdapterAvailableException e) {
				// If no packet adapter was found, you can fallback to the no-op implementation:
				scoreboardLibrary = new NoopScoreboardLibrary();
				Log.error("NovaScoreboardManager", "Failed to init scoreboard api. All scoreboard calls will be no-oped. " + e.getClass().getName() + " " + e.getMessage());
			}
		}

		Task.tryStartTask(tickTask);

		Bukkit.getServer().getOnlinePlayers().forEach(this::initPlayer);
		DelayedRunner.runDelayed(() -> {
			if (isEnabled()) {
				globalContentUpdate();
			}
		}, 1L);
	}

	@Override
	public void onDisable() throws Exception {
		Task.tryStopTask(tickTask);

		while (boards.size() > 0) {
			UUID key = boards.keySet().stream().findFirst().get();
			NovaScoreboardPlayerBoard board = boards.get(key);
			board.dispose();
			boards.remove(key);
		}
		globalPlayerNameColors.clear();

		if (scoreboardLibrary != null) {
			scoreboardLibrary.close();
		}
	}

	private void tick() {
		defaultTitle.tick();
		boards.values().stream().forEach(NovaScoreboardPlayerBoard::tick);
		globalLines.values().forEach(ScoreboardLine::tick);
	}

	public boolean initPlayer(Player player) {
		if (boards.containsKey(player.getUniqueId())) {
			return false;
		}
		NovaScoreboardPlayerBoard board = new NovaScoreboardPlayerBoard(this, player);
		boards.put(player.getUniqueId(), board);
		return true;
	}

	public void setDefaultTitle(ScoreboardTitle defaultTitle) {
		this.defaultTitle = defaultTitle;
	}

	public ScoreboardTitle getDefaultTitle() {
		return defaultTitle;
	}

	public ScoreboardLibrary getScoreboardLibrary() {
		return scoreboardLibrary;
	}

	public Map<Player, ChatColor> getGlobalPlayerNameColors() {
		return globalPlayerNameColors;
	}

	public void setGlobalLine(int line, ScoreboardLine content) {
		globalLines.put(line, content);
		globalContentUpdate();
	}

	public void setGlobalLines(Map<Integer, ScoreboardLine> lines) {
		lines.forEach((key, val) -> globalLines.put(key, val));
		globalContentUpdate();
	}

	public void clearGlobalLine(int line) {
		globalLines.remove(line);
		globalContentUpdate();
	}

	public void clearAllGlobalLines() {
		globalLines.clear();
		globalContentUpdate();
	}

	public void setPlayerLine(Player player, int line, ScoreboardLine content) {
		getPlayerBoard(player, b -> b.setPlayerLine(line, content));
	}

	public void setPlayerLine(UUID uuid, int line, ScoreboardLine content) {
		getPlayerBoard(uuid, b -> b.setPlayerLine(line, content));
	}

	public void clearPlayerLine(Player player, int line) {
		getPlayerBoard(player, b -> b.clearPlayerLine(line));
	}

	public void clearPlayerLine(UUID uuid, int line) {
		getPlayerBoard(uuid, b -> b.clearPlayerLine(line));
	}

	public void clearAllPlayerlLines(Player player) {
		getPlayerBoard(player, NovaScoreboardPlayerBoard::clearAllPlayerlLines);
	}

	public void clearAllPlayerlLines(UUID uuid) {
		getPlayerBoard(uuid, NovaScoreboardPlayerBoard::clearAllPlayerlLines);
	}

	public void globalContentUpdate() {
		boards.values().forEach(NovaScoreboardPlayerBoard::forceContentUpdate);
	}

	@Nullable
	public NovaScoreboardPlayerBoard getPlayerBoard(Player player) {
		return this.getPlayerBoard(player.getUniqueId());

	}

	@Nullable
	public NovaScoreboardPlayerBoard getPlayerBoard(UUID uuid) {
		return boards.get(uuid);
	}

	public boolean getPlayerBoard(Player player, Consumer<NovaScoreboardPlayerBoard> consumer) {
		return this.getPlayerBoard(player.getUniqueId(), consumer);

	}

	public boolean getPlayerBoard(UUID uuid, Consumer<NovaScoreboardPlayerBoard> consumer) {
		if (boards.containsKey(uuid)) {
			consumer.accept(boards.get(uuid));
			return true;
		}
		return false;
	}

	public Map<Integer, ScoreboardLine> getGlobalLines() {
		return globalLines;
	}

	public int getDefaultLineCount() {
		return defaultLineCount;
	}

	public void setDefaultLineCount(int defaultLineCount) {
		this.defaultLineCount = defaultLineCount;
	}

	public void setLineCount(int lineCount) {
		setDefaultLineCount(lineCount);
		boards.values().forEach(b -> b.setLineCount(lineCount));
	}

	public void setPlayerNameColor(Player player, ChatColor color) {
		globalPlayerNameColors.put(player, color);
		boards.values().forEach(NovaScoreboardPlayerBoard::forceTeamColorUpdate);
	}

	public void removePlayerNameColor(Player player) {
		if (globalPlayerNameColors.containsKey(player)) {
			globalPlayerNameColors.remove(player);
			boards.values().forEach(NovaScoreboardPlayerBoard::forceTeamColorUpdate);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		initPlayer(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		if (boards.containsKey(uuid)) {
			NovaScoreboardPlayerBoard board = boards.get(uuid);
			board.dispose();
			boards.remove(uuid);
		}

		globalPlayerNameColors.remove(player);
		boards.values().forEach(board -> board.removePlayerNameColor(player));
	}

	public static class Utils {
		public static NamedTextColor ChatColorToNamedTextColor(ChatColor color) {
			if (color == ChatColor.AQUA) {
				return NamedTextColor.AQUA;
			} else if (color == ChatColor.BLACK) {
				return NamedTextColor.BLACK;
			} else if (color == ChatColor.BLUE) {
				return NamedTextColor.BLUE;
			} else if (color == ChatColor.DARK_AQUA) {
				return NamedTextColor.DARK_AQUA;
			} else if (color == ChatColor.DARK_BLUE) {
				return NamedTextColor.DARK_BLUE;
			} else if (color == ChatColor.DARK_GRAY) {
				return NamedTextColor.DARK_GRAY;
			} else if (color == ChatColor.DARK_GREEN) {
				return NamedTextColor.DARK_GREEN;
			} else if (color == ChatColor.DARK_PURPLE) {
				return NamedTextColor.DARK_PURPLE;
			} else if (color == ChatColor.DARK_RED) {
				return NamedTextColor.DARK_RED;
			} else if (color == ChatColor.GOLD) {
				return NamedTextColor.GOLD;
			} else if (color == ChatColor.GRAY) {
				return NamedTextColor.GRAY;
			} else if (color == ChatColor.GREEN) {
				return NamedTextColor.GREEN;
			} else if (color == ChatColor.LIGHT_PURPLE) {
				return NamedTextColor.LIGHT_PURPLE;
			} else if (color == ChatColor.RED) {
				return NamedTextColor.RED;
			} else if (color == ChatColor.WHITE) {
				return NamedTextColor.WHITE;
			} else if (color == ChatColor.YELLOW) {
				return NamedTextColor.YELLOW;
			} else {
				Log.warn("NovaScoreboardManager", "Invalid color selected: " + color.name());
				return NamedTextColor.WHITE;
			}
		}
	}

	public void clear() {
		globalLines.clear();
		this.clearAllPlayerLines();
	}

	public void clearAllPlayerLines() {
		boards.values().forEach(NovaScoreboardPlayerBoard::clearAllPlayerlLines);
	}

	public void clearAllPlayerLines(int line) {
		boards.values().forEach(b -> b.clearPlayerLine(line));
	}
}