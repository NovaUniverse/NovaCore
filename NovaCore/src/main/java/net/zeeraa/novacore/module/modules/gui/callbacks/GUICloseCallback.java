package net.zeeraa.novacore.module.modules.gui.callbacks;

import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Called when a GUI is closed
 * 
 * @author Zeeraa
 */
public interface GUICloseCallback {
	/**
	 * Called when a GUI is closed
	 * 
	 * @param event the {@link InventoryCloseEvent}
	 */
	public void onClose(InventoryCloseEvent event);
}