package net.zeeraa.novacore.spigot.module.modules.lootdrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.loottable.LootTable;

/**
 * Represents a loot drop
 * 
 * @author Zeeraa
 */
public class LootDrop {
	private Location location;
	private Inventory inventory;
	private UUID uuid;
	private String lootTable;
	private boolean removed;
	private LootTable table;
	private List<ItemStack> generatedLoot;
	public LootDrop(Location location, String lootTable) {
		this.removed = false;
		this.uuid = UUID.randomUUID();
		this.location = location;
		this.inventory = Bukkit.createInventory(new LootDropInventoryHolder(uuid), 27, "Loot drop");

		this.lootTable = lootTable;
		this.generatedLoot = new ArrayList<>();

		this.fill();

		NovaCore.getInstance().getVersionIndependentUtils().setBlockAsPlayerSkull(this.location.getBlock());

		NBTEditor.setSkullTexture(this.location.getBlock(), "http://textures.minecraft.net/texture/2bdd62f25f4a49cc42e054a3f212c3e0092138299172d7d8f3d438214ca972ac");
	}

	public void fill() {
		table = NovaCore.getInstance().getLootTableManager().getLootTable(lootTable);

		if (table == null) {
			Log.warn("LootDrop", "No loot table named " + lootTable + " was found");
			return;
		}

		List<ItemStack> loot = table.generateLoot();
		this.generatedLoot.addAll(loot);
		inventory.clear();

		while (loot.size() > inventory.getSize()) {
			loot.remove(0);
		}

		while (loot.size() > 0) {
			Random random = new Random();

			int slot = random.nextInt(inventory.getSize());

			if (inventory.getItem(slot) == null) {
				ItemStack item = loot.remove(0);
				inventory.setItem(slot, item);
			}
		}
	}

	public boolean isRemoved() {
		return removed;
	}

	public void scheduleRemove() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(NovaCore.getInstance(), () -> remove(true), 80L);
	}

	public void remove() {
		this.remove(false);
	}

	public void remove(boolean dropItems) {
		removed = true;

		this.location.getBlock().setType(Material.AIR);

		this.location.getWorld().getPlayers().forEach(player -> {
			if (player.getOpenInventory() != null) {
				if (player.getOpenInventory().getTopInventory() != null) {
					if (player.getOpenInventory().getTopInventory().getHolder() instanceof LootDropInventoryHolder) {
						if (((LootDropInventoryHolder) player.getOpenInventory().getTopInventory().getHolder()).getUuid() == this.uuid) {
							player.closeInventory();
						}
					}
				}
			}
		});
		if (dropItems) {
			if (table != null) {
				if (!generatedLoot.isEmpty()) {
					for (ItemStack item : generatedLoot) {
						location.getWorld().dropItemNaturally(location, item);
					}
				} else {
					for (ItemStack item : table.generateLoot()) {
						location.getWorld().dropItemNaturally(location, item);
					}
				}
			} else {
				LootTable table =  NovaCore.getInstance().getLootTableManager().getLootTable(lootTable);
				for (ItemStack item : table.generateLoot()) {
					location.getWorld().dropItemNaturally(location, item);
				}
			}
		}

		for (ItemStack i : this.inventory.getContents()) {
			if (i == null) {
				continue;
			}

			if (i.getType() == Material.AIR) {
				continue;
			}

			this.location.getWorld().dropItem(this.location, i);
		}

		this.inventory.clear();
	}

	public UUID getUuid() {
		return uuid;
	}

	public Location getLocation() {
		return location;
	}

	public World getWorld() {
		return this.location.getWorld();
	}

	public Inventory getInventory() {
		return inventory;
	}
}