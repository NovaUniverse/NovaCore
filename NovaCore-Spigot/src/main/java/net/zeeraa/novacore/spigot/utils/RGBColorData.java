package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.json.JSONObject;

public class RGBColorData {
	private int r;
	private int g;
	private int b;

	public RGBColorData(int r, int g, int b) {
		this.r = 0;
		this.g = 0;
		this.b = 0;

		setR(r);
		setG(g);
		setB(b);
	}

	public RGBColorData(@Nonnull org.bukkit.Color color) {
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}

	public RGBColorData(@Nonnull java.awt.Color color) {
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public void setR(int r) {
		if (r < 0) {
			throw new IllegalArgumentException("r cant be less than 0");
		}
		if (r > 255) {
			throw new IllegalArgumentException("r cant be larget than 255");
		}
		this.r = r;
	}

	public void setG(int g) {
		if (g < 0) {
			throw new IllegalArgumentException("g cant be less than 0");
		}
		if (g > 255) {
			throw new IllegalArgumentException("g cant be larget than 255");
		}
		this.g = g;
	}

	public void setB(int b) {
		if (b < 0) {
			throw new IllegalArgumentException("b cant be less than 0");
		}
		if (b > 255) {
			throw new IllegalArgumentException("b cant be larget than 255");
		}
		this.b = b;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("r", r);
		json.put("g", g);
		json.put("b", b);
		return json;
	}

	public static RGBColorData fromJSON(@Nonnull JSONObject json) {
		return new RGBColorData(json.getInt("r"), json.getInt("g"), json.getInt("b"));
	}

	public org.bukkit.Color toBukkitColor() {
		return org.bukkit.Color.fromRGB(r, g, b);
	}

	public java.awt.Color toAWTColor() {
		return new java.awt.Color(r, g, b);
	}

	public Color getColorByChatColor(@Nonnull net.md_5.bungee.api.ChatColor color) {
		if (color == net.md_5.bungee.api.ChatColor.BLACK) {
			return Color.BLACK;
		} else if (color == net.md_5.bungee.api.ChatColor.DARK_BLUE) {
			return Color.NAVY;
		} else if (color == net.md_5.bungee.api.ChatColor.DARK_GREEN) {
			return Color.GREEN;
		} else if (color == net.md_5.bungee.api.ChatColor.DARK_AQUA) {
			return Color.TEAL;
		} else if (color == net.md_5.bungee.api.ChatColor.DARK_RED) {
			return Color.MAROON;
		} else if (color == net.md_5.bungee.api.ChatColor.DARK_PURPLE) {
			return Color.PURPLE;
		} else if (color == net.md_5.bungee.api.ChatColor.GOLD) {
			return Color.ORANGE;
		} else if (color == net.md_5.bungee.api.ChatColor.GRAY) {
			return Color.SILVER;
		} else if (color == net.md_5.bungee.api.ChatColor.DARK_GRAY) {
			return Color.GRAY;
		} else if (color == net.md_5.bungee.api.ChatColor.BLUE) {
			return Color.BLUE;
		} else if (color == net.md_5.bungee.api.ChatColor.GREEN) {
			return Color.LIME;
		} else if (color == net.md_5.bungee.api.ChatColor.AQUA) {
			return Color.AQUA;
		} else if (color == net.md_5.bungee.api.ChatColor.RED) {
			return Color.RED;
		} else if (color == net.md_5.bungee.api.ChatColor.LIGHT_PURPLE) {
			return Color.FUCHSIA;
		} else if (color == net.md_5.bungee.api.ChatColor.YELLOW) {
			return Color.YELLOW;
		} else if (color == net.md_5.bungee.api.ChatColor.WHITE) {
			return Color.WHITE;
		} else {
			return Color.WHITE;
		}
	}

	public static RGBColorData fromChatColor(@Nonnull ChatColor color) {
		return ChatColorRGBMapper.chatColorToRGBColorData(color);
	}

	public static RGBColorData fromChatColor(@Nonnull net.md_5.bungee.api.ChatColor color) {
		return ChatColorRGBMapper.chatColorToRGBColorData(color);
	}

	public static RGBColorData fromBukkitColor(@Nonnull org.bukkit.Color color) {
		return new RGBColorData(color);
	}

	public static RGBColorData fromAWTColor(@Nonnull java.awt.Color color) {
		return new RGBColorData(color);
	}
}