package net.brunogamer.novacore.spigot.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

public class ColorUtils {
    public static DyeColor getDyeColorByColor(Color color) {
        int rgb = color.asRGB();
        if (rgb == Color.WHITE.asRGB()) {
            return DyeColor.WHITE;
        } else if (rgb == Color.SILVER.asRGB()) {
            return DyeColor.SILVER;
        } else if (rgb == Color.GRAY.asRGB()) {
            return DyeColor.GRAY;
        } else if (rgb == Color.BLACK.asRGB()) {
            return DyeColor.BLACK;
        } else if (rgb == Color.RED.asRGB() || rgb ==  Color.MAROON.asRGB()) {
            return DyeColor.RED;
        } else if (rgb == Color.YELLOW.asRGB() || rgb ==  Color.OLIVE.asRGB()) {
            return DyeColor.YELLOW;
        } else if (rgb == Color.LIME.asRGB()) {
            return DyeColor.LIME;
        } else if (rgb == Color.GREEN.asRGB()) {
            return DyeColor.GREEN;
        } else if (rgb == Color.AQUA.asRGB()) {
            return DyeColor.LIGHT_BLUE;
        } else if (rgb == Color.TEAL.asRGB()) {
            return DyeColor.CYAN;
        } else if (rgb == Color.BLUE.asRGB() || rgb ==  Color.NAVY.asRGB()) {
            return DyeColor.BLUE;
        } else if (rgb == Color.FUCHSIA.asRGB()) {
            return DyeColor.PINK;
        } else if (rgb == Color.PURPLE.asRGB()) {
            return DyeColor.PURPLE;
        } else if (rgb == Color.ORANGE.asRGB()) {
            return DyeColor.ORANGE;
        } else {
            return DyeColor.WHITE;
        }
    }
    public static DyeColor getDyeColorByChatColor(ChatColor color) {
        if (color == ChatColor.BLACK) {
            return DyeColor.BLACK;
        } else if (color == ChatColor.DARK_BLUE || color == ChatColor.BLUE) {
            return DyeColor.BLUE;
        } else if (color == ChatColor.DARK_GREEN) {
            return DyeColor.GREEN;
        } else if (color == ChatColor.DARK_AQUA) {
            return DyeColor.CYAN;
        } else if (color == ChatColor.DARK_RED || color == ChatColor.RED) {
            return DyeColor.RED;
        } else if (color == ChatColor.DARK_PURPLE) {
            return DyeColor.PURPLE;
        } else if (color == ChatColor.GOLD) {
            return DyeColor.ORANGE;
        } else if (color == ChatColor.GRAY) {
            return DyeColor.SILVER;
        } else if (color == ChatColor.DARK_GRAY) {
            return DyeColor.GRAY;
        } else if (color == ChatColor.GREEN) {
            return DyeColor.LIME;
        } else if (color == ChatColor.AQUA) {
            return DyeColor.LIGHT_BLUE;
        } else if (color == ChatColor.LIGHT_PURPLE) {
            return DyeColor.PINK;
        } else if (color == ChatColor.YELLOW) {
            return DyeColor.YELLOW;
        } else if (color == ChatColor.WHITE) {
            return DyeColor.WHITE;
        } else {
            return DyeColor.WHITE;
        }
    }
    public static Color getColorByDyeColor(DyeColor color) {
        switch (color) {
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.NAVY;
            case GREEN:
                return Color.GREEN;
            case CYAN:
                return Color.TEAL;
            case RED:
                return Color.RED;
            case PURPLE:
                return Color.PURPLE;
            case ORANGE:
                return Color.ORANGE;
            case SILVER:
                return Color.SILVER;
            case GRAY:
                return Color.GRAY;
            case LIME:
                return Color.LIME;
            case LIGHT_BLUE:
                return Color.AQUA;
            case MAGENTA:
            case PINK:
                return Color.FUCHSIA;
            case YELLOW:
                return Color.YELLOW;
            case WHITE:
                return Color.WHITE;
            default:
                return Color.WHITE;
        }
    }
    public static Color getColorByChatColor(ChatColor color) {
        if (color == ChatColor.BLACK) {
            return Color.BLACK;
        } else if (color == ChatColor.DARK_BLUE) {
            return Color.NAVY;
        } else if (color == ChatColor.BLUE) {
            return Color.BLUE;
        } else if (color == ChatColor.DARK_GREEN) {
            return Color.GREEN;
        } else if (color == ChatColor.DARK_AQUA) {
            return Color.TEAL;
        } else if (color == ChatColor.DARK_RED) {
            return Color.MAROON;
        } else if (color == ChatColor.RED) {
            return Color.RED;
        } else if (color == ChatColor.DARK_PURPLE) {
            return Color.PURPLE;
        } else if (color == ChatColor.GOLD) {
            return Color.ORANGE;
        } else if (color == ChatColor.GRAY) {
            return Color.SILVER;
        } else if (color == ChatColor.DARK_GRAY) {
            return Color.GRAY;
        } else if (color == ChatColor.GREEN) {
            return Color.LIME;
        } else if (color == ChatColor.AQUA) {
            return Color.AQUA;
        } else if (color == ChatColor.LIGHT_PURPLE) {
            return Color.FUCHSIA;
        } else if (color == ChatColor.YELLOW) {
            return Color.YELLOW;
        } else if (color == ChatColor.WHITE) {
            return Color.WHITE;
        } else {
            return Color.WHITE;
        }
    }
    
    public static java.awt.Color bukkitColorToAWT(Color color) {
    	return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public static Color[] gradientFromColors(RGB color1, RGB color2, int lenght) throws Exception {
        if (lenght <= 1) {
            throw new Exception("can not be 1 or lower");
        }
        Color[] colors = new Color[lenght];

        int redDif = color2.getRed() - color1.getRed();
        int blueDif = color2.getBlue() - color1.getBlue();
        int greenDif = color2.getGreen() - color1.getGreen();
        double parcelledRed = (double) redDif/(lenght - 1);
        double parcelledBlue = (double) blueDif/(lenght - 1);
        double parcelledGreen = (double) greenDif/(lenght - 1);
        for (int i = 0; i < lenght; i++) {
            int currentRed = color1.getRed() +  (int) (Math.round(parcelledRed * i));
            int currentBlue = color1.getBlue() + (int) (Math.round(parcelledBlue * i));
            int currentGreen = color1.getGreen() + (int) (Math.round(parcelledGreen * i));
            colors[i] = Color.fromRGB(currentRed, currentGreen, currentBlue);
        }
        return colors;
    }
}
