package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

public class BlockLogicLanternRedstone extends BlockLogicLantern {
	protected final boolean isActive;

	public BlockLogicLanternRedstone(Block<?> block, boolean isActive) {
		super(block);
		this.isActive = isActive;
		if (isActive)
			block.withLightEmission(7);
		block.setTicking(true);
	}
	public int tickDelay() {
		return 2;
   	}
	@Override
	protected void place(World world, int x, int y, int z, Direction direction) {
		super.place(world, x, y, z, direction);
		Side side;
		for(int i = 0; i < Side.sides.length; i++)
		{
			side = Side.sides[i];
			world.notifyBlocksOfNeighborChange(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ(), this.id());
		}
   	}
	@Override
   public void onBlockRemoved(World world, int x, int y, int z, int data) {
    	Side side;
    	for(int i = 0; i < Side.sides.length; i++)
		{
			side = Side.sides[i];
			world.notifyBlocksOfNeighborChange(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ(), this.id());
		}
   }
	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(ModBlocks.LANTERN_REDSTONE)};
	}
	public boolean shouldSideEmitSignal(WorldSource worldSource, int x, int y, int z, Side side) {
		Direction direction = getDirection(worldSource.getBlockMetadata(x, y, z));
		return side != direction.getSide();
	}
	public boolean getSignal(WorldSource worldSource, int x, int y, int z, Side side) {
		Direction direction = getDirection(worldSource.getBlockMetadata(x, y, z));
		side = side.getOpposite();
		return this.isActive && (shouldSideEmitSignal(worldSource, x, y, z, side) || side == direction.getSide());
   	}
	protected boolean getInputSignal(World world, int x, int y, int z) {
	    Direction direction = getDirection(world.getBlockMetadata(x, y, z)).getOpposite();
        return world.getSignal(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ(), direction.getSide());
   	}
	public void updateTick(World world, int x, int y, int z, Random rand) {
      boolean isPowered = this.getInputSignal(world, x, y, z);
      if (this.isActive) {
         if (isPowered) {
            world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_REDSTONE_IDLE.id(), world.getBlockMetadata(x, y, z));
         }
      } else if (!isPowered) {
         world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_REDSTONE.id(), world.getBlockMetadata(x, y, z));
      }
   	}
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		world.scheduleBlockUpdate(x, y, z, this.id(), this.tickDelay());
   	}
   	public boolean getDirectSignal(World world, int x, int y, int z, Side side) {
		Direction direction = getDirection(world.getBlockMetadata(x, y, z));
   		return side.getOpposite() == direction.getSide() && this.getSignal(world, x, y, z, side);
   	}

   	public boolean isSignalSource() {
   	   return true;
   	}
	public void animationTick(World world, int x, int y, int z, Random rand) {
		if (!isActive) return;
		double xPos = x + 0.5;
		double yPos = y + 0.5;
		double zPos = z + 0.5;
		world.spawnParticle("reddust", xPos, yPos, zPos, 0.0, 0.0, 0.0, 10);
	}
}
