package net.zeeraa.novacore.spigot.module.modules.gui.callbacks;

import org.bukkit.event.inventory.InventoryClickEvent;
import net.zeeraa.novacore.spigot.module.modules.gui.GUIAction;

/**
 * Called when a GUI is clicked
 * 
 * @author Zeeraa
 * @since 2.0.0
 */
public interface GUIClickCallbackWithEvent {
	/**
	 * Called when someone clicks a GUI. The return value is used to either allow or
	 * deny the interaction
	 * 
	 * @param event The {@link InventoryClickEvent}
	 * @return return {@link GUIAction#NONE} or <code>null</code> to use the default
	 *         outcome and deny the interaction. use the other values to either
	 *         allow or deny the interaction
	 */
	GUIAction onClick(InventoryClickEvent event);
}