package quitebetter.core.util;

import net.minecraft.core.world.World;

public class WorldUtil {
	public static int getOceanLevel(World world) {
		return world.getWorldType().getOceanY();
	}
}
