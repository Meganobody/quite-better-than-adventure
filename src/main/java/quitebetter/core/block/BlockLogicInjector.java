package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import quitebetter.core.tileentity.TileEntityFetcher;
import quitebetter.core.util.ItemUtil;

import java.util.Random;

public class BlockLogicInjector extends BlockLogicMachine {
	public BlockLogicInjector(Block<?> block) {
		super(block);
	}
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		super.updateTick(world, x, y, z, rand);
		Direction direction = metaToDirection(world.getBlockMetadata(x, y, z));
		Direction opDirection = direction.getOpposite();
		final int 	outX = x + opDirection.getOffsetX(),
					outY = y + opDirection.getOffsetY(),
					outZ = z + opDirection.getOffsetZ();
		final int 	inX = x + direction.getOffsetX(),
					inY = y + direction.getOffsetY(),
					inZ = z + direction.getOffsetZ();
		ItemStack itemStack = ItemUtil.takeItemStack(world, inX, inY, inZ);
		if (itemStack == null)
		{
			world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.3f, 1.5F);
			return;
		}
		world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.pop", 0.3f, 1);
		ItemUtil.putItemStack(world, outX, outY, outZ, itemStack);
	}
}
