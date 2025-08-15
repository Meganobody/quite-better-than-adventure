package quitebetter.core.util;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.PacketAddParticle;
import net.minecraft.core.net.packet.PacketChat;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import turniplabs.halplibe.helper.EnvironmentHelper;
import static quitebetter.core.ModCore.LOGGER;

public class BaseUtil {
	public static void spawnParticle(World world, String particleKey, double x, double y, double z, double motionX, double motionY, double motionZ, int data) {
		if (EnvironmentHelper.isServerEnvironment()) {
			world.spawnParticle(particleKey, x, y, z, motionX, motionY, motionZ, data);
			ServerUtil.getPlayerList().sendPacketToAllPlayersInDimension(new PacketAddParticle(particleKey, x, y, z, motionX, motionY, motionZ, data),world.dimension.id);
		} else {
			world.spawnParticle(particleKey, x, y, z, motionX, motionY, motionZ, data);
		}
	}
}
