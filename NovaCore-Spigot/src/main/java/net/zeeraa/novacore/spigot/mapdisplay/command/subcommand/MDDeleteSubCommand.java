package net.zeeraa.novacore.spigot.mapdisplay.command.subcommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplay;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplayManager;

public class MDDeleteSubCommand extends NovaSubCommand {

	public MDDeleteSubCommand() {
		super("delete");

		setPermission("novacore.command.mapdisplay.delete");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Delete a map display");
		setFilterAutocomplete(true);
		setAllowedSenders(AllowedSenders.ALL);
		setAliases(NovaCommand.generateAliasList("remove"));
		setUsage("/mapdisplay delete <name>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!MapDisplayManager.getInstance().isEnabled()) {
			sender.sendMessage(ChatColor.DARK_RED + "MapDisplayManager is not enabled");
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a name");
			return false;
		}

		String name = args[0];
		if (!name.contains(":")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				name = player.getWorld().getName() + ":" + name;
			}
		}

		name = name.toLowerCase();

		MapDisplay display = MapDisplayManager.getInstance().getMapDisplay(name);
		if (display != null) {
			display.delete();
			sender.sendMessage(ChatColor.GREEN + "Display removed");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Could not find map display named " + args[0] + ". If the display is in another world make sure you provide the world name like this: world:display_1");
		}
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();

		MapDisplayManager.getInstance().getMapDisplays().forEach(display -> result.add(display.getNamespace()));
		if(sender instanceof Player) {
			Player player = (Player) sender;
			World world = player.getWorld();
			MapDisplayManager.getInstance().getMapDisplaysInWorld(world).forEach(display -> result.add(display.getName()));
		}

		return result;
	}
}