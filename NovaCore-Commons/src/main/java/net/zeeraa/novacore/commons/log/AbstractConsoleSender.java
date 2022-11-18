package net.zeeraa.novacore.commons.log;

/**
 * Represents the console sender used for sending messages to the console
 * 
 * @author Zeeraa
 */
public interface AbstractConsoleSender {
	/**
	 * Sends this sender a message
	 *
	 * @param message Message to be displayed
	 */
	void sendMessage(String message);

	/**
	 * Sends this sender multiple messages
	 *
	 * @param message An array of messages to be displayed
	 */
	void sendMessage(String[] message);
}