package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockLogicLanternCoal extends BlockLogicLantern {
	public BlockLogicLanternCoal(Block<?> block) {
		super(block);
		block.withLightEmission(15);
	}

	public void animationTick(World world, int x, int y, int z, Random rand) {
		double xPos = (double)x + (double)0.5F;
		double yPos = (double)y + 0.6;
		double zPos = (double)z + (double)0.5F;
		world.spawnParticle("smoke", xPos, yPos, zPos, (double)0.0F, (double)0.0F, (double)0.0F, 0);
		world.spawnParticle("flame", xPos, yPos, zPos, (double)0.0F, (double)0.0F, (double)0.0F, 0);
	}
}
