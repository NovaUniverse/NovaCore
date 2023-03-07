package net.zeeraa.novacore.spigot.gameengine.command.commands.game.debug;

import java.io.IOException;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.commons.async.AsyncManager;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.MapGame;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;

public class GameDebugCommand extends NovaSubCommand {
	public GameDebugCommand() {
		super("debug");
		setAllowedSenders(AllowedSenders.ALL);
		setDescription("Show debug info");
		setPermission("novacore.command.game.debug");
		setPermissionDescription("Debug command for games");
		setPermissionDefaultValue(PermissionDefault.OP);

		setEmptyTabMode(true);
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String message = "";

		message += ChatColor.AQUA + "-=-=-= BEGIN DEBUG =-=-=-\n";
		message += ChatColor.GOLD + "Server version: " + ChatColor.AQUA + Bukkit.getServer().getVersion() + "\n";

		if (GameManager.getInstance().isEnabled()) {
			message += ChatColor.GREEN + "GameManager is enabled\n";
			if (GameManager.getInstance().hasRegisteredGame()) {
				message += ChatColor.GOLD + "Registered games: " + ChatColor.AQUA;
				for (Game g : GameManager.getInstance().getRegisteredGames()) {
					message += (g.getDisplayName() + "(" + g.getName() + "), ");
				}
				message = message.substring(0,message.length() - 2);
				message += "\n";
			}


			if (GameManager.getInstance().hasActiveGame()) {
				message += ChatColor.GOLD + "Loaded game name: " + ChatColor.AQUA + GameManager.getInstance().getActiveGame().getName() + "\n";
				message += ChatColor.GOLD + "Display name: " + ChatColor.AQUA + GameManager.getInstance().getActiveGame().getDisplayName() + "\n";
				message += ChatColor.GOLD + "Class: " + ChatColor.AQUA + GameManager.getInstance().getActiveGame().getClass().getName() + "\n";
				boolean started = GameManager.getInstance().getActiveGame().hasStarted();
				boolean ended = GameManager.getInstance().getActiveGame().hasEnded();
				message += ChatColor.GOLD + "Started: " + (started ? ChatColor.GREEN : ChatColor.RED) + "" + started + ChatColor.GOLD + " Ended: " + (ended ? ChatColor.GREEN : ChatColor.RED) + "" + ended + ChatColor.GOLD + "\n";

				message += ChatColor.GOLD + "Player count: " + ChatColor.AQUA + GameManager.getInstance().getActiveGame().getPlayers().size() + "\n";

				message += ChatColor.GOLD + "Auto end: " + ChatColor.AQUA + GameManager.getInstance().getActiveGame().autoEndGame() + "\n";
				message += ChatColor.GOLD + "Can start: " + ChatColor.AQUA + GameManager.getInstance().getActiveGame().canStart() + "\n";

				if (GameManager.getInstance().getActiveGame() instanceof MapGame) {
					MapGame mapGame = (MapGame) GameManager.getInstance().getActiveGame();

					message += ChatColor.GOLD + "-= MAP DATA =-\n";

					message += ChatColor.GOLD + "Has active map: " + ChatColor.AQUA + (mapGame.hasActiveMap() ? (ChatColor.GREEN + "yes") : (ChatColor.RED + "no")) + "\n";

					if (mapGame.hasActiveMap()) {
						message += ChatColor.GOLD + "Map name: " + ChatColor.AQUA + mapGame.getActiveMap().getAbstractMapData().getMapName() + "\n";
						message += ChatColor.GOLD + "Display name: " + ChatColor.AQUA + mapGame.getActiveMap().getAbstractMapData().getDisplayName() + "\n";

						String modules = "";

						for (MapModule module : mapGame.getActiveMap().getMapData().getMapModules()) {
							modules += module.getName() + " ";
						}

						message += ChatColor.GOLD + "Modules (" + mapGame.getActiveMap().getMapData().getMapModules().size() + "): " + modules + "\n";
						message += ChatColor.GOLD + "-= MODULE DATA =-\n";
						for (MapModule module : mapGame.getActiveMap().getMapData().getMapModules()) {
							String moduleStatus = module.getSummaryString();
							if (moduleStatus != null) {
								message += module.getName() + ": " + moduleStatus + "\n";
							}
						}
					}
				}
			} else {
				message += ChatColor.RED + "No game loaded\n";
			}
		} else {
			message += ChatColor.RED + "GameManager is disabled\n";
		}

		message += ChatColor.AQUA + "-=-=-= END DEBUG =-=-=-\n";

		for (String line : message.split("\n")) {
			sender.sendMessage(line);
		}

		sender.sendMessage(ChatColor.AQUA + "Uploading report to hastebin....");
		final String finalHastebinMessage = ChatColor.stripColor(message);
		AsyncManager.runAsync(() -> {
			try {
				String url = NovaCore.getInstance().getHastebinInstance().post(finalHastebinMessage, true);
				sender.sendMessage(ChatColor.GOLD + "View the report online here: " + ChatColor.AQUA + url);
			} catch (IOException e) {
				Log.error("GameDebug", "Failed to upload message to hastebin. " + e.getClass().getName() + " " + e.getMessage());
				e.printStackTrace();
				sender.sendMessage(ChatColor.DARK_RED + "Failed to upload data to hastebin. " + e.getClass().getName() + " " + e.getMessage());
			}
		});

		return true;
	}
}