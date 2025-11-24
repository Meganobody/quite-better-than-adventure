package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

public class BlockLogicLanternEmpty extends BlockLogicLantern {

	public BlockLogicLanternEmpty(Block<?> block) {
		super(block);
	}

	public void animationTick(World world, int x, int y, int z, Random rand) {}
	protected boolean canPutItem(World world, int x, int y, int z, @Nullable ItemStack itemStack)
	{
		return itemStack != null &&
		(itemStack.itemID == Blocks.TORCH_COAL.id() 
		|| itemStack.itemID == ModBlocks.MUSHROOM_GLOWING.id() 
		|| itemStack.itemID == Blocks.TORCH_REDSTONE_ACTIVE.id());
	}
	protected @Nullable ItemStack putItem(World world, int x, int y, int z, @Nullable ItemStack itemStack, @Nullable Entity entity)
	{
		if (itemStack == null)
			return null;
		if (itemStack.itemID == Blocks.TORCH_COAL.id())
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_COAL.id(), world.getBlockMetadata(x, y, z));
		else if (itemStack.itemID == ModBlocks.MUSHROOM_GLOWING.id())
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_MUSHROOM.id(), world.getBlockMetadata(x, y, z));
		else if (itemStack.itemID == Blocks.TORCH_REDSTONE_ACTIVE.id())
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_REDSTONE.id(), world.getBlockMetadata(x, y, z));
		else
			return null;
		world.playSoundEffect(entity, SoundCategory.WORLD_SOUNDS, x, y, z, "random.pop", 0.1f, 2f);
		return null;
	}
}
