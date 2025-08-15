package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockLogicLanternMushroom extends BlockLogicLanternCoal {

	public BlockLogicLanternMushroom(Block<?> block, Material material) {
		super(block, material);
	}

	public void animationTick(World world, int x, int y, int z, Random rand) {}
}
