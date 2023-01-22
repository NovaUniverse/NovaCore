package net.zeeraa.novacore.commons.platformindependent;

import java.util.UUID;

/**
 * API to send players between servers
 * 
 * @author Zeeraa
 */
public interface PlatformIndependentBungeecordAPI {
	/**
	 * Try to send a player by their {@link UUID} to a server by its name
	 * 
	 * @param player The {@link UUID} of the player to send
	 * @param server The name of the server to send the player to
	 * @return <code>true</code> if an attempt was made
	 */
	boolean sendPlayerToServer(UUID player, String server);
}