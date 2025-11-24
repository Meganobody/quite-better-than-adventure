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

public class BlockLogicLanternMushroom extends BlockLogicLantern {

	public BlockLogicLanternMushroom(Block<?> block) {
		super(block);
		block.withLightEmission(15);
	}
	public int getLightEmission()
	{
		return 15;
	}
	public void animationTick(World world, int x, int y, int z, Random rand) {}
	@Override
	protected @Nullable ItemStack putItem(World world, int x, int y, int z, @Nullable ItemStack itemStack, @Nullable Entity entity)
	{
		world.playSoundEffect(entity, SoundCategory.WORLD_SOUNDS, x, y, z, "random.pop", 0.1f, 0.5f);
		world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_EMPTY.id(), world.getBlockMetadata(x, y, z));
		return new ItemStack(ModBlocks.MUSHROOM_GLOWING);
	}
	@Override
	protected boolean canPutItem(World world, int x, int y, int z, @Nullable ItemStack itemStack) {
		return itemStack == null;
	}
}
