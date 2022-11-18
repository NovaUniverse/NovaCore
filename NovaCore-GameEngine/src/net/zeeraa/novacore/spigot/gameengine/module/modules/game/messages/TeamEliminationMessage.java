package net.zeeraa.novacore.spigot.gameengine.module.modules.game.messages;

import net.zeeraa.novacore.spigot.teams.Team;

/**
 * Used to create custom team elimination messages
 * 
 * @author Zeeraa
 */
public interface TeamEliminationMessage {
	/**
	 * Show the team elimination message
	 * 
	 * @param team      The {@link Team} that was eliminated
	 * @param placement The placement of the player
	 */
	void showTeamEliminatedMessage(Team team, int placement);
}