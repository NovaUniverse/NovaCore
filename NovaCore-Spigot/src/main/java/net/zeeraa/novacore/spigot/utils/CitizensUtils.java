package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.entity.Entity;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.zeeraa.novacore.spigot.NovaCore;

public class CitizensUtils {
	/**
	 * Check if an {@link Entity} is an npc
	 * 
	 * @param entity The entity to check
	 * @return <code>true</code> if the npc is an entity
	 */
	public static boolean isNPC(@Nonnull Entity entity) {
		return entity.hasMetadata("NPC");
	}

	/**
	 * Get the instance of {@link CitizensUtils} from
	 * {@link NovaCore#getCitizensUtils()}
	 * 
	 * @return CitizensUtils instance
	 */
	public static CitizensUtils get() {
		return NovaCore.getInstance().getCitizensUtils();
	}

	/**
	 * Check if citizens utils is available
	 * 
	 * @return <code>true</code> if citizens is installed and citizens utils is
	 *         available
	 */
	public static boolean isAvailable() {
		return NovaCore.getInstance().hasCitizensUtils();
	}

	/**
	 * Set the skin of a {@link NPC} to match the skin of a player by their name
	 * 
	 * @param npc      The npc to change the skin of
	 * @param username The username of the player to get the skin from
	 */
	public void setSkin(@Nonnull NPC npc, @Nonnull String username) {
		SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
		skinTrait.setSkinName(username);
	}
}