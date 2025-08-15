package quitebetter.core.feature;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.net.command.util.CommandHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.BiomeOutback;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;

import java.util.Arrays;
import java.util.Random;

public class ModFeatures {
	public static int WATER_LEVEL = 100;

	public static boolean TreeGeneration(World world, Random random, int x, int y, int z, Class<?> treeclass) {
		WorldFeatureGlowingMushrooms.PlaceNearTree(world, random, x, y, z, world.getBlockBiome(x,y,z));
		return true;
	}

	public static void Setup() {
		CommandHelper.registerWorldFeatureClass(WorldFeatureMaze.class);
	}

	public static void ChunkDecoration(World world, Chunk chunk, int chunkX, int chunkZ, Random ChunkRandom, int x, int y, int z, Random random) {
		Biome biome = world.getBlockBiome(x,y,z);
		if (Arrays.asList(WorldFeatureMaze.allowedBiomes).contains(biome)) {
			if (random.nextInt(65)==0) {
				int topBlock = world.getHeightValue(x, z);
				if (topBlock > WATER_LEVEL+5) {
					(new WorldFeatureMaze()).place(world, ChunkRandom, x, topBlock, z);
				}
			}
		}
	}
}
