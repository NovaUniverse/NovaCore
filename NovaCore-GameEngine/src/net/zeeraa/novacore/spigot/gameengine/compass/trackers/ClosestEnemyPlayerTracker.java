package net.zeeraa.novacore.spigot.gameengine.compass.trackers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.module.modules.compass.CompassTarget;
import net.zeeraa.novacore.spigot.module.modules.compass.CompassTrackerTarget;
import net.zeeraa.novacore.spigot.teams.Team;
import net.zeeraa.novacore.spigot.teams.TeamManager;

public class ClosestEnemyPlayerTracker implements CompassTrackerTarget {
	@Override
	public CompassTarget getCompassTarget(Player player) {
		if (GameManager.getInstance().hasActiveGame()) {
			List<UUID> players = new ArrayList<>(GameManager.getInstance().getActiveGame().getPlayers());

			// Ignore self
			players.remove(player.getUniqueId());

			double closestDistance = Double.MAX_VALUE;
			CompassTarget result = null;

			Team team = null;
			if (NovaCore.getInstance().hasTeamManager()) {
				team = TeamManager.getTeamManager().getPlayerTeam(player);
			}

			for (UUID uuid : players) {
				Player p = Bukkit.getServer().getPlayer(uuid);

				if (p != null) {
					if (p.isOnline()) {
						if (GameManager.getInstance().hasActiveGame()) {
							if (!GameManager.getInstance().getActiveGame().getPlayers().contains(p.getUniqueId())) {
								continue;
							}
						}
						if (p.getLocation().getWorld() == player.getLocation().getWorld()) {
							if (team != null) {
								Team p2team = NovaCore.getInstance().getTeamManager().getPlayerTeam(p);

								if (p2team != null) {
									if (team.equals(p2team)) {
										continue;
									}
								}
							}

							double dist = player.getLocation().distance(p.getLocation());

							if (dist < closestDistance) {
								closestDistance = dist;
								result = new CompassTarget(p.getLocation(), ChatColor.GREEN + "Tracking player " + ChatColor.AQUA + p.getName());
							}
						}
					}
				}
			}

			return result;
		}
		return null;
	}
}