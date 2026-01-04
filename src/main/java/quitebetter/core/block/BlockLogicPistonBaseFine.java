package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.piston.BlockLogicPistonBase;
import net.minecraft.core.block.piston.BlockLogicPistonHead;
import net.minecraft.core.block.piston.BlockLogicPistonMoving;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import static quitebetter.core.util.ItemUtil.putItemStack;
import static quitebetter.core.util.ItemUtil.takeItemStack;

public class BlockLogicPistonBaseFine extends BlockLogicPistonBase {
	public static final int TYPE_FINE = 10;
	public BlockLogicPistonBaseFine(Block<?> block, int maxPushedBlocks) {
		super(block, maxPushedBlocks);
	}
	public void createPistonHeadAt(World world, int x, int y, int z, int data, Direction direction) {
		world.setBlockAndMetadata(x, y, z, Blocks.PISTON_MOVING.id(), BlockLogicPistonHead.setPistonType(TYPE_FINE, direction.getId()));
		world.replaceBlockTileEntity(x, y, z, BlockLogicPistonMoving.createTileEntity(ModBlocks.PISTON_HEAD_FINE.id(), BlockLogicPistonHead.setPistonType(TYPE_FINE, direction.getId()), null, direction, true, false));
	}
	public void extendEvent(World world, int x, int y, int z, int data, Direction direction) {
		final int outX = x + direction.getOffsetX() * 2,
			outY = y + direction.getOffsetY() * 2,
			outZ = z + direction.getOffsetZ() * 2;
		Direction opDirection = direction.getOpposite();
		final int inX = x + opDirection.getOffsetX(),
			inY = y + opDirection.getOffsetY(),
			inZ = z + opDirection.getOffsetZ();
		putItemStack(world, outX, outY, outZ, takeItemStack(world, inX, inY, inZ));
		super.extendEvent(world, x, y, z, data, direction);
	}


}
