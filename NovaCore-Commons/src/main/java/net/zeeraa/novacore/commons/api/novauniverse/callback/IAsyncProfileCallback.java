package net.zeeraa.novacore.commons.api.novauniverse.callback;

import net.zeeraa.novacore.commons.api.novauniverse.data.MojangPlayerProfile;

public interface IAsyncProfileCallback {
	/**
	 * Called when the request has finished.
	 * <p>
	 * profile and exception can be null at the same time and that means that the
	 * player was not found
	 * 
	 * @param profile   The {@link MojangPlayerProfile} or <code>null</code> if not
	 *                  found or on error
	 * @param exception The {@link Exception} thrown or null on success or not found
	 */
	void onResult(MojangPlayerProfile profile, Exception exception);
}