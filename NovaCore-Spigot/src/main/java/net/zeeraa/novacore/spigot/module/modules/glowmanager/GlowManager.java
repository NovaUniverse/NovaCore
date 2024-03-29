package net.zeeraa.novacore.spigot.module.modules.glowmanager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.NovaCoreGameVersion;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.utils.exception.UnsupportedVersionException;

public class GlowManager extends NovaModule {
	private GlowingEntities glowingEntities;

	public GlowManager() {
		super("NovaCore.GlowManager");

		glowingEntities = null;
	}

	@Override
	public void onEnable() throws Exception {
		if (VersionIndependentUtils.get().getNovaCoreGameVersion().isBefore(NovaCoreGameVersion.V_1_18)) {
			throw new UnsupportedVersionException("GlowManager requires version 1.18 or newer");
		}

		glowingEntities = new GlowingEntities(getPlugin());
	}

	@Override
	public void onDisable() throws Exception {
		if (glowingEntities != null) {
			HandlerList.unregisterAll(glowingEntities);
			glowingEntities = null;
		}
	}

	/**
	 * Make the {@link Entity} passed as a parameter glow with its default team
	 * color.
	 * 
	 * @param entity   entity to make glow
	 * @param receiver player which will see the entity glowing
	 * @throws ReflectiveOperationException can be thrown if the version is not
	 *                                      supported
	 */
	public void setGlowing(Entity entity, Player receiver) throws ReflectiveOperationException {
		if (!isEnabled()) {
			return;
		}
		glowingEntities.setGlowing(entity, receiver);
	}

	/**
	 * Make the {@link Entity} passed as a parameter glow with the specified color.
	 * 
	 * @param entity   entity to make glow
	 * @param receiver player which will see the entity glowing
	 * @param color    color of the glowing effect
	 * @throws ReflectiveOperationException can be thrown if the version is not
	 *                                      supported
	 */
	public void setGlowing(Entity entity, Player receiver, ChatColor color) throws ReflectiveOperationException {
		if (!isEnabled()) {
			return;
		}
		glowingEntities.setGlowing(entity, receiver, color);
	}

	/**
	 * Make the entity with specified entity ID glow with its default team color.
	 * 
	 * @param entityID entity id of the entity to make glow
	 * @param teamID   internal string used to add the entity to a team
	 * @param receiver player which will see the entity glowing
	 * @throws ReflectiveOperationException can be thrown if the version is not
	 *                                      supported
	 */
	public void setGlowing(int entityID, String teamID, Player receiver) throws ReflectiveOperationException {
		if (!isEnabled()) {
			return;
		}
		glowingEntities.setGlowing(entityID, teamID, receiver);
	}

	/**
	 * Make the entity with specified entity ID glow with the specified color.
	 * 
	 * @param entityID entity id of the entity to make glow
	 * @param teamID   internal string used to add the entity to a team
	 * @param receiver player which will see the entity glowing
	 * @param color    color of the glowing effect
	 * @throws ReflectiveOperationException can be thrown if the version is not
	 *                                      supported
	 */
	public void setGlowing(int entityID, String teamID, Player receiver, ChatColor color) throws ReflectiveOperationException {
		if (!isEnabled()) {
			return;
		}
		glowingEntities.setGlowing(entityID, teamID, receiver, color);
	}

	/**
	 * Make the entity with specified entity ID glow with the specified color, and
	 * keep some flags.
	 * 
	 * @param entityID   entity id of the entity to make glow
	 * @param teamID     internal string used to add the entity to a team
	 * @param receiver   player which will see the entity glowing
	 * @param color      color of the glowing effect
	 * @param otherFlags internal flags that must be kept (on fire, crouching...).
	 *                   See <a href=
	 *                   "https://wiki.vg/Entity_metadata#Entity">wiki.vg</a> for
	 *                   more informations.
	 * @throws ReflectiveOperationException can be thrown if the version is not
	 *                                      supported
	 */
	public void setGlowing(int entityID, String teamID, Player receiver, ChatColor color, byte otherFlags) throws ReflectiveOperationException {
		if (!isEnabled()) {
			return;
		}
		glowingEntities.setGlowing(entityID, teamID, receiver, color, otherFlags);
	}

	/**
	 * Make the {@link Entity} passed as a parameter loose its custom glowing
	 * effect.
	 * <p>
	 * This has <b>no effect</b> on glowing status given by another plugin or
	 * vanilla behavior.
	 * 
	 * @param entity   entity to remove glowing effect from
	 * @param receiver player which will no longer see the glowing effect
	 * @throws ReflectiveOperationException can be thrown if the version is not
	 *                                      supported
	 */
	public void unsetGlowing(Entity entity, Player receiver) throws ReflectiveOperationException {
		if (!isEnabled()) {
			return;
		}
		glowingEntities.unsetGlowing(entity, receiver);
	}

	/**
	 * Make the entity with specified entity ID passed as a parameter loose its
	 * custom glowing effect.
	 * <p>
	 * This has <b>no effect</b> on glowing status given by another plugin or
	 * vanilla behavior.
	 * 
	 * @param entityID entity id of the entity to remove glowing effect from
	 * @param receiver player which will no longer see the glowing effect
	 * @throws ReflectiveOperationException can be thrown if the version is not
	 *                                      supported
	 */
	public void unsetGlowing(int entityID, Player receiver) throws ReflectiveOperationException {
		if (!isEnabled()) {
			return;
		}
		glowingEntities.unsetGlowing(entityID, receiver);
	}
}