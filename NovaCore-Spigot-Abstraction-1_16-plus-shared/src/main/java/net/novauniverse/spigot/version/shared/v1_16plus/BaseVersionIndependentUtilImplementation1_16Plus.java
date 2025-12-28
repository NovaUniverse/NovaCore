package net.novauniverse.spigot.version.shared.v1_16plus;

import java.awt.Color;
import java.util.Base64;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.json.JSONException;
import org.json.JSONObject;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.novauniverse.spigot.version.shared.v1_16plus.bossbar.NovaNativeBossBar;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.LoopableIterator;
import net.zeeraa.novacore.commons.utils.RandomGenerator;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentLoader;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.bossbar.NovaBossBar;
import net.zeeraa.novacore.spigot.abstraction.enums.ColoredBlockType;
import net.zeeraa.novacore.spigot.abstraction.enums.NovaCoreGameVersion;

public abstract class BaseVersionIndependentUtilImplementation1_16Plus extends VersionIndependentUtils {
	private DyeColorToMaterialMapper dyeColorToMaterialMapper;
	protected Random random;

	public BaseVersionIndependentUtilImplementation1_16Plus(VersionIndependentLoader loader, DyeColorToMaterialMapper colorToMaterialMapper) {
		super(loader);
		this.dyeColorToMaterialMapper = colorToMaterialMapper;
		this.random = new Random();
	}

	public DyeColorToMaterialMapper getDyeColorToMaterialMapper() {
		return dyeColorToMaterialMapper;
	}

	public abstract Material getColoredMaterial(DyeColor color, ColoredBlockType type);

	@Override
	public NovaBossBar createBossBar(String text) {
		return new NovaNativeBossBar(text);
	}

	public static String addDashesToUUID(String uuidWithoutDashes) {
		if (uuidWithoutDashes.length() != 32) {
			throw new IllegalArgumentException("Cant extend non uuid with non 32 length");
		}

		StringBuilder formattedUUID = new StringBuilder(uuidWithoutDashes);
		formattedUUID.insert(8, "-");
		formattedUUID.insert(13, "-");
		formattedUUID.insert(18, "-");
		formattedUUID.insert(23, "-");

		return formattedUUID.toString();
	}

	@Override
	public ItemStack getPlayerSkullWithBase64Texture(String b64stringtexture) {
		UUID uuid = UUID.randomUUID();
		String name = null;

		byte[] decodedBytes = Base64.getDecoder().decode(b64stringtexture);
		String decodedString = new String(decodedBytes);
		try {
			JSONObject json = new JSONObject(decodedString);

			if (json.has("profileName")) {
				name = json.getString("profileName");
			}

			if (json.has("profileId")) {
				String profileId = json.getString("profileId");
				if (profileId.length() == 32) {
					uuid = UUID.fromString(addDashesToUUID(profileId));
				} else if (profileId.length() == 36) {
					uuid = UUID.fromString(profileId);
				} else {
					Log.warn("getPlayerSkullWithBase64Texture", "Profile id: " + profileId + " seems to be invalid");
				}
			}
		} catch (Exception e) {
			if (e instanceof JSONException) {
				Log.error("VersionIndependentUtils", "Got " + e.getClass().getName() + " when parsing base64 encoded texture. Make sure that you entered a valid base64 encoded player texture. Message: " + e.getMessage());
			}
			e.printStackTrace();
			return new ItemStack(Material.PLAYER_HEAD, 1);
		}

		if (name == null) {
			name = "Player";
		}

		GameProfile profile = new GameProfile(uuid, name);
		PropertyMap propertyMap = profile.getProperties();
		if (propertyMap == null) {
			throw new IllegalStateException("Profile doesn't contain a property map");
		}

		if (name == null && getNovaCoreGameVersion().isAfterOrEqual(NovaCoreGameVersion.V_1_20_R2)) {
			name = RandomGenerator.randomAlphanumericString(16, random);
		}

		propertyMap.put("textures", new Property("textures", b64stringtexture));
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		try {
			getField(headMetaClass, "profile", GameProfile.class, 0).set(headMeta, profile);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		this.preProcessHeadMetaApplication(headMeta, profile, head);
		head.setItemMeta(headMeta);
		return head;
	}

	protected void preProcessHeadMetaApplication(ItemMeta meta, GameProfile profile, ItemStack stack) {
	}

	@Override
	public void setBlockAsPlayerSkull(Block block) {
		block.setType(Material.PLAYER_HEAD);
		block.getState().update(true);
	}

	@Override
	public ItemStack getItemInMainHand(Player player) {
		return player.getInventory().getItemInMainHand();
	}

	@Override
	public ItemStack getItemInOffHand(Player player) {
		return player.getInventory().getItemInOffHand();
	}

	@Override
	public double getEntityMaxHealth(LivingEntity livingEntity) {
		return livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
	}

	@Override
	public void setEntityMaxHealth(LivingEntity livingEntity, double health) {
		livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
	}

	@Override
	public void resetEntityMaxHealth(LivingEntity livingEntity) {
		livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
	}

	@Override
	public void cloneBlockData(Block source, Block target) {
		target.setBlockData(source.getBlockData());
	}

	@Override
	public void setItemInMainHand(Player player, ItemStack item) {
		player.getInventory().setItemInMainHand(item);
	}

	@Override
	public void setItemInOffHand(Player player, ItemStack item) {
		player.getInventory().setItemInOffHand(item);
	}

	@Override
	public void setColoredBlock(Block block, DyeColor color, ColoredBlockType type) {
		Material material = getColoredMaterial(color, type);

		block.setType(material);
	}

	@Override
	public ItemStack getColoredItem(DyeColor color, ColoredBlockType type) {
		return new ItemStack(getColoredMaterial(color, type));
	}

	@Override
	public void setShapedRecipeIngredientAsColoredBlock(ShapedRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color) {
		recipe.setIngredient(ingredient, getColoredMaterial(color, type));
	}

	@Override
	public void addShapelessRecipeIngredientAsColoredBlock(ShapelessRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color) {
		recipe.addIngredient(getColoredMaterial(color, type));
	}

	@Override
	public void attachMapView(ItemStack item, MapView mapView) {
		MapMeta meta = (MapMeta) item.getItemMeta();

		meta.setMapView(mapView);

		item.setItemMeta(meta);
	}

	@Override
	public MapView getAttachedMapView(ItemStack item) {
		MapMeta meta = (MapMeta) item.getItemMeta();

		return meta.getMapView();
	}

	@Override
	public int getMapViewId(MapView mapView) {
		return mapView.getId();
	}

	@Override
	public void setShapedRecipeIngredientAsPlayerSkull(ShapedRecipe recipe, char ingredient) {
		recipe.setIngredient(ingredient, Material.PLAYER_HEAD);
	}

	@Override
	public ItemStack getPlayerSkullitem() {
		return new ItemStack(Material.PLAYER_HEAD, 1);
	}

	@Override
	public void sendActionBarMessage(Player player, String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}

	@Override
	public ItemMeta setUnbreakable(ItemMeta meta, boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return meta;
	}

	@Override
	public void setCreatureItemInMainHand(Creature creature, ItemStack item) {
		creature.getEquipment().setItemInMainHand(item);
	}

	@Override
	public void setCustomModelData(ItemMeta meta, int data) {
		meta.setCustomModelData(data);
	}

	@Override
	public boolean isInteractEventMainHand(PlayerInteractEvent e) {
		return e.getHand() == EquipmentSlot.HAND;
	}

	@Override
	public Entity getEntityByUUID(UUID uuid) {
		return Bukkit.getEntity(uuid);
	}

	@Override
	public void setShapedRecipeIngredientAsDye(ShapedRecipe recipe, char ingredient, DyeColor color) {
		recipe.setIngredient(ingredient, dyeColorToMaterialMapper.dyeColorToMaterial(color));
	}

	@Override
	public void addShapelessRecipeIngredientAsDye(ShapelessRecipe recipe, int count, DyeColor color) {
		recipe.addIngredient(count, dyeColorToMaterialMapper.dyeColorToMaterial(color));
	}

	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		if (title.length() == 0) {
			title = " ";
		}
		player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	@Override
	public void setAI(LivingEntity entity, boolean ai) {
		entity.setAI(ai);
	}

	@Override
	public void setSilent(LivingEntity entity, boolean silent) {
		entity.setSilent(silent);
	}

	@Override
	public String colorize(Color color, String message) {
		return ChatColor.of(color).toString() + message;
	}

	@Override
	public String colorizeGradient(Color[] colors, String message) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			builder.append(ChatColor.of(new Color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue()))).append(message.toCharArray()[i]);
		}
		return builder.toString();
	}

	@Override
	public String colorizeRainbow(Color[] colors, int charsPerColor, String message) {
		LoopableIterator<Color> iterator = new LoopableIterator<>();
		Collections.addAll(iterator, colors);
		StringBuilder finalBuild = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {

			if (i % charsPerColor == 0) {
				finalBuild.append(colorize(iterator.next(), message.toCharArray()[i] + ""));
			} else {
				finalBuild.append(message.toCharArray()[i]);
			}

		}
		return finalBuild.toString();
	}

	@Override
	public void setPotionEffect(ItemStack item, ItemMeta meta, PotionEffect effect, boolean color) {
		if (meta instanceof PotionMeta) {
			PotionMeta potMeta = (PotionMeta) meta;
			potMeta.addCustomEffect(effect, true);
			if (color) {
				potMeta.setColor(effect.getType().getColor());
			}
		}
	}

	@Override
	public void setPotionColor(ItemMeta meta, org.bukkit.Color color) {
		if (meta instanceof PotionMeta) {
			PotionMeta potMeta = (PotionMeta) meta;
			potMeta.setColor(color);
		}
	}

	@Override
	public Block getBlockFromProjectileHitEvent(ProjectileHitEvent e) {
		return e.getHitBlock();
	}

	@Override
	public ShapedRecipe createShapedRecipeSafe(ItemStack result, Plugin owner, String key) {
		return new ShapedRecipe(new NamespacedKey(owner, key.toLowerCase()), result);
	}

	@Override
	public ShapelessRecipe createShapelessRecipe(ItemStack result, Plugin owner, String key) {
		return new ShapelessRecipe(new NamespacedKey(owner, key.toLowerCase()), result);
	}

	@Override
	public Color bungeecordChatColorToJavaColor(ChatColor color) {
		return color.getColor();
	}

	@Override
	public void setMarker(ArmorStand stand, boolean marker) {
		stand.setMarker(marker);
	}

	@Override
	public boolean isMarker(ArmorStand stand) {
		return stand.isMarker();
	}

	@Override
	public ItemStack getColoredBannerItemStack(DyeColor color) {
		return SharedBannerItemStackCreator.getColoredBannerItemStack(color);
	}

	@Override
	public void registerCustomEntity(Class<?> entity, String name) {
		// there is no need to register custom entities on 1.14+
	}

	@Override
	public void registerCustomEntityWithEntityId(Class<?> entity, String name, int id) {
		// there is no need to register custom entities on 1.14+
	}

	@Override
	public boolean isArrowInBlock(Arrow arrow) {
		return arrow.isInBlock();
	}

	@Override
	public void showBlockBreakParticles(Block block, int particleCount) {
		BlockData blockData = block.getBlockData();
		Particle particle = Particle.BLOCK_CRACK;

		block.getLocation().getWorld().spawnParticle(particle, block.getLocation(), particleCount, blockData);
	}

	@Override
	public Block getArrowAttachedBlock(Arrow arrow) {
		return arrow.getAttachedBlock();
	}
}