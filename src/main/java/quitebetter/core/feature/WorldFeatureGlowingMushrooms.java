package quitebetter.core.feature;

import quitebetter.core.block.ModBlocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;

import java.util.Random;

public class WorldFeatureGlowingMushrooms extends WorldFeature {
	public boolean place(World world, Random random, int x, int y, int z) {
		for(int l = 0; l < 10; ++l) {
			int i1 = x + random.nextInt(8) - random.nextInt(8);
			int j1 = y + random.nextInt(4) - random.nextInt(4);
			int k1 = z + random.nextInt(8) - random.nextInt(8);
			if (world.isAirBlock(i1, j1, k1)) {
				int l1 = 1 + random.nextInt(random.nextInt(3) + 1);

				for(int i2 = 0; i2 < l1; ++i2) {
					if (ModBlocks.MUSHROOM_GLOWING.canBlockStay(world, i1, j1 + i2, k1)) {
						world.setBlock(i1, j1 + i2, k1, ModBlocks.MUSHROOM_GLOWING.id());
					}
				}
			}
		}

		return true;
	}
}
