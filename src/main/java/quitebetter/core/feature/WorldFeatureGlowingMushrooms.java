package quitebetter.core.feature;

import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import quitebetter.core.block.ModBlocks;
import net.minecraft.core.world.World;

import java.util.*;

public class WorldFeatureGlowingMushrooms {

	private static final Biome[] allowedBiomes = {
		Biomes.OVERWORLD_BIRCH_FOREST,
		Biomes.OVERWORLD_FOREST,
		Biomes.OVERWORLD_PLAINS,
		Biomes.OVERWORLD_SEASONAL_FOREST,
		Biomes.OVERWORLD_RETRO,
		Biomes.OVERWORLD_SWAMPLAND,
	};

	public static void PlaceNearTree(World world, Random random, int x, int y, int z, Biome biome) {
		if (Arrays.asList(allowedBiomes).contains(biome) && random.nextInt(8)==0) {
			for(int l = 0; l < 10; ++l) {
				int i1 = x + random.nextInt(8) - random.nextInt(8);
				int j1 = y + random.nextInt(4) - random.nextInt(4);
				int k1 = z + random.nextInt(8) - random.nextInt(8);
				if (world.isAirBlock(i1, j1, k1)) {
					int l1 = 1 + random.nextInt(random.nextInt(3) + 1);

					for(int i2 = 0; i2 < l1; ++i2) {
						if ((world.getBlock(x,y,z)==null || world.getBlock(x,y,z).hasTag(BlockTags.PLACE_OVERWRITES)) && ModBlocks.MUSHROOM_GLOWING.canBlockStay(world, i1, j1 + i2, k1)) {
							world.setBlock(i1, j1 + i2, k1, ModBlocks.MUSHROOM_GLOWING.id());
						}
					}
				}
			}
		}
	}
}
