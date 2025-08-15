package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockLogicLanternRedstone extends BlockLogicLanternCoal {

	public BlockLogicLanternRedstone(Block<?> block, Material material) {
		super(block, material);
	}

	public void animationTick(World world, int x, int y, int z, Random rand) {
		double xPos = (double)x + (double)0.5F;
		double yPos = (double)y + 0.5;
		double zPos = (double)z + (double)0.5F;
		world.spawnParticle("reddust", xPos, yPos, zPos, (double)0.0F, (double)0.0F, (double)0.0F, 10);
	}

	public boolean getSignal(WorldSource worldSource, int x, int y, int z, Side side) {
		return true;
	}

	public boolean isSignalSource() {
		return true;
	}
}
