package quitebetter.core.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.net.PlayerList;
import turniplabs.halplibe.helper.EnvironmentHelper;

@Environment(EnvType.SERVER)
public class ServerUtil {
	public static PlayerList getPlayerList() {
		if (EnvironmentHelper.isServerEnvironment()) {
			return MinecraftServer.getInstance().playerList;
		}
		return null;
	}

	public static void sendToWorldPlayers(World world, Packet packet) {
		if (EnvironmentHelper.isServerEnvironment()) {
			assert ServerUtil.getPlayerList()!=null;
			ServerUtil.getPlayerList().sendPacketToAllPlayersInDimension(packet, world.dimension.id);
		}
	}

	public static void sendToAllPlayers(Packet packet) {
		if (EnvironmentHelper.isServerEnvironment()) {
			assert ServerUtil.getPlayerList()!=null;
			ServerUtil.getPlayerList().sendPacketToAllPlayers(packet);
		}
	}

	public static void markBlockNeedsUpdate(World world, int x, int y, int z) {
		if (EnvironmentHelper.isServerEnvironment()) {
			assert ServerUtil.getPlayerList()!=null;
			ServerUtil.getPlayerList().markBlockNeedsUpdate(x,y,z,world.dimension.id);
		}
	}
}
