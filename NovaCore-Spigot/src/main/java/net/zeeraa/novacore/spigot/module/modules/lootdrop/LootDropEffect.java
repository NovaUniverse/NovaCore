package net.zeeraa.novacore.spigot.module.modules.lootdrop;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.commons.utils.MathUtil;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;
import net.zeeraa.novacore.spigot.module.ModuleManager;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class LootDropEffect implements Runnable {
	private Location location;
	private String lootTable;

	private boolean completed;

	private Task task;
	private int ticksLeft;

	private int startTime;

	private Map<Location, Material> removedBlocks;

	public LootDropEffect(Location location, String lootTable) {
		location.setX(location.getBlockX() + 0.5);
		location.setY(location.getBlockY());
		location.setZ(location.getBlockZ() + 0.5);

		this.completed = false;

		this.lootTable = lootTable;

		this.removedBlocks = new HashMap<Location, Material>();
		this.location = location;

		LootDropManager module = (LootDropManager) ModuleManager.getModule(LootDropManager.class);
		startTime = module == null ? 20 * 2 : module.getDefaultSpawnTimeTicks();
		this.ticksLeft = startTime;

		this.task = new SimpleTask(NovaCore.getInstance(), this, 0L);
		Task.tryStartTask(task);

		for (int x = (int) location.getBlockX() - 1; x <= location.getBlockX() + 1; x++) {
			for (int z = location.getBlockZ() - 1; z <= location.getBlockZ() + 1; z++) {
				Location l = new Location(location.getWorld(), x, location.getBlockY() - 2, z);

				removedBlocks.put(l, l.getWorld().getBlockAt(l).getType());

				l.getBlock().setType(Material.IRON_BLOCK);
			}
		}

		Location beaconLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());

		removedBlocks.put(beaconLocation, beaconLocation.getWorld().getBlockAt(beaconLocation).getType());

		beaconLocation.getBlock().setType(Material.BEACON);
	}

	@Override
	public void run() {
		if (ticksLeft <= 0) {
			cancelTask();

			Firework fw = (Firework) this.location.getWorld().spawnEntity(this.location, EntityType.FIREWORK);
			FireworkMeta fwm = fw.getFireworkMeta();

			fwm.setPower(1);
			fwm.addEffect(FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.ORANGE).trail(true).flicker(true).build());

			fw.setFireworkMeta(fwm);

			Bukkit.getScheduler().scheduleSyncDelayedTask(NovaCore.getInstance(), new Runnable() {
				@Override
				public void run() {
					fw.detonate();
				}
			}, 2L);

			animationCompleted();
			return;
		}

		if (ticksLeft % 4 == 0) {
			Location fireworkLocation = getFireworkLocation();

			Firework fw = (Firework) fireworkLocation.getWorld().spawnEntity(fireworkLocation, EntityType.FIREWORK);
			FireworkMeta fwm = fw.getFireworkMeta();

			fwm.setPower(1);
			fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).trail(true).build());

			fw.setFireworkMeta(fwm);

			Bukkit.getScheduler().scheduleSyncDelayedTask(NovaCore.getInstance(), new Runnable() {
				@Override
				public void run() {
					fw.detonate();
				}
			}, 2L);
		}

		ticksLeft--;
	}

	public void cancelTask() {
		Task.tryStopTask(task);
	}

	public void stop() {
		Task.tryStopTask(task);
		undoBlocks();
		completed = true;
	}

	public void undoBlocks() {
		for (Location l : removedBlocks.keySet()) {
			l.getBlock().setType(removedBlocks.get(l));
		}
		removedBlocks.clear();

		LootDropManager.getInstance().spawnChest(location, lootTable);

		VersionIndependentSound.ANVIL_LAND.playAtLocation(location, 1F, 1F);
	}

	private void animationCompleted() {
		undoBlocks();
		completed = true;
	}

	public boolean isCompleted() {
		return completed;
	}

	public Location getLocation() {
		return location;
	}

	public Location getFireworkLocation() {
		double progress = ((double) ticksLeft) / ((double) startTime);
		double y = location.getBlockY();
		double maxHeight = location.getWorld().getMaxHeight();
		
		double offset = MathUtil.lerp(maxHeight, y, progress);

		return new Location(location.getWorld(), location.getX(), (double) 2 + offset, location.getZ());
	}

	public World getWorld() {
		return location.getWorld();
	}

	public Map<Location, Material> getRemovedBlocks() {
		return removedBlocks;
	}
}