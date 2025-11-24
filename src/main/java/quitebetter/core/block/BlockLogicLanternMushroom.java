package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;


	public BlockLogicLanternMushroom(Block<?> block) {
		super(block);
	}
	public int getLightEmission()
	{
		return 15;
	}
	public void animationTick(World world, int x, int y, int z, Random rand) {}
	@Override
}
