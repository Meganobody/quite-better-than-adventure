package quitebetter.core.block;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

public class BlockLogicLanternRedstoneEncased extends BlockLogicLanternRedstone {
    public BlockLogicLanternRedstoneEncased(Block<?> block, boolean isActive) {
		super(block, isActive);
		block.setTicking(true);
	}
    @Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(ModBlocks.LANTERN_REDSTONE_ENCASED)};
	}
    @Override
    public boolean shouldSideEmitSignal(WorldSource worldSource, int x, int y, int z, Side side) {
		Direction direction = getDirection(worldSource.getBlockMetadata(x, y, z));
		return side == direction.getOpposite().getSide();
	}
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
      boolean isPowered = this.getInputSignal(world, x, y, z);
      if (this.isActive) {
         if (isPowered) {
            world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_REDSTONE_ENCASED_IDLE.id(), world.getBlockMetadata(x, y, z));
         }
      } else if (!isPowered) {
         world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_REDSTONE_ENCASED.id(), world.getBlockMetadata(x, y, z));
      }
   	}
    @Override
   	protected @Nullable ItemStack putItem(World world, int x, int y, int z, @Nullable ItemStack itemStack, @Nullable Entity entity)
    {
        return null;
    }
    @Override
	protected boolean canPutItem(World world, int x, int y, int z, @Nullable ItemStack itemStack) {
		return false;
	}
}
