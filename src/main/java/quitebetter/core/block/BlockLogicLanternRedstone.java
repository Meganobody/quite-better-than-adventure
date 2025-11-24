package quitebetter.core.block;

import net.minecraft.client.sound.SoundEvent;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicPumpkinRedstone;
import net.minecraft.core.block.BlockLogicRepeater;
import net.minecraft.core.block.BlockLogicTorchRedstone;
import net.minecraft.core.block.BlockLogicWireRedstone;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

import org.jetbrains.annotations.NotNull;
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
		if (isActive)
		{
			Side side;
			for(int i = 0; i < Side.sides.length; i++)
			{
				side = Side.sides[i];
				world.notifyBlocksOfNeighborChange(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetY(), this.id());
			}
		}
   	}

   public void onBlockRemoved(World world, int x, int y, int z, int data) {
    	Side side;
    	for(int i = 0; i < Side.sides.length; i++)
		{
			side = Side.sides[i];
			world.notifyBlocksOfNeighborChange(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetY(), this.id());
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
		return this.isActive && shouldSideEmitSignal(worldSource, x, y, z, side);
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
		Side groundSide = getDirection(world.getBlockMetadata(x, y, z)).getOpposite().getSide();
   		return side == groundSide && this.getSignal(world, x, y, z, side);
   	}

   	public boolean isSignalSource() {
   	   return true;
   	}
	@Override
   	protected @Nullable ItemStack putItem(World world, int x, int y, int z, @Nullable ItemStack itemStack, @Nullable Entity entity)
	{
		if (itemStack == null)
		{
			world.playSoundEffect(entity, SoundCategory.WORLD_SOUNDS, x, y, z, "random.pop", 0.1f, 0.5f);
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_EMPTY.id(), world.getBlockMetadata(x, y, z));
			return new ItemStack(Blocks.TORCH_REDSTONE_ACTIVE);
		}
		world.playSoundEffect(entity, SoundCategory.WORLD_SOUNDS, x, y, z, "random.door_close", 0.3f, 0.2f);
		if (itemStack.itemID == Items.INGOT_STEEL_CRUDE.id)
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_REDSTONE_ENCASED.id(), world.getBlockMetadata(x, y, z));
		else
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.LANTERN_SWITCH.id(), world.getBlockMetadata(x, y, z));
		return null;
	}
	@Override
	protected boolean canPutItem(World world, int x, int y, int z, @Nullable ItemStack itemStack) {
		return itemStack == null 
			|| itemStack.itemID == Items.INGOT_STEEL_CRUDE.id 
			|| itemStack.itemID == Items.TOOL_CLOCK.id
		;
	}
	public void animationTick(World world, int x, int y, int z, Random rand) {
		if (!isActive) return;
		double xPos = (double)x + (double)0.5F;
		double yPos = (double)y + 0.5;
		double zPos = (double)z + (double)0.5F;
		world.spawnParticle("reddust", xPos, yPos, zPos, (double)0.0F, (double)0.0F, (double)0.0F, 10);
	}
}
