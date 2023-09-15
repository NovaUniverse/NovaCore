package net.zeeraa.novacore.spigot.version.v1_12_R1;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_12_R1.ScoreboardObjective;
import net.minecraft.server.v1_12_R1.ScoreboardScore;
import net.zeeraa.novacore.commons.utils.ReflectUtils;
import net.zeeraa.novacore.spigot.abstraction.INetheriteBoard;
import net.zeeraa.novacore.spigot.abstraction.enums.ObjectiveMode;
import net.zeeraa.novacore.spigot.abstraction.netheriteboard.BPlayerBoard;

public class PlayerBoardV1_12_R1 extends BPlayerBoard {
	private EntityPlayer playerHandle;
	private Field playerScoreField;

	public PlayerBoardV1_12_R1(INetheriteBoard netheriteBoard, Player player, String name) throws Exception {
		this(netheriteBoard, player, null, name);
	}

	public PlayerBoardV1_12_R1(INetheriteBoard netheriteBoard, Player player, Scoreboard scoreboard, String name) throws Exception {
		super(netheriteBoard, player, scoreboard, name);
		playerHandle = ((CraftPlayer) player).getHandle();

		playerScoreField = net.minecraft.server.v1_12_R1.Scoreboard.class.getDeclaredField("playerScores");
		playerScoreField.setAccessible(true);
		
		this.init();
	}

	@Override
	protected void sendObjective(Objective objective, ObjectiveMode mode) throws Exception {
		ScoreboardObjective nmsObjective = (ScoreboardObjective) ReflectUtils.getHandle(objective);
		PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(nmsObjective, mode.ordinal());
		playerHandle.playerConnection.sendPacket(packet);
	}

	@Override
	protected void sendObjectiveDisplay(Objective objective) throws Exception {
		ScoreboardObjective nmsObjective = (ScoreboardObjective) ReflectUtils.getHandle(objective);
		PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective(1, nmsObjective);
		playerHandle.playerConnection.sendPacket(packet);
	}

	@Override
	protected void sendScore(Objective objective, String name, int score, boolean remove) throws Exception {
		net.minecraft.server.v1_12_R1.Scoreboard nmsScoreboard = ((CraftScoreboard) this.scoreboard).getHandle();
		ScoreboardObjective nmsObjective = (ScoreboardObjective) ReflectUtils.getHandle(objective);

		ScoreboardScore scoreboardScore = new ScoreboardScore(nmsScoreboard, nmsObjective, name);
		scoreboardScore.setScore(score);

		@SuppressWarnings("unchecked")
		Map<String, Map<ScoreboardObjective, ScoreboardScore>> scores = (Map<String, Map<ScoreboardObjective, ScoreboardScore>>) playerScoreField.get(nmsScoreboard);

		if (remove) {
			if (scores.containsKey(name)) {
				scores.get(name).remove(nmsObjective);
			}
		} else {
			if (!scores.containsKey(name)) {
				scores.put(name, new HashMap<>());
			}

			scores.get(name).put(nmsObjective, scoreboardScore);
		}

		@SuppressWarnings("rawtypes")
		Packet packet;

		if (remove) {
			packet = new PacketPlayOutScoreboardScore(name, nmsObjective);
		} else {
			packet = new PacketPlayOutScoreboardScore(scoreboardScore);
		}

		playerHandle.playerConnection.sendPacket(packet);
	}
}