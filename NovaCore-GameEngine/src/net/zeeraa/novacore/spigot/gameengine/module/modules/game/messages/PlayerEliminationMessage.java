package net.zeeraa.novacore.spigot.gameengine.module.modules.game.messages;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.elimination.PlayerEliminationReason;

/**
 * Used to create custom player elimination messages
 * @author Zeeraa
 */
public interface PlayerEliminationMessage {
	/**
	 * Show the player elimination message
	 * 
	 * @param player The player that was eliminated
	 * @param killer The entity that killed the player. this will always be
	 *               <code>null</code> unless the {@link PlayerEliminationReason} is
	 *               {@link PlayerEliminationReason#KILLED}
	 * @param reason The {@link PlayerEliminationReason}
	 * @param placement The placement of the player
	 */
	void showPlayerEliminatedMessage(OfflinePlayer player, Entity killer, PlayerEliminationReason reason, int placement);
}