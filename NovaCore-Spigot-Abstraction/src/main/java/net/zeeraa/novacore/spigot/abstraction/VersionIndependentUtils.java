package net.zeeraa.novacore.spigot.abstraction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.zeeraa.novacore.spigot.abstraction.commons.AttributeInfo;
import net.zeeraa.novacore.spigot.abstraction.enums.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;

import net.zeeraa.novacore.spigot.abstraction.log.AbstractionLogger;
import net.zeeraa.novacore.spigot.abstraction.packet.PacketManager;
import org.bukkit.potion.PotionEffect;
import java.awt.Color;
import java.util.function.Consumer;

/**
 * A utility to make your plugins support multiple versions of the game without
 * using reflection. This class contains a lot of functions that changes between
 * versions
 * 
 * @author Zeeraa
 * @author Bruno
 */
public abstract class VersionIndependentUtils {
	private static VersionIndependentUtils instance;
	protected static List<String> BED_MATERIALS = new ArrayList<>();

	static {
		BED_MATERIALS.add("BED");
		BED_MATERIALS.add("BED_BLOCK");
		BED_MATERIALS.add("BLACK_BED");
		BED_MATERIALS.add("BLUE_BED");
		BED_MATERIALS.add("BROWN_BED");
		BED_MATERIALS.add("CYAN_BED");
		BED_MATERIALS.add("GRAY_BED");
		BED_MATERIALS.add("GREEN_BED");
		BED_MATERIALS.add("LIGHT_BLUE_BED");
		BED_MATERIALS.add("LIGHT_GRAY_BED");
		BED_MATERIALS.add("LIME_BED");
		BED_MATERIALS.add("MAGENTA_BED");
		BED_MATERIALS.add("ORANGE_BED");
		BED_MATERIALS.add("PINK_BED");
		BED_MATERIALS.add("PURPLE_BED");
		BED_MATERIALS.add("RED_BED");
		BED_MATERIALS.add("WHITE_BED");
		BED_MATERIALS.add("YELLOW_BED");
	}

	private VersionIndependenceLayerError lastError = VersionIndependenceLayerError.NONE;

	/**
	 * Set the last error cause used for debugging purposes
	 * 
	 * @param error The last {@link VersionIndependenceLayerError} raised
	 * @author Zeeraa
	 */
	protected final void setLastError(VersionIndependenceLayerError error) {
		this.lastError = error;
	}

	/**
	 * GEt the last error type caused in the version independent layer. This will
	 * return null if no errors was encountered or
	 * {@link VersionIndependentUtils#resetLastError()} was called
	 * 
	 * @return The {@link VersionIndependenceLayerError} of <code>null</code> if no
	 *         errors occurred
	 * @author Zeeraa
	 */
	public final VersionIndependenceLayerError getLastError() {
		return lastError;
	}

	/**
	 * Resets the last error variable
	 * 
	 * @author Zeeraa
	 */
	public final void resetLastError() {
		this.lastError = VersionIndependenceLayerError.NONE;
	}

	/**
	 * Get the {@link ChunkLoader} implementation
	 * 
	 * @return {@link ChunkLoader} to use
	 * @author Zeeraa
	 */
	public abstract ChunkLoader getChunkLoader();

	/**
	 * Get the {@link VersionIndependentUtils} instance that this server uses. Same
	 * as {@link VersionIndependentUtils#getInstance()}
	 * 
	 * @return {@link VersionIndependentUtils} instance
	 * @author Zeeraa
	 */
	public static VersionIndependentUtils get() {
		return instance;
	}

	/**
	 * Get the {@link VersionIndependentUtils} instance that this server uses. Same
	 * as {@link VersionIndependentUtils#get()}
	 * 
	 * @return {@link VersionIndependentUtils} instance
	 * @author Zeeraa
	 */
	public static VersionIndependentUtils getInstance() {
		return instance;
	}

	public static void setInstance(VersionIndependentUtils instance) {
		VersionIndependentUtils.instance = instance;
	}

	/**
	 * Get the {@link NovaCoreGameVersion} nova core is using for the server
	 * 
	 * @return The {@link NovaCoreGameVersion} for thie server
	 * @author Zeeraa
	 */
	public abstract NovaCoreGameVersion getNovaCoreGameVersion();

	/**
	 * Set a block as a player skull
	 * 
	 * @param block The {@link Block} to change the type of
	 * @author Zeeraa
	 */
	public abstract void setBlockAsPlayerSkull(Block block);

	/**
	 * Get the item from the player main hand
	 * 
	 * @param player The player to get item from
	 * @return {@link ItemStack} from the players main hand
	 * @author Zeeraa
	 */
	public abstract ItemStack getItemInMainHand(Player player);

	/**
	 * Set the item in a players main hand
	 * 
	 * @param player The player
	 * @param item   The item so place the the players hand
	 * @author Zeeraa
	 */
	public abstract void setItemInMainHand(Player player, ItemStack item);

	/**
	 * Get the item from the player off hand
	 * 
	 * @param player The player to get item from
	 * @return {@link ItemStack} or <code>null</code> if the server version does not
	 *         have a off hand
	 * @author Zeeraa
	 */
	public abstract ItemStack getItemInOffHand(Player player);

	/**
	 * Set the item in a players off hand
	 * 
	 * @param player The player
	 * @param item   The item so place the the players hand
	 * @author Zeeraa
	 */
	public abstract void setItemInOffHand(Player player, ItemStack item);

	/**
	 * Get the ping of a player
	 * 
	 * @param player The player to get the ping of
	 * @return ping
	 * @author Zeeraa
	 */
	public abstract int getPlayerPing(Player player);

	/**
	 * Attempt to damage a player with a specified reason. If this is not correctly
	 * implemented the player will be damaged using {@link Player#damage(double)}
	 * instead
	 * 
	 * @param player The player to damage
	 * @param reason The {@link PlayerDamageReason} to use
	 * @param damage The amount of damage to cause
	 * @author Zeeraa
	 */
	public abstract void damagePlayer(Player player, PlayerDamageReason reason, float damage);

	/**
	 * Get the max heath of a {@link LivingEntity}
	 * 
	 * @param livingEntity The entity to get the max health from
	 * 
	 * @return The max health of the player
	 * @author Zeeraa
	 */
	public abstract double getEntityMaxHealth(LivingEntity livingEntity);

	/**
	 * Set the max heath of a {@link LivingEntity}
	 * 
	 * @param livingEntity The entity to reset the set health of
	 * @param health       The new health value
	 * @author Zeeraa
	 */
	public abstract void setEntityMaxHealth(LivingEntity livingEntity, double health);

	/**
	 * Reset the max heath of a {@link LivingEntity}
	 * 
	 * @param livingEntity The entity to reset the max health of
	 * @author Zeeraa
	 */
	public abstract void resetEntityMaxHealth(LivingEntity livingEntity);

	/**
	 * Get the recent server TPS
	 * 
	 * @return Array with recent TPS
	 * @author Zeeraa
	 */
	public abstract double[] getRecentTps();

	/**
	 * Clones the data of 2 blocks
	 * 
	 * @param source The block to copy the data from
	 * @param target The block to set the data of
	 * @author Zeeraa
	 */
	public abstract void cloneBlockData(Block source, Block target);

	/**
	 * Send text to display in the tab list
	 * 
	 * @param player The {@link Player} that should receive the text
	 * @param header The top text
	 * @param footer The bottom text
	 * @author Zeeraa
	 */
	public abstract void sendTabList(Player player, String header, String footer);

	/**
	 * Set a block to a {@link ColoredBlockType} with a {@link DyeColor}
	 * 
	 * @param block The {@link Block} to change
	 * @param color The {@link DyeColor} to use
	 * @param type  The {@link ColoredBlockType} to set the block as
	 * @author Zeeraa
	 */
	public abstract void setColoredBlock(Block block, DyeColor color, ColoredBlockType type);

	/**
	 * Used to add a colored block as ingredient of a {@link ShapedRecipe}
	 * 
	 * @param recipe     The {@link ShapedRecipe} to modify
	 * @param ingredient The ingredient char
	 * @param type       The {@link ColoredBlockType} to use
	 * @param color      The {@link DyeColor} to use
	 * @author Zeeraa
	 */
	public abstract void setShapedRecipeIngredientAsColoredBlock(ShapedRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color);

	/**
	 * Used to add a colored block as ingredient of a {@link ShapelessRecipe}
	 * 
	 * @param recipe     The {@link ShapelessRecipe} to modify
	 * @param ingredient The ingredient char
	 * @param type       The {@link ColoredBlockType} to use
	 * @param color      The {@link DyeColor} to use
	 * @author Zeeraa
	 */
	public abstract void addShapelessRecipeIngredientAsColoredBlock(ShapelessRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color);

	/**
	 * Used to add dyes in a {@link ShapedRecipe}
	 * 
	 * @param recipe     The {@link ShapedRecipe} to modify
	 * @param ingredient The ingredient char
	 * @param color      The {@link DyeColor} to use
	 * @author Zeeraa
	 */
	public abstract void setShapedRecipeIngredientAsDye(ShapedRecipe recipe, char ingredient, DyeColor color);

	/**
	 * Used to add dyes in a {@link ShapelessRecipe}
	 * 
	 * @param recipe The {@link ShapelessRecipe} to modify
	 * @param count  The amount required
	 * @param color  The {@link DyeColor} to use
	 * @author Zeeraa
	 */
	public abstract void addShapelessRecipeIngredientAsDye(ShapelessRecipe recipe, int count, DyeColor color);

	/**
	 * Get an {@link ItemStack} of with the type provided in the form of
	 * {@link ColoredBlockType}
	 * 
	 * @param color The {@link DyeColor}
	 * @param type  The {@link ColoredBlockType}
	 * @return An {@link ItemStack} of the {@link ColoredBlockType}
	 * @author Zeeraa
	 */
	public abstract ItemStack getColoredItem(DyeColor color, ColoredBlockType type);

	/**
	 * GEt the {@link ItemBuilderRecordList} for this verion
	 * 
	 * @return {@link ItemBuilderRecordList}
	 * @author Zeeraa
	 */
	public abstract ItemBuilderRecordList getItemBuilderRecordList();

	/**
	 * Set the {@link MapView} of a map {@link ItemStack}
	 * 
	 * @param item    The {@link ItemStack} to set the map view of
	 * @param mapView The {@link MapView} to apply
	 * @author Zeeraa
	 */
	public abstract void attachMapView(ItemStack item, MapView mapView);

	/**
	 * Get the {@link MapView} of a map {@link ItemStack}
	 * 
	 * @param item The {@link ItemStack} to get the map view of
	 * @return mapView The {@link MapView} used for that item
	 * @author Zeeraa
	 */
	public abstract MapView getAttachedMapView(ItemStack item);

	/**
	 * Get the id number of a {@link MapView}
	 * 
	 * @param mapView The {@link MapView} to get the id of
	 * @return id number of the provided {@link MapView}
	 * @author Zeeraa
	 */
	public abstract int getMapViewId(MapView mapView);

	/**
	 * Play a sound for a player
	 * 
	 * @param player   The player to play the sound for
	 * @param location The location of the sound
	 * @param sound    The {@link VersionIndependentSound} to play
	 * @return <code>true</code> on success. <code>false</code> if the sound is not
	 *         configured
	 * @author Zeeraa
	 */
	public boolean playSound(Player player, Location location, VersionIndependentSound sound) {
		return this.playSound(player, location, sound, 1F, 1F);
	}

	/**
	 * Play a sound for a player
	 * 
	 * @param player   The player to play the sound for
	 * @param location The location of the sound
	 * @param sound    The {@link VersionIndependentSound} to play
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @return <code>true</code> on success. <code>false</code> if the sound is not
	 *         configured
	 * @author Zeeraa
	 */
	public boolean playSound(Player player, Location location, VersionIndependentSound sound, float volume, float pitch) {
		Sound realSound = this.getSound(sound);

		if (sound == null) {
			return false;
		}

		player.playSound(location, realSound, volume, pitch);
		return true;
	}

	/**
	 * Play a sound at a location
	 * 
	 * @param location The location of the sound
	 * @param sound    The {@link VersionIndependentSound} to play
	 * @return <code>true</code> on success. <code>false</code> if the sound is not
	 *         configured
	 * @author Zeeraa
	 */
	public boolean playSound(Location location, VersionIndependentSound sound) {
		return this.playSound(location, sound, 1F, 1F);
	}

	/**
	 * Play a sound at a location
	 * 
	 * @param location The location of the sound
	 * @param sound    The {@link VersionIndependentSound} to play
	 * @param volume   The volume of the sound
	 * @param pitch    The pitch of the sound
	 * @return <code>true</code> on success. <code>false</code> if the sound is not
	 *         configured
	 * @author Zeeraa
	 */
	public boolean playSound(Location location, VersionIndependentSound sound, float volume, float pitch) {
		Sound realSound = this.getSound(sound);

		if (sound == null) {
			return false;
		}

		location.getWorld().playSound(location, realSound, volume, pitch);
		return true;
	}

	/**
	 * Get the {@link Sound} from a {@link VersionIndependentSound}
	 * 
	 * @param sound The {@link VersionIndependentSound} to get
	 * @return resulting {@link Sound}
	 * @author Zeeraa
	 */
	public abstract Sound getSound(VersionIndependentSound sound);

	/**
	 * Send a title to a player
	 * 
	 * @param player   The {@link Player} that should receive the title
	 * @param title    The title text
	 * @param subtitle The subtitle text
	 * @param fadeIn   The amount of ticks for the fade in animation. Set to 0 to
	 *                 disable
	 * @param stay     The amount of ticks the text should stay
	 * @param fadeOut  The amount of ticks for the fade out animation. Set to 0 to
	 *                 disable
	 * @author Zeeraa
	 */
	public abstract void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

	/**
	 * Get an {@link ItemStack} with a skull that has its texture set to the
	 * provided base64 string
	 * 
	 * @param b64stringtexture The base64 string containing texture data
	 * @return {@link ItemStack} with a textured player skull
	 * @author Zeeraa
	 */
	public abstract ItemStack getPlayerSkullWithBase64Texture(String b64stringtexture);

	public static <T> Field getField(Class<?> target, String name, Class<T> fieldType, int index) {
		for (final Field field : target.getDeclaredFields()) {
			if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
				field.setAccessible(true);
				return field;
			}
		}

		// Search in parent classes
		if (target.getSuperclass() != null)
			return getField(target.getSuperclass(), name, fieldType, index);
		throw new IllegalArgumentException("Cannot find field with type " + fieldType);
	}

	/**
	 * Get the instance of {@link VersionIndependentItems} for this version of the
	 * game
	 * 
	 * @return {@link VersionIndependentItems} instance
	 * @author Zeeraa
	 */
	public abstract VersionIndependentItems getVersionIndependantItems();

	/**
	 * Used to add a player skull as a ingredient in a {@link ShapedRecipe}
	 * 
	 * @param recipe     The {@link ShapedRecipe} to modify
	 * @param ingredient The ingredient char
	 * @author Zeeraa
	 */
	public abstract void setShapedRecipeIngredientAsPlayerSkull(ShapedRecipe recipe, char ingredient);

	/**
	 * Get the {@link Material} from {@link VersionIndependentMaterial}
	 * 
	 * @param material The {@link VersionIndependentMaterial}
	 * @return The minecraft {@link Material}
	 * @author Zeeraa
	 */
	public abstract Material getMaterial(VersionIndependentMaterial material);

	/**
	 * Get an {@link ItemStack} with the material from a
	 * {@link VersionIndependentMaterial}. Can return null if the
	 * {@link VersionIndependentUtils} instance used is outdated. In case of missing
	 * materials please contact the devs
	 * 
	 * @param material The {@link VersionIndependentMaterial} the item should be of
	 * @return {@link ItemStack} of provided type or <code>null</code> if the
	 *         material has not been implemented by the devs
	 * @author Zeeraa
	 */
	public ItemStack getItemStack(VersionIndependentMaterial material) {
		Material mcMaterial = this.getMaterial(material);
		if (mcMaterial == null) {
			AbstractionLogger.getLogger().error("VersionIndependentUtils", "Failed to get version independent material " + material.name() + " for version " + this.getNovaCoreGameVersion().name() + ". This needs to be added");
			return null;
		}

		return new ItemStack(mcMaterial);
	}

	/**
	 * Get an {@link ItemStack} of a player skull
	 * 
	 * @return Player skull {@link ItemStack}
	 * @author Zeeraa
	 */
	public abstract ItemStack getPlayerSkullitem();

	/**
	 * Check if a block is a sign
	 * 
	 * @param block The block to check
	 * @return <code>true</code> if the block is a sign
	 * @author Zeeraa
	 */
	public boolean isSign(Block block) {
		return this.isSign(block.getType());
	}

	public abstract boolean isSign(Material material);

	/**
	 * Send a message to a player in their action bar
	 * 
	 * @param player  The {@link Player} that should receive the message
	 * @param message The message to send
	 * @author Zeeraa
	 */
	public abstract void sendActionBarMessage(Player player, String message);

	/**
	 * @return The minimum y level blocks can exist at
	 * @author Zeeraa
	 */
	public abstract int getMinY();

	/**
	 * Modify the unbreakable parameter of the provided {@link ItemMeta}
	 * 
	 * @param meta        The {@link ItemMeta} to change
	 * @param unbreakable <code>true</code> to make the item unbreakable
	 * @return the {@link ItemMeta} instance
	 * @author Zeeraa
	 */
	public abstract ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable);

	public ItemStack setUnbreakable(ItemStack item, boolean unbreakable) {
		ItemMeta meta = item.getItemMeta();
		this.setUnbreakable(meta, unbreakable);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Set the item in the main hand of a {@link Creature}
	 * 
	 * @param creature The {@link Creature} to use
	 * @param item     The {@link ItemStack} to place in the main hand
	 * @author Zeeraa
	 */
	public abstract void setCreatureItemInMainHand(Creature creature, ItemStack item);

	/**
	 * Get the body rotation of a {@link Player} using NMS
	 * 
	 * @param player The {@link Player} to get the rotation of
	 * @return Player body rotation
	 * @author Zeeraa
	 */
	public abstract float getPlayerBodyRotation(Player player);

	/**
	 * Set the custom model data int of {@link ItemMeta}. If the server does not
	 * support custom model data this will get no oped
	 * 
	 * @param meta The {@link ItemMeta} to modify
	 * @param data The custom model data
	 * @author Bruno
	 */
	public abstract void setCustomModelData(ItemMeta meta, int data);

	/**
	 * Secure way to modify gamerules since the 1.8 way got deprecated in newer
	 * versions of spigot
	 * 
	 * @param world The {@link World} to set the rule in
	 * @param rule  The rule to set
	 * @param value The value of the game rule
	 * @author Zeeraa
	 */
	public abstract void setGameRule(World world, String rule, String value);

	/**
	 * Check if a {@link PlayerInteractEvent} is in the main hand
	 * 
	 * @param event The {@link PlayerInteractEvent} to check
	 * @return <code>true</code> if the event was triggered by the main hand
	 * @author Zeeraa
	 */
	public abstract boolean isInteractEventMainHand(PlayerInteractEvent event);

	/**
	 * GEt an {@link Entity} by its {@link UUID}
	 * 
	 * @param uuid The {@link UUID} of the entity
	 * @return The {@link Entity} or <code>null</code> if not found
	 * @author Zeeraa
	 */
	public abstract Entity getEntityByUUID(UUID uuid);

	/**
	 * Set if the AI should be enabled or disabled on a {@link LivingEntity}
	 * 
	 * @param entity The entity
	 * @param ai     <code>false</code> to disable the ai
	 * @author Zeeraa
	 */
	public abstract void setAI(LivingEntity entity, boolean ai);

	/**
	 * Check if a block is a bed
	 * 
	 * @param block The {@link Block} to check
	 * @return <code>true</code> if its a bed
	 * @author Zeeraa
	 */
	public boolean isBed(Block block) {
		return this.isBed(block.getType());
	}

	/**
	 * Check if a material is a bed
	 * 
	 * @param material The {@link Material} to check
	 * @return <code>true</code> if its a bed
	 * @author Zeeraa
	 */
	public boolean isBed(Material material) {
		return BED_MATERIALS.contains(material.name());
	}

	/**
	 * Sets an entity as silent
	 * 
	 * @param entity The {@link LivingEntity}
	 * @param silent boolean
	 * @author Zeeraa
	 */
	public abstract void setSilent(LivingEntity entity, boolean silent);

	/**
	 * Predicts what {@link DeathType} would get if they died from that damage
	 * 
	 * @param e           The {@link EntityDamageEvent} (could be any of its
	 *                    children)
	 * @param lastDamager The last damager
	 * @author Bruno
	 * 
	 * @return DeathType from {@link EntityDeathEvent}
	 */
	public abstract DeathType getDeathTypeFromDamage(EntityDamageEvent e, Entity lastDamager);

	/**
	 * Colorizes the message with one of 16 million RGB colors
	 * 
	 * @param color   The {@link Color}
	 * @param message The message
	 * @author Bruno
	 * 
	 * @return Colorized message
	 */
	public abstract String colorize(Color color, String message);

	/**
	 * Colorizes the message as a gradient (transitioning from the start of the
	 * {@link Color[]} array to the end)
	 * 
	 * @param colors  The {@link Color[]} array
	 * @param message The message
	 * @author Bruno
	 * 
	 * @return Colorized message
	 */
	public abstract String colorizeGradient(Color[] colors, String message);

	/**
	 * Colorizes the message in a rainbow pattern (changes from 1 color to another,
	 * always looping) with changeable intervals between them
	 * 
	 * @param colors        The {@link Color[]} array
	 * @param charsPerColor How many characters before going to next color
	 * @param message       The message
	 * @author Bruno
	 * 
	 * @return Colorized message
	 */
	public abstract String colorizeRainbow(Color[] colors, int charsPerColor, String message);

	public abstract PacketManager getPacketManager();

	public abstract boolean canBreakBlock(ItemStack item, Material material);

	public abstract MaterialNameList getMaterialNameList();

	public abstract Material getMaterialFromName(String name);

	public abstract void sendPacket(Player player, Object packet);

	public abstract void addAttribute(ItemStack item, ItemMeta meta, AttributeInfo attributeInfo);

	public abstract Block getTargetBlockExact(LivingEntity entity, int distance, List<Material> ignore);

	public abstract Block getReacheableBlockExact(LivingEntity entity);

	public abstract FallingBlock spawnFallingBlock(Location location, Material material, byte data, Consumer<FallingBlock> consumer);

	public abstract void setPotionEffect(ItemStack item, ItemMeta meta, PotionEffect effect, boolean color);

	public abstract void setPotionColor(ItemMeta meta, org.bukkit.Color color);

	public abstract Block getBlockFromProjectileHitEvent(ProjectileHitEvent e);

	/**
	 * Create a {@link ShapedRecipe} in any version of the game without receiving
	 * warnings in the console
	 * 
	 * @param result The output of the recipe
	 * @param owner  The {@link Plugin} that owns the recipe. The plugin will be
	 *               used in the namespaced key
	 * @param key    The key to use in the namespaced key
	 * @return A {@link ShapedRecipe} with a namespaced key if applicable
	 */
	public abstract ShapedRecipe createShapedRecipeSafe(ItemStack result, Plugin owner, String key);

	/**
	 * Create a {@link ShapelessRecipe} in any version of the game without receiving
	 * warnings in the console
	 * 
	 * @param result The output of the recipe
	 * @param owner  The {@link Plugin} that owns the recipe. The plugin will be
	 *               used in the namespaced key
	 * @param key    The key to use in the namespaced key
	 * @return A {@link ShapelessRecipe} with a namespaced key if applicable
	 */
	public abstract ShapelessRecipe createShapelessRecipe(ItemStack result, Plugin owner, String key);

	/**
	 * Send a title to all players online
	 * 
	 * @param title    The title text
	 * @param subtitle The subtitle text
	 * @param fadeIn   The amount of ticks for the fade in animation. Set to 0 to
	 *                 disable
	 * @param stay     The amount of ticks the text should stay
	 * @param fadeOut  The amount of ticks for the fade out animation. Set to 0 to
	 *                 disable
	 * @author Zeeraa
	 */
	public void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		Bukkit.getServer().getOnlinePlayers().forEach(player -> this.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut));
	}

	/**
	 * Send a title to all players online in the specified world
	 * 
	 * @param world    The world to broadcast to
	 * @param title    The title text
	 * @param subtitle The subtitle text
	 * @param fadeIn   The amount of ticks for the fade in animation. Set to 0 to
	 *                 disable
	 * @param stay     The amount of ticks the text should stay
	 * @param fadeOut  The amount of ticks for the fade out animation. Set to 0 to
	 *                 disable
	 * @author Zeeraa
	 */
	public void broadcastTitle(World world, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.getWorld().equals(world)).forEach(player -> this.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut));
	}
	
	public Color bungeecordChatColorToJavaColor(ChatColor color) {
		return DefaultBungeecordColorMapper.getColorOfChatColor(color);
	}

	public org.bukkit.ChatColor bungeecordChatColorToBukkitChatColor(ChatColor color) {
		return DefaultBungeecordColorMapper.getBukkitChatColorOfBungeeChatColor(color);
	}

	public abstract void displayTotem(Player player);

	public abstract void displayCustomTotem(Player player, int cmd);
}