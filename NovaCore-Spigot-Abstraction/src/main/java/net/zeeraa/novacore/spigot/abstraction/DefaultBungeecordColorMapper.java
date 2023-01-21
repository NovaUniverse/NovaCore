package net.zeeraa.novacore.spigot.abstraction;

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;

public class DefaultBungeecordColorMapper {
	public static Color getColorOfChatColor(ChatColor color) {
		switch (color.name().toLowerCase()) {
		case "black":
			return new Color(0, 0, 0);

		case "dark_blue":
			return new Color(0, 0, 170);

		case "dark_green":
			return new Color(0, 170, 0);

		case "dark_aqua":
			return new Color(0, 170, 170);

		case "dark_red":
			return new Color(170, 0, 0);

		case "dark_purple":
			return new Color(170, 0, 170);

		case "gold":
			return new Color(255, 170, 0);

		case "gray":
			return new Color(170, 170, 170);

		case "dark_gray":
			return new Color(85, 85, 85);

		case "blue":
			return new Color(85, 85, 255);

		case "green":
			return new Color(85, 255, 85);

		case "aqua":
			return new Color(85, 255, 255);

		case "red":
			return new Color(255, 85, 85);

		case "light_purple":
			return new Color(255, 85, 255);

		case "yellow":
			return new Color(255, 255, 85);

		case "white":
			return new Color(255, 255, 255);

		default:
			return new Color(255, 255, 255);
		}
	}

	public static org.bukkit.ChatColor getBukkitChatColorOfBungeeChatColor(ChatColor color) {

		return org.bukkit.ChatColor.valueOf(color.name());
	}
}