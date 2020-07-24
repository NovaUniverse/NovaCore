package xyz.zeeraa.ezcore.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public class HelpSubCommand extends EZSubCommand {

	public HelpSubCommand() {
		super("help");
		setDescription("Show help");
	}

	public HelpSubCommand(String permission, String permissionDescription, PermissionDefault permissionDefaultValue) {
		super("help");
		setDescription("Show help");
		setPermission(permission);
		setPermissionDescription(permissionDescription);
		setPermissionDefaultValue(permissionDefaultValue);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String result = "";

		result += getCommandHelpText(this.getParentCommand()) + "\n";
		for (EZSubCommand command : this.getParentCommand().getSubCommands()) {
			result += getCommandHelpText(command) + "\n";
		}

		sender.sendMessage(result);

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return new ArrayList<>();
	}

	private String getCommandHelpText(EZCommandBase command) {
		return ChatColor.GOLD + getFullCommandLable(command) + ChatColor.AQUA + " " + command.getDescription();
	}

	private String getFullCommandLable(EZCommandBase command) {
		if (command.hasParentCommand()) {
			return getFullCommandLable(command.getParentCommand()) + " " + command.getName();
		}

		return "/" + command.getName();
	}
}