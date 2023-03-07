package net.zeeraa.novacore.spigot.gameengine.command.commands.game.resetcountdown;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;

/**
 * A command from NovaCore
 * 
 * @author Zeeraa
 */
public class NovaCoreSubCommandResetCountdownGame extends NovaSubCommand {
	public NovaCoreSubCommandResetCountdownGame() {
		super("resetcountdown");

		this.setDescription("Reset the game countdown");
		this.setPermission("novacore.command.game.resetcountdown");
		this.setPermissionDefaultValue(PermissionDefault.OP);
		this.setPermissionDescription("Access to the reset countdown command");

		this.addHelpSubCommand();

		this.setFilterAutocomplete(true);
		this.setEmptyTabMode(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (GameManager.getInstance().isEnabled()) {
			if (GameManager.getInstance().hasActiveGame()) {
				if (!GameManager.getInstance().getActiveGame().hasStarted()) {
					if (GameManager.getInstance().getActiveGame().canStart()) {
						if (GameManager.getInstance().hasCountdown()) {
							GameManager.getInstance().getCountdown().resetTimeLeft();
							sender.sendMessage(ChatColor.GREEN + "Countdown has been reset to the starting value");
							return true;
						} else {
							sender.sendMessage(ChatColor.RED + "No countdown has been enabled");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "The game cant start right now");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Game has already been started");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "No game has been loaded");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "GameManager is not enabled");
		}
		return false;
	}
}