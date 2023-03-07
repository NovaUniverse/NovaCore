package net.zeeraa.novacore.spigot.module;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.module.event.ModuleDisabledEvent;
import net.zeeraa.novacore.spigot.module.event.ModuleEnableEvent;

/**
 * Represents a module that can be loaded, enabled and disabled.
 * <p>
 * Important information
 * <ul>
 * <li>Module names can't contain spaces</li>
 * <li>Modules need to be loaded with
 * {@link ModuleManager#loadModule(Plugin, Class)}</li>
 * <li>All modules will be disabled in {@link NovaCore#onDisable()}</li>
 * <li>All modules added to {@link NovaModule#addDependency(Class)} will be
 * enabled before this module is enabled</li>
 * <li>The dependencies can in no way have the module as a dependency or the
 * enable function will cause a {@link StackOverflowError}</li>
 * </ul>
 * <p>
 * 
 * @since 1.0
 * @author Zeeraa
 */
public abstract class NovaModule {
	/**
	 * This will be <code>true</code> if the module is enabled
	 * <p>
	 * Do dot change this in the code of your module!
	 * 
	 * @since 1.0
	 */
	private boolean enabled = false;
	private boolean hasBeenEnabledBefore = false;
	protected ModuleEnableFailureReason enableFailureReason = null;
	private String name;
	private Plugin plugin = null;

	private List<Class<? extends NovaModule>> dependencies = new ArrayList<>();

	private String missingPluginName;

	public NovaModule(String name) {
		this.name = name;
	}

	/**
	 * Get the module display name. Module names should be kept to letters, numbers,
	 * underscores and dots. Names are not allowed to have spaces
	 * <p>
	 * As of version 2.0.0 you can no longer override this to set the name. To set
	 * the name use the constructor instead
	 * 
	 * @return name
	 * @since 1.0
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Add a module to use as a dependency. All the dependencies will be enabled
	 * before this module is enabled. Make sure 2 modules dont depend on each other
	 * since that might cause a {@link StackOverflowError}
	 * 
	 * @param dependency The module class to depend on
	 * @since 1.0
	 */
	protected void addDependency(Class<? extends NovaModule> dependency) {
		dependencies.add(dependency);
	}

	/**
	 * Get a {@link List} containing all dependencies for this module
	 * 
	 * @return List with dependencies
	 * @since 1.0
	 */
	public List<Class<? extends NovaModule>> getDependencies() {
		return dependencies;
	}

	/**
	 * Called when the module is loaded by {@link ModuleManager}
	 * 
	 * @since 1.0
	 */
	public void onLoad() {
	}

	/**
	 * Called when the module is enabling. this is called before registering events
	 * 
	 * @throws Exception Thrown if something goes wrong while loading the module
	 * @since 1.0
	 */
	public void onEnable() throws Exception {
	}

	/**
	 * Called when the module is disabling. this is called after disabling events
	 * 
	 * @throws Exception Thrown if something goes wrong while loading the module
	 * @since 1.0
	 */
	public void onDisable() throws Exception {
	}

	/**
	 * Called when the module is being reloaded. This gets called by
	 * {@link NovaModule#reload()} before the module is disabled using
	 * {@link NovaModule#disable()}
	 * 
	 * @since 2.0.0
	 */
	public void onReload() {
	}

	/**
	 * Check why the module failed to enable. This value gets reset to
	 * <code>null</code> if the module is successfully enabled
	 * 
	 * @return {@link ModuleEnableFailureReason} on fail, <code>null</code> on
	 *         success
	 * @since 1.0
	 */
	public ModuleEnableFailureReason getEnableFailureReason() {
		return enableFailureReason;
	}

	/**
	 * Enable the module and register events. If this fails
	 * {@link NovaModule#getEnableFailureReason()} can be used to get the reason for
	 * the failure
	 * 
	 * @return <code>true</code> if successful, <code>false</code> if
	 *         {@link NovaModule#onEnable()} failed
	 * @since 1.0
	 */
	public final boolean enable() {
		if (enabled) {
			enableFailureReason = ModuleEnableFailureReason.ALREADY_ENABLED;
			return false;
		}

		missingPluginName = null;

		Log.info("Enabling module " + getName());

		if (dependencies != null) {
			Log.debug("Module:" + getName(), getName() + " has " + dependencies.size() + " dependencies");
			for (Class<? extends NovaModule> clazz : dependencies) {
				if (!ModuleManager.moduleExists(clazz)) {
					Log.error("Module:" + getName(), "Failed to load module " + getName() + ". Missing dependency" + clazz.getName());
					enableFailureReason = ModuleEnableFailureReason.MISSING_DEPENDENCY;
					return false;
				}

				if (!ModuleManager.isEnabled(clazz)) {
					if (!ModuleManager.enable(clazz)) {
						Log.error("Module:" + getName(), "Failed to load module " + getName() + ". Failed to enable dependency" + clazz.getName());
						enableFailureReason = ModuleEnableFailureReason.DEPENDENCY_ENABLE_FAILED;
						return false;
					}
				}
			}
		}

		boolean returnValue = true;

		try {
			onEnable();
			if (this instanceof Listener) {
				Log.debug("Module:" + getName(), "Registering listeners for module " + getName());
				Bukkit.getPluginManager().registerEvents((Listener) this, NovaCore.getInstance());
			}
			enableFailureReason = null;
			enabled = true;
			hasBeenEnabledBefore = true;
		} catch (MissingPluginDependencyException e) {
			this.enableFailureReason = ModuleEnableFailureReason.MISSING_PLUGIN_DEPENDENCY;
			returnValue = false;
			missingPluginName = e.getPluginName();
			Log.error(getName(), "Failed to enable module. Reason: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			enableFailureReason = ModuleEnableFailureReason.EXCEPTION;
			returnValue = false;
		}

		ModuleEnableEvent event = new ModuleEnableEvent(this, returnValue, enableFailureReason);
		Bukkit.getServer().getPluginManager().callEvent(event);

		return returnValue;
	}

	/**
	 * Disable the module and unregister events
	 *
	 * @return <code>false</code> if an {@link Exception} was thrown by
	 *         {@link NovaModule#onDisable()} or if the module was already disabled
	 * @since 1.0
	 */
	public final boolean disable() {
		if (!enabled) {
			return false;
		}

		Log.info("Disabling module " + this.getName());

		enableFailureReason = null;
		boolean returnValue;
		if (this instanceof Listener) {
			Log.debug("Unregistering listeners for module " + getName());
			HandlerList.unregisterAll((Listener) this);
		}

		try {
			this.onDisable();
			returnValue = true;
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
		}
		enabled = false;

		ModuleDisabledEvent event = new ModuleDisabledEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(event);

		return returnValue;
	}

	/**
	 * Disable and enable this module
	 * 
	 * @return <code>true</code> on success
	 */
	public final boolean reload() {
		if (!enabled) {
			Log.warn("Module", "Call to reload was made on non enabled module");
			return false;
		}
		this.onReload();
		Log.info("Reloading module " + this.getName());
		this.disable();
		return this.enable();
	}

	/**
	 * Get the class name of the module. used to identify the module in
	 * {@link ModuleManager}
	 * 
	 * @return Class name of the module
	 * @since 1.0
	 */
	public final String getClassName() {
		return getClass().getName();
	}

	/**
	 * Check if the module has been enabled
	 * 
	 * @return <code>true</code> if the module has been enabled
	 * @since 1.0
	 */
	public final boolean isEnabled() {
		return enabled;
	}

	/**
	 * Check if the module has ever been enabled
	 * <p>
	 * This will be true even if the module has been enabled and disabled again
	 * 
	 * @return <code>true</code> if the module has ever been enabled
	 * @since 1.0
	 */
	public final boolean hasBeenEnabledBefore() {
		return hasBeenEnabledBefore;
	}

	/**
	 * Get the name of the missing plugin that caused the module to not be enabled.
	 * This will be <code>null</code> unless the module failed with
	 * {@link ModuleEnableFailureReason#MISSING_PLUGIN_DEPENDENCY}
	 * 
	 * @return The name of the missing plugin
	 * @since 1.1
	 */
	public final String getMissingPluginName() {
		return missingPluginName;
	}

	/**
	 * Set the {@link Plugin} that owns this module. This will only set the plugin
	 * first time this is called and attempting to set the plugin again will cause a
	 * warning
	 * 
	 * @param owner {@link Plugin} that owns this module
	 */
	public final void setPlugin(Plugin owner) {
		if (plugin != null) {
			Log.warn(name, "Tried to call NovaModule#setPlugin() after plugin has already been set in " + this.getClassName());
			return;
		}
		plugin = owner;
	}

	/**
	 * Get the {@link Plugin} that this module belongs to
	 * 
	 * @return {@link Plugin} that registered this module
	 */
	public final Plugin getPlugin() {
		return plugin;
	}

	/**
	 * Disable this module
	 */
	public final void disableSelf() {
		ModuleManager.disable(this.getClass());
	}
}