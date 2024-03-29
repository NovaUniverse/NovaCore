package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

public class ItemUtils {
    /**
     * Remove one item from the players main hand
     * <p>
     * This is tha same as {@link InventoryUtils#removeOneFromHand(Player)}
     *
     * @param player The player to remove the item from
     * @return The new amount in hand
     */
    public static int removeOneFromHand(@Nonnull Player player) {
        return InventoryUtils.removeOneFromHand(player);
    }
    /**
     * Remove one item from the players offhand
     * <p>
     * This is tha same as {@link InventoryUtils#removeOneFromOffHand(Player)}
     *
     * @param player The player to remove the item from
     * @return The new amount in offhand
     */
    public static int removeOneFromOffHand(@Nonnull Player player) {
        return InventoryUtils.removeOneFromOffHand(player);
    }
}