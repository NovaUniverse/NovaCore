package net.zeeraa.novacore.spigot.gameengine.command.commands.gamelobby;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.gamelobby.GameLobby;
import net.zeeraa.novacore.spigot.module.ModuleManager;

/**
 * A command from NovaCore
 * 
 * @author Zeeraa
 */
public class NovaCoreCommandGameLobby extends NovaCommand {
	public NovaCoreCommandGameLobby(Plugin plugin) {
		super("gamelobby", plugin);

		this.setDescription("Game lobby commands");
		this.setPermission("novacore.command.gamelobby");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the game lobby command");

		this.addHelpSubCommand();
		this.addSubCommand(new SCLeaveQueue());

		this.setEmptyTabMode(true);
		this.setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/gamelobby help" + ChatColor.GOLD + " to see all commands");
		return true;
	}
}

class SCLeaveQueue extends NovaSubCommand {
	public SCLeaveQueue() {
		super("leavequeue");

		this.setDescription("Leave the queue");
		this.setPermission("novacore.command.gamelobby.leavequeue");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the game leavequeue command");

		this.setAllowedSenders(AllowedSenders.PLAYERS);

		this.setEmptyTabMode(true);
		this.setFilterAutocomplete(true);

		this.addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (ModuleManager.isEnabled(GameManager.class)) {
			if (GameManager.getInstance().hasGame()) {
				if (!GameManager.getInstance().getActiveGame().hasStarted()) {
					if (ModuleManager.isEnabled(GameLobby.class)) {
						Player player = (Player) sender;
						GameLobby.getInstance().getWaitingPlayers().remove(player.getUniqueId());
						player.sendMessage(ChatColor.GREEN + "You will not be added to the game");
					} else {
						sender.sendMessage(ChatColor.RED + "GameLobby not enabled");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Game has already started");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "No game has been loaded");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "GameManager not enabled");
		}
		return false;
	}
}