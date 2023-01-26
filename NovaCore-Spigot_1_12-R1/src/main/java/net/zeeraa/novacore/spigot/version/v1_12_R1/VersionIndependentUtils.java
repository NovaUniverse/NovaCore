package net.zeeraa.novacore.spigot.version.v1_12_R1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.zeeraa.novacore.commons.utils.ListUtils;
import net.zeeraa.novacore.spigot.abstraction.ChunkLoader;
import net.zeeraa.novacore.spigot.abstraction.ItemBuilderRecordList;
import net.zeeraa.novacore.spigot.abstraction.MaterialNameList;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentItems;
import net.zeeraa.novacore.spigot.abstraction.commons.AttributeInfo;
import net.zeeraa.novacore.spigot.abstraction.enums.ColoredBlockType;
import net.zeeraa.novacore.spigot.abstraction.enums.DeathType;
import net.zeeraa.novacore.spigot.abstraction.enums.NovaCoreGameVersion;
import net.zeeraa.novacore.spigot.abstraction.enums.PlayerDamageReason;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependenceLayerError;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentMaterial;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityFallingBlock;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IBlockAccess;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagDouble;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.zeeraa.novacore.spigot.abstraction.log.AbstractionLogger;
import net.zeeraa.novacore.spigot.abstraction.packet.PacketManager;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.map.MapView;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VersionIndependentUtils extends net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils {
	private ItemBuilderRecordList itemBuilderRecordList;
	private MaterialNameList materialNameList;
	private PacketManager packetManager;

	private ChunkLoader chunkLoader;

	@Override
	public ChunkLoader getChunkLoader() {
		if (chunkLoader == null) {
			chunkLoader = new ChunkLoaderImplementation();
		}
		return chunkLoader;
	}

	public VersionIndependentUtils() {
		itemBuilderRecordList = new ItemBuilderRecordList1_12();
		materialNameList = MaterialNameList1_12.get();
	}

	@Override
	public ItemBuilderRecordList getItemBuilderRecordList() {
		return itemBuilderRecordList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBlockAsPlayerSkull(Block block) {
		block.setType(Material.SKULL);
		Skull skull = (Skull) block.getState();
		skull.setSkullType(SkullType.PLAYER);

		block.getState().update(true);

		// TODO: fix
		block.setData((byte) 1);
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

	@SuppressWarnings("deprecation")
	@Override
	public double[] getRecentTps() {
		// Deprecated but still the way spigot does it in the /tps command
		return MinecraftServer.getServer().recentTps;
	}

	@Override
	public int getPlayerPing(Player player) {
		return ((CraftPlayer) player).getHandle().ping;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void cloneBlockData(Block source, Block target) {
		target.setData(source.getData());
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
	public void sendTabList(Player player, String header, String footer) {
		CraftPlayer craftplayer = (CraftPlayer) player;
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		IChatBaseComponent headerJSON = ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent footerJSON = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

		try {
			Field headerField = packet.getClass().getDeclaredField("a");
			headerField.setAccessible(true);
			headerField.set(packet, headerJSON);
			headerField.setAccessible(!headerField.isAccessible());

			Field footerField = packet.getClass().getDeclaredField("b");
			footerField.setAccessible(true);
			footerField.set(packet, footerJSON);
			footerField.setAccessible(!footerField.isAccessible());
		} catch (Exception e) {
			e.printStackTrace();
		}

		connection.sendPacket(packet);
	}

	@Override
	public void damagePlayer(Player player, PlayerDamageReason reason, float damage) {
		DamageSource source;

		switch (reason) {
		case FALL:
			source = DamageSource.FALL;
			break;
		case FALLING_BLOCK:
			source = DamageSource.FALLING_BLOCK;
			break;
		case OUT_OF_WORLD:
			source = DamageSource.OUT_OF_WORLD;
			break;

		case BURN:
			source = DamageSource.BURN;
			break;

		case LIGHTNING:
			source = DamageSource.LIGHTNING;
			break;

		case MAGIC:
			source = DamageSource.MAGIC;
			break;

		case DROWN:
			source = DamageSource.DROWN;
			break;

		case STARVE:
			source = DamageSource.STARVE;
			break;

		case LAVA:
			source = DamageSource.LAVA;
			break;

		case GENERIC:
			source = DamageSource.GENERIC;
			break;

		default:
			source = DamageSource.GENERIC;
			break;
		}

		((CraftPlayer) player).getHandle().damageEntity(source, damage);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setColoredBlock(Block block, DyeColor color, ColoredBlockType type) {
		MaterialData data = getColoredBlockMaterialData(color, type);

		block.setType(data.getItemType());

		block.setData(data.getData());
	}

	@SuppressWarnings("deprecation")
	private MaterialData getColoredBlockMaterialData(DyeColor color, ColoredBlockType type) {
		Material material = null;

		switch (type) {
		case GLASS_PANE:
			material = Material.STAINED_GLASS_PANE;
			break;

		case GLASS_BLOCK:
			material = Material.STAINED_GLASS;
			break;

		case WOOL:
			material = Material.WOOL;
			break;

		case CLAY:
			material = Material.STAINED_CLAY;
			break;

		default:
			material = Material.STAINED_GLASS;
			break;
		}

		MaterialData data = new MaterialData(material, color.getWoolData());

		return data;
	}

	@Override
	public void setShapedRecipeIngredientAsColoredBlock(ShapedRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color) {
		MaterialData data = getColoredBlockMaterialData(color, type);

		recipe.setIngredient(ingredient, data);
	}

	@Override
	public void addShapelessRecipeIngredientAsColoredBlock(ShapelessRecipe recipe, char ingredient, ColoredBlockType type, DyeColor color) {
		MaterialData data = getColoredBlockMaterialData(color, type);

		recipe.addIngredient(ingredient, data);
	}

	@Override
	public ItemStack getColoredItem(DyeColor color, ColoredBlockType type) {
		MaterialData data = getColoredBlockMaterialData(color, type);

		@SuppressWarnings("deprecation")
		ItemStack stack = new ItemStack(data.getItemType(), 1, (short) data.getData());

		stack.setData(data);

		return stack;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void attachMapView(ItemStack item, MapView mapView) {
		item.setDurability(mapView.getId());
	}

	@SuppressWarnings("deprecation")
	@Override
	public MapView getAttachedMapView(ItemStack item) {
		return Bukkit.getMap(item.getDurability());
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getMapViewId(MapView mapView) {
		return (int) mapView.getId();
	}

	@Override
	public Sound getSound(VersionIndependentSound sound) {
		switch (sound) {
		case NOTE_PLING:
			return Sound.BLOCK_NOTE_PLING;

		case NOTE_HAT:
			return Sound.BLOCK_NOTE_HAT;

		case WITHER_DEATH:
			return Sound.ENTITY_WITHER_DEATH;

		case WITHER_HURT:
			return Sound.ENTITY_WITHER_HURT;

		case ITEM_BREAK:
			return Sound.ENTITY_ITEM_BREAK;

		case ITEM_PICKUP:
			return Sound.ENTITY_ITEM_PICKUP;

		case ORB_PICKUP:
			return Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

		case ANVIL_LAND:
			return Sound.BLOCK_ANVIL_LAND;

		case EXPLODE:
			return Sound.ENTITY_GENERIC_EXPLODE;

		case LEVEL_UP:
			return Sound.ENTITY_PLAYER_LEVELUP;

		case WITHER_SHOOT:
			return Sound.ENTITY_WITHER_SHOOT;

		case EAT:
			return Sound.ENTITY_GENERIC_EAT;

		case ANVIL_BREAK:
			return Sound.BLOCK_ANVIL_BREAK;

		case FIZZ:
			return Sound.BLOCK_FIRE_EXTINGUISH;

		case ENDERMAN_TELEPORT:
			return Sound.ENTITY_ENDERMEN_TELEPORT;

		case CLICK:
			return Sound.BLOCK_LEVER_CLICK;

		default:
			setLastError(VersionIndependenceLayerError.MISSING_SOUND);
			AbstractionLogger.getLogger().error("VersionIndependentUtils", "VersionIndependantSound " + sound.name() + " is not defined in this version. Please add it to " + this.getClass().getName());
			return null;
		}
	}

	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	@Override
	public ItemStack getPlayerSkullWithBase64Texture(String b64stringtexture) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		PropertyMap propertyMap = profile.getProperties();
		if (propertyMap == null) {
			throw new IllegalStateException("Profile doesn't contain a property map");
		}
		propertyMap.put("textures", new Property("textures", b64stringtexture));
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		try {
			getField(headMetaClass, "profile", GameProfile.class, 0).set(headMeta, profile);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}

	@Override
	public VersionIndependentItems getVersionIndependantItems() {
		return new net.zeeraa.novacore.spigot.version.v1_12_R1.VersionIndependantItems();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setShapedRecipeIngredientAsPlayerSkull(ShapedRecipe recipe, char ingredient) {
		MaterialData skull = new MaterialData(Material.SKULL_ITEM);

		skull.setData((byte) 3);

		recipe.setIngredient(ingredient, skull);
	}

	@Override
	public Material getMaterial(VersionIndependentMaterial material) {
		switch (material) {
		case FILLED_MAP:
			return Material.MAP;

		case END_STONE:
			return Material.ENDER_STONE;

		case WORKBENCH:
			return Material.WORKBENCH;

		case OAK_BOAT:
			return Material.BOAT;

		case DIAMOND_SHOVEL:
			return Material.DIAMOND_SPADE;

		case SNOWBALL:
			return Material.SNOW_BALL;

		case FARMLAND:
			return Material.SOIL;

		case GOLDEN_AXE:
			return Material.GOLD_AXE;

		case GOLDEN_HOE:
			return Material.GOLD_HOE;

		case GOLDEN_PICKAXE:
			return Material.GOLD_PICKAXE;

		case GOLDEN_SHOVEL:
			return Material.GOLD_SPADE;

		case GOLDEN_SWORD:
			return Material.GOLD_SWORD;

		case WOODEN_AXE:
			return Material.WOOD_AXE;

		case WOODEN_HOE:
			return Material.WOOD_HOE;

		case WOODEN_PICKAXE:
			return Material.WOOD_PICKAXE;

		case WOODEN_SHOVEL:
			return Material.WOOD_SPADE;

		case WOODEN_SWORD:
			return Material.WOOD_SWORD;

		case WATCH:
			return Material.WATCH;

		case GOLD_HELMET:
			return Material.GOLD_HELMET;

		case GOLD_CHESTPLATE:
			return Material.GOLD_CHESTPLATE;

		case GOLD_LEGGINGS:
			return Material.GOLD_LEGGINGS;

		case GOLD_BOOTS:
			return Material.GOLD_BOOTS;

		case GRILLED_PORK:
			return Material.GRILLED_PORK;

		case EXP_BOTTLE:
			return Material.EXP_BOTTLE;

		default:
			setLastError(VersionIndependenceLayerError.MISSING_MATERIAL);
			AbstractionLogger.getLogger().warning("VersionIndependentUtils", "Unknown version Independent material: " + material.name());
			return null;
		}
	}

	@Override
	public NovaCoreGameVersion getNovaCoreGameVersion() {
		return NovaCoreGameVersion.V_1_12;
	}

	@Override
	public ItemStack getPlayerSkullitem() {
		return new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
	}

	@Override
	public boolean isSign(Material material) {
		return material == Material.SIGN_POST || material == Material.WALL_SIGN;
	}

	@Override
	public void sendActionBarMessage(Player player, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), ChatMessageType.GAME_INFO);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public int getMinY() {
		return 0;
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
	public float getPlayerBodyRotation(Player player) {
		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		return nmsPlayer.aN;
	}

	@Override
	public void setCustomModelData(ItemMeta meta, int data) {
		AbstractionLogger.getLogger().error("VersionIndependentUtils", "Current version does not have CustomModelData support.");
	}

	@Override
	public void setGameRule(World world, String rule, String value) {
		world.setGameRuleValue(rule, value);
	}

	@Override
	public boolean isInteractEventMainHand(PlayerInteractEvent e) {
		return e.getHand() == EquipmentSlot.HAND;
	}

	@Override
	public Entity getEntityByUUID(UUID uuid) {
		return Bukkit.getEntity(uuid);
	}

	@SuppressWarnings("deprecation")
	private MaterialData getDyeMaterialData(DyeColor color) {
		return new MaterialData(Material.INK_SACK, color.getDyeData());
	}

	@Override
	public void setShapedRecipeIngredientAsDye(ShapedRecipe recipe, char ingredient, DyeColor color) {
		recipe.setIngredient(ingredient, getDyeMaterialData(color));
	}

	@Override
	public void addShapelessRecipeIngredientAsDye(ShapelessRecipe recipe, int count, DyeColor color) {
		recipe.addIngredient(count, getDyeMaterialData(color));
	}

	@Override
	public void setAI(LivingEntity entity, boolean ai) {
		entity.setAI(ai);
	}

	@Override
	public boolean isBed(Material material) {
		// Faster implementation since 1.12.2 only have 2 types of bed
		return material == Material.BED_BLOCK || material == Material.BED;
	}

	@Override
	public void setSilent(LivingEntity entity, boolean silent) {
		entity.setSilent(silent);
	}

	@Override
	public DeathType getDeathTypeFromDamage(EntityDamageEvent e, Entity lastDamager) {
		switch (e.getCause()) {
		case FIRE:
			if (lastDamager != null)
				return DeathType.FIRE_SOURCE_COMBAT;
			return DeathType.FIRE_SOURCE;

		case LAVA:
			if (lastDamager != null)
				return DeathType.LAVA_COMBAT;
			return DeathType.LAVA;

		case FALL:
			if (e.getFinalDamage() <= 2.0)
				if (lastDamager != null)
					return DeathType.FALL_SMALL_COMBAT;
				else
					return DeathType.FALL_SMALL;

			return DeathType.FALL_BIG;

		case VOID:
			if (lastDamager != null)
				return DeathType.VOID_COMBAT;
			return DeathType.VOID;

		case THORNS:
			return DeathType.THORNS;
		case WITHER:
			if (lastDamager != null)
				return DeathType.EFFECT_WITHER_COMBAT;
			return DeathType.EFFECT_WITHER;

		case CONTACT:
			if (lastDamager != null)
				return DeathType.CACTUS_COMBAT;
			return DeathType.CACTUS;

		case DROWNING:
			if (lastDamager != null)
				return DeathType.DROWN_COMBAT;
			return DeathType.DROWN;

		case LIGHTNING:
			if (lastDamager != null)
				return DeathType.LIGHTNING_COMBAT;
			return DeathType.LIGHTNING;

		case PROJECTILE:
			if (lastDamager.getType() == EntityType.ARROW)
				return DeathType.PROJECTILE_ARROW;
			return DeathType.PROJECTILE_OTHER;
		case STARVATION:
			if (lastDamager != null)
				return DeathType.STARVING_COMBAT;
			return DeathType.STARVING;
		case SUFFOCATION:
			if (lastDamager != null)
				return DeathType.SUFFOCATION_COMBAT;
			return DeathType.SUFFOCATION;

		case ENTITY_ATTACK:
		case ENTITY_SWEEP_ATTACK:
			switch (lastDamager.getType()) {
			case WITHER:
				return DeathType.COMBAT_WITHER_SKULL;
			case FIREBALL:
			case SMALL_FIREBALL:
				return DeathType.COMBAT_FIREBALL;
			default:
				return DeathType.COMBAT_NORMAL;
			}
		case FALLING_BLOCK:
			if (e instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) e;
				if (entityEvent.getDamager() instanceof FallingBlock) {
					FallingBlock block = (FallingBlock) entityEvent.getDamager();
					switch (block.getMaterial()) {
					case ANVIL:
						if (lastDamager != null)
							return DeathType.ANVIL_FALL_COMBAT;
						return DeathType.ANVIL_FALL;
					default:
						if (lastDamager != null)
							return DeathType.BLOCK_FALL_COMBAT;
						return DeathType.BLOCK_FALL;
					}
				}
			}
		case BLOCK_EXPLOSION:
		case ENTITY_EXPLOSION:
			if (lastDamager != null)
				return DeathType.EXPLOSION_COMBAT;
			return DeathType.EXPLOSION;

		case FIRE_TICK:
			if (lastDamager != null)
				return DeathType.FIRE_NATURAL_COMBAT;
			return DeathType.FIRE_NATURAL;

		case MAGIC:
			DeathType type = DeathType.MAGIC;
			if (lastDamager != null) {
				if (e instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) e;
					if (entityEvent.getDamager() instanceof ThrownPotion) {
						ThrownPotion potion = (ThrownPotion) entityEvent.getDamager();
						if (potion.getShooter() instanceof Entity) {
							if (((Entity) potion.getShooter()).getUniqueId().toString().equalsIgnoreCase(lastDamager.getUniqueId().toString())) {
								type = DeathType.MAGIC_COMBAT;
							} else {
								type = DeathType.MAGIC_COMBAT_ACCIDENT;
							}
						}
					}
				}
			}
			return type;
		case CRAMMING:
			if (lastDamager != null)
				return DeathType.SUFFOCATION_CRAMMING_COMBAT;
			return DeathType.SUFFOCATION_COMBAT;

		case HOT_FLOOR:
			if (lastDamager != null)
				return DeathType.MAGMA_BLOCK_COMBAT;
			return DeathType.MAGMA_BLOCK;

		case DRAGON_BREATH:
			if (lastDamager != null)
				return DeathType.DRAGON_BREATH_COMBAT;
			return DeathType.DRAGON_BREATH;

		case FLY_INTO_WALL:
			if (lastDamager != null)
				return DeathType.ELYTRA_WALL_COMBAT;
			return DeathType.ELYTRA_WALL;
		case POISON:
		case MELTING:
		case CUSTOM:
		case SUICIDE:
		default:
			if (lastDamager != null)
				return DeathType.GENERIC_COMBAT;
			return DeathType.GENERIC;

		}
	}

	@Override
	public String colorize(Color color, String message) {
		// does not work on 1.15 and below
		return message;
	}

	@Override
	public String colorizeGradient(Color[] colors, String message) {
		// does not work on 1.15 and below
		return message;
	}

	@Override
	public String colorizeRainbow(Color[] colors, int charsPerColor, String message) {
		// does not work on 1.15 and below
		return message;
	}

	@Override
	public PacketManager getPacketManager() {
		if (packetManager == null)
			packetManager = new net.zeeraa.novacore.spigot.version.v1_12_R1.packet.PacketManager();
		return packetManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean canBreakBlock(ItemStack item, Material block) {

		net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		if (nmsItem == null) {
			return false;
		}
		NBTTagCompound nbtTag = nmsItem.getTag();
		if (nbtTag == null) {
			return false;
		}
		NBTTagList list = nbtTag.getList("CanDestroy", 8);
		try {
			Field f = NBTTagList.class.getDeclaredField("list");
			f.setAccessible(true);

			for (NBTTagString nbt : (List<NBTTagString>) f.get(list)) {
				boolean b = getMaterialFromName(nbt.c_()) == block;

				if (b) {
					return true;
				}
			}
		} catch (Exception e1) {
			return false;
		}

		return false;
	}

	@Override
	public MaterialNameList getMaterialNameList() {
		return materialNameList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Material getMaterialFromName(String name) {
		try {
			int value = Integer.parseInt(name);
			for (Material material : Material.values()) {
				if (value == material.getId()) {
					return material;
				}
			}
			return null;
		} catch (Exception ignored) {
		}

		if (Material.matchMaterial(name) == null) {
			String updatedname = name.replace("minecraft:", "").toLowerCase(Locale.ROOT);
			return materialNameList.getMaterialMap().getOrDefault(updatedname, null);
		} else {
			return Material.matchMaterial(name);
		}
	}

	@Override
	public void sendPacket(Player player, Object packet) {
		if (packet instanceof Packet) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
		} else {
			AbstractionLogger.getLogger().warning("NovaCore", "Packet sent isnt instance of " + Packet.class.getCanonicalName());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addAttribute(ItemStack item, ItemMeta meta, AttributeInfo attributeInfo) {
		if (attributeInfo == null) {
			AbstractionLogger.getLogger().error("VersionIndependentUtils", "AttributeInfo is null");
			return;
		}

		if (attributeInfo.getAttribute() == null) {
			AbstractionLogger.getLogger().error("VersionIndependentUtils", "Attribute is null");
			return;
		}

		net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

		switch (attributeInfo.getAttribute()) {
		case GENERIC_MAX_HEALTH:
		case GENERIC_FOLLOW_RANGE:
		case GENERIC_KNOCKBACK_RESISTANCE:
		case GENERIC_MOVEMENT_SPEED:
		case GENERIC_FLYING_SPEED:
		case GENERIC_ATTACK_DAMAGE:
		case GENERIC_ATTACK_SPEED:
		case GENERIC_ARMOR:
		case GENERIC_ARMOR_TOUGHNESS:
		case GENERIC_LUCK:
		case HORSE_JUMP_STRENGTH:
		case ZOMBIE_SPAWN_REINFORCEMENTS:
			break;
		default:
			AbstractionLogger.getLogger().error("VersionIndependentUtils", "Attribute " + attributeInfo.getAttribute().name() + " (" + attributeInfo.getAttribute().getPre1_16Key() + ") does not exist in current version");
			return;
		}

		List<net.zeeraa.novacore.spigot.abstraction.enums.EquipmentSlot> newList = ListUtils.removeDuplicates(attributeInfo.getEquipmentSlots());

		if (newList.containsAll(Arrays.stream(net.zeeraa.novacore.spigot.abstraction.enums.EquipmentSlot.values()).filter(es -> es != net.zeeraa.novacore.spigot.abstraction.enums.EquipmentSlot.ALL).collect(Collectors.toList()))) {
			newList.clear();
			newList.add(net.zeeraa.novacore.spigot.abstraction.enums.EquipmentSlot.ALL);
		}

		List<NBTTagCompound> compoundList = new ArrayList<>();
		NBTTagList modifiers = new NBTTagList();
		NBTTagCompound attributeTag = new NBTTagCompound();

		UUID id = UUID.randomUUID();

		attributeTag.set("AttributeName", new NBTTagString(attributeInfo.getAttribute().getPre1_16Key()));
		attributeTag.set("Name", new NBTTagString(attributeInfo.getAttribute().getPre1_16Key()));
		attributeTag.set("Amount", new NBTTagDouble(attributeInfo.getValue()));
		attributeTag.set("Operation", new NBTTagInt(attributeInfo.getOperation().getValue()));
		attributeTag.set("UUIDLeast", new NBTTagInt(((Long) id.getLeastSignificantBits()).intValue()));
		attributeTag.set("UUIDMost", new NBTTagInt(((Long) id.getMostSignificantBits()).intValue()));

		if (!newList.contains(net.zeeraa.novacore.spigot.abstraction.enums.EquipmentSlot.ALL)) {
			for (net.zeeraa.novacore.spigot.abstraction.enums.EquipmentSlot eSlot : newList) {
				NBTTagCompound extra = (NBTTagCompound) attributeTag.clone();
				extra.set("Slot", new NBTTagString(eSlot.getValue()));
				compoundList.add(extra);
			}

		} else {
			compoundList.add(attributeTag);
		}

		for (NBTTagCompound tagCompound : compoundList) {
			modifiers.add(tagCompound);
		}

		NBTTagList attributeModifiers = (NBTTagList) compound.get("AttributeModifiers");
		if (attributeModifiers == null || attributeModifiers.isEmpty()) {
			compound.set("AttributeModifiers", modifiers);
		} else {
			attributeModifiers.add(modifiers);
		}

		nmsItem.setTag(compound);
		ItemStack newItem = CraftItemStack.asBukkitCopy(nmsItem);

		try {
			Class<?> clazz = Class.forName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem");
			Field field = clazz.getDeclaredField("unhandledTags");
			field.setAccessible(true);
			Map<String, NBTBase> map = (Map<String, NBTBase>) field.get(newItem.getItemMeta());
			Map<String, NBTBase> map1 = (Map<String, NBTBase>) field.get(meta);
			if (map1.containsKey("AttributeModifiers")) {
				NBTTagList attributeMods = (NBTTagList) map1.get("AttributeModifiers");
				for (NBTTagCompound tagCompound : compoundList) {
					attributeMods.add(tagCompound);
				}
				map1.put("AttributeModifiers", attributeMods);
			} else {
				map1.putAll(map);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		meta.serialize();

	}

	@SuppressWarnings("deprecation")
	@Override
	public Block getTargetBlockExact(LivingEntity entity, int distance, List<Material> ignore) {
		Location eye = entity.getEyeLocation();
		CraftWorld world = (CraftWorld) entity.getWorld();
		org.bukkit.util.Vector direction = eye.getDirection();
		direction.multiply(distance);
		org.bukkit.util.Vector from = eye.toVector();

		int rayTrace = 1000;
		org.bukkit.util.Vector parcelled = direction.clone().setX(direction.getX() / rayTrace).setY(direction.getY() / rayTrace).setZ(direction.getZ() / rayTrace);
		Block foundBlock = null;

		for (int i = 0; i < rayTrace + 1; i++) {
			Vector current = from.clone();
			current.add(parcelled.clone().multiply(i));
			Material type = world.getBlockAt(current.getBlockX(), current.getBlockY(), current.getBlockZ()).getType();
			if (!ignore.contains(type) && type != Material.AIR) {

				Location blockLoc = new Location(world, current.getX(), current.getY(), current.getZ());
				BlockPosition bPos = new BlockPosition(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ());
				IBlockData data = world.getHandle().getType(bPos);
				net.minecraft.server.v1_12_R1.Block b = data.getBlock();

				List<AxisAlignedBB> bbs = new ArrayList<>();
				AxisAlignedBB cube = new AxisAlignedBB(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ(), blockLoc.getBlockX() + 1, blockLoc.getBlockY() + 1, blockLoc.getBlockZ() + 1);
				b.a(data, world.getHandle(), bPos, cube, bbs, null, true);
				if (bbs.isEmpty()) {
					bbs.add(b.a(data, (IBlockAccess) null, bPos));
				}

				boolean stop = false;
				for (AxisAlignedBB aabb : bbs) {
					if ((current.getX() >= aabb.a && current.getX() <= aabb.d) && (current.getY() >= aabb.b && current.getY() <= aabb.e) && (current.getZ() >= aabb.c && current.getZ() <= aabb.f)) {
						foundBlock = world.getBlockAt(current.getBlockX(), current.getBlockY(), current.getBlockZ());
						stop = true;
						break;
					}
				}
				if (stop)
					break;
			}
		}
		return foundBlock;
	}

	@Override
	public Block getReacheableBlockExact(LivingEntity entity) {
		List<Material> ignore = new ArrayList<>();
		ignore.add(Material.LAVA);
		ignore.add(Material.STATIONARY_LAVA);
		ignore.add(Material.WATER);
		ignore.add(Material.STATIONARY_WATER);
		return getTargetBlockExact(entity, 5, ignore);
	}

	@Override
	public FallingBlock spawnFallingBlock(Location location, Material material, byte data, Consumer<FallingBlock> consumer) {
		@SuppressWarnings("deprecation")
		EntityFallingBlock fb = new EntityFallingBlock(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), CraftMagicNumbers.getBlock(material).fromLegacyData(data));
		fb.ticksLived = 1;
		if (fb.getBukkitEntity() instanceof CraftFallingBlock) {
			CraftFallingBlock cfb = (CraftFallingBlock) fb.getBukkitEntity();
			consumer.accept(cfb);
			((CraftWorld) location.getWorld()).getHandle().addEntity(fb, CreatureSpawnEvent.SpawnReason.CUSTOM);

			return cfb;
		} else {
			throw new IllegalStateException("[VersionIndependentUtils] An unexpected error occurred");
		}
	}

	@Override
	public void setPotionEffect(ItemStack item, ItemMeta meta, PotionEffect effect, boolean color) {
		if (meta instanceof PotionMeta) {
			PotionMeta potMeta = (PotionMeta) meta;
			potMeta.addCustomEffect(effect, true);
			if (color) {
				potMeta.setColor(effect.getColor());
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
	public void displayTotem(Player player) {
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(((CraftPlayer) player).getHandle(), (byte) 35);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void displayCustomTotem(Player player, int cmd) {
		AbstractionLogger.getLogger().error("VersionIndependentUtils", "Current version does not have CustomModelData support.");
	}

	@Override
	public void setMarker(ArmorStand stand, boolean marker) {
		stand.setMarker(marker);
	}

	@Override
	public boolean isMarker(ArmorStand stand) {
		return stand.isMarker();
	}
}