package net.zeeraa.novacore.spigot.mapdisplay.command.subcommand;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplay;
import net.zeeraa.novacore.spigot.mapdisplay.MapDisplayManager;

public class MDSetImageSubCommand extends NovaSubCommand {
	public static int IMAGE_FETCH_TIMEOUT = 10000;
	public static String useragent = "NovaCore 2.0.0 MapImageDisplays";
	public static boolean disableWebInteractions = false;

	public MDSetImageSubCommand() {
		super("setimage");

		setPermission("novacore.command.mapdisplay.setimage");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Set the image of a map display");
		setFilterAutocomplete(true);
		setAllowedSenders(AllowedSenders.ALL);
		setUsage("/mapdisplay setimage <name> <url>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!MapDisplayManager.getInstance().isEnabled()) {
			sender.sendMessage(ChatColor.DARK_RED + "MapDisplayManager is not enabled");
			return false;
		}
		
		if(disableWebInteractions) {
			sender.sendMessage(ChatColor.RED + "The server does not allow fetching images from external urls");
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a name of a display");
			return false;
		}

		if (args.length == 1) {
			sender.sendMessage(ChatColor.RED + "Please provide a url containing an image");
			return false;
		}

		for (MapDisplay display : MapDisplayManager.getInstance().getMapDisplays()) {
			if (display.getName().equalsIgnoreCase(args[0])) {
				BufferedImage image = null;

				try {
					URL url = new URL(args[1]);
					final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(IMAGE_FETCH_TIMEOUT);
					connection.setReadTimeout(IMAGE_FETCH_TIMEOUT);
					connection.setRequestProperty("User-Agent", useragent);
					image = ImageIO.read(connection.getInputStream());
					// image = ImageIO.read(url);
					Log.trace("Image loaded from url");
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Could not read image from url. " + e.getClass().getName() + " " + e.getMessage());
					e.printStackTrace();
				}

				if (image != null) {
					try {
						display.setImage(image);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED + "Failed to set the display image. " + e.getClass().getName() + " " + e.getMessage());
						e.printStackTrace();
					}
				}

				return true;
			}
		}

		sender.sendMessage(ChatColor.RED + "Could not find map display named " + args[0]);

		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();

		MapDisplayManager.getInstance().getMapDisplays().forEach(display -> result.add(display.getName()));
		
		return result;
	}
}