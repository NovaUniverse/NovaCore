package net.zeeraa.novacore.spigot.gameengine.module.modules.game.events;

import org.bukkit.event.HandlerList;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;

/**
 * Called after {@link Game#startGame()} is called
 * 
 * @author Zeeraa
 */
public class PreGameStartEvent extends GameEvent {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	public PreGameStartEvent(Game game) {
		super(game);
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}