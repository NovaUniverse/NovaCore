package net.zeeraa.novacore.spigot.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;

public class ChatColorRGBMapper {
	public static RGBColorData chatColorToRGBColorData(ChatColor color) {
		return ChatColorRGBMapper.colorNameToRGBColorData(color.name());
	}

	public static RGBColorData chatColorToRGBColorData(net.md_5.bungee.api.ChatColor color) {
		return ChatColorRGBMapper.chatColorToRGBColorData(color, Color.WHITE);
	}

	public static RGBColorData chatColorToRGBColorData(net.md_5.bungee.api.ChatColor color, Color fallback) {
		if (!NovaCore.getInstance().isNoNMSMode()) {
			return new RGBColorData(VersionIndependentUtils.get().bungeecordChatColorToJavaColor(color));
		}

		return ChatColorRGBMapper.colorNameToRGBColorData(color.getName(), fallback);
	}

	public static RGBColorData colorNameToRGBColorData(String colorName) {
		return ChatColorRGBMapper.colorNameToRGBColorData(colorName, Color.WHITE);
	}

	public static RGBColorData colorNameToRGBColorData(String colorName, Color fallback) {
		switch (colorName.toLowerCase()) {
		case "black":
			return new RGBColorData(0, 0, 0);

		case "dark_blue":
			return new RGBColorData(0, 0, 170);

		case "dark_green":
			return new RGBColorData(0, 170, 0);

		case "dark_aqua":
			return new RGBColorData(0, 170, 170);

		case "dark_red":
			return new RGBColorData(170, 0, 0);

		case "dark_purple":
			return new RGBColorData(170, 0, 170);

		case "gold":
			return new RGBColorData(255, 170, 0);

		case "gray":
			return new RGBColorData(170, 170, 170);

		case "dark_gray":
			return new RGBColorData(85, 85, 85);

		case "blue":
			return new RGBColorData(85, 85, 255);

		case "green":
			return new RGBColorData(85, 255, 85);

		case "aqua":
			return new RGBColorData(85, 255, 255);

		case "red":
			return new RGBColorData(255, 85, 85);

		case "light_purple":
			return new RGBColorData(255, 85, 255);

		case "yellow":
			return new RGBColorData(255, 255, 85);

		case "white":
			return new RGBColorData(255, 255, 255);

		default:
			return new RGBColorData(fallback.getRed(), fallback.getGreen(), fallback.getBlue());
		}
	}
}