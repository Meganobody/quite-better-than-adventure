package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFence;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.BlockLogicLadder;
import net.minecraft.core.block.BlockLogicRope;
import net.minecraft.core.block.BlockLogicTorch;
import net.minecraft.core.block.BlockLogicVeryRotatable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.data.gamerule.GameRule;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import quitebetter.core.util.ILeftClickable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class BlockLogicLantern extends BlockLogicFullyRotatable 
	implements ILeftClickable 
{
	public static final int MASK_DIRECTION = 7;
	public BlockLogicLantern(Block<?> block) {
		super(block, Material.decoration);
		//this.setBlockBounds(0.2F,0F,0.2F,0.8F,1F,0.8F);
	}
	private final double VERTICAL_MIN = 0; 
	private final double VERTICAL_MAX = 1;
	private final double HORIZONTAL_AVG = 0.5;
	private final double HORIZONTAL_AMP = 0.3;
	public static Direction getDirection(int data) {
		return Direction.getDirectionById(data & MASK_DIRECTION);
	}
	public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        double minX, maxX, minY, maxY, minZ, maxZ;
		Direction direction = getDirection(world.getBlockMetadata(x, y, z));
		switch (direction) {
			case NORTH:
			case SOUTH:
				minX = HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxX = HORIZONTAL_AVG + HORIZONTAL_AMP;
				minZ = VERTICAL_MIN;
				maxZ = VERTICAL_MAX;
				minY = HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxY = HORIZONTAL_AVG + HORIZONTAL_AMP;
				break;
			case WEST:
			case EAST:
				minY = HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxY = HORIZONTAL_AVG + HORIZONTAL_AMP;
				minX = VERTICAL_MIN;
				maxX = VERTICAL_MAX;
				minZ = HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxZ = HORIZONTAL_AVG + HORIZONTAL_AMP;
				break;
			case DOWN:
			default:
				minX = HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxX = HORIZONTAL_AVG + HORIZONTAL_AMP;
				minY = VERTICAL_MIN;
				maxY = VERTICAL_MAX;
				minZ = HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxZ = HORIZONTAL_AVG + HORIZONTAL_AMP;
				break;
		}
        return AABB.getTemporaryBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
	public boolean isSolidRender() {
		return false;
	}

	public boolean isCubeShaped() { 
		return false;
	}
	protected static boolean isValidDirection(World world, int x, int y, int z, Direction direction) {
		if (direction == Direction.NONE)
			return false;
		Side bottomSide = direction.getSide().getOpposite();
		x += bottomSide.getOffsetX();
		y += bottomSide.getOffsetY();
		z += bottomSide.getOffsetZ();
		Block<?> block = world.getBlock(x, y, z);
		if (block == null)
			return false;
      	BlockLogic logic = block.getLogic();
		if (logic == null) 
			return false;
      	return world.canPlaceOnSurfaceOfBlock(x, y, z)
		 || (block.isCubeShaped() || (logic instanceof BlockLogicLantern && getDirection(world.getBlockMetadata(x, y, z)) == direction) || (logic instanceof BlockLogicFence && direction.isVertical()));
    }
	public Direction getFirstValidDirection(World world, int x, int y, int z) {
		Direction direction;
		for (int i = 0; i < Direction.directions.length; i++) {
			direction = Direction.directions[i];
			if (isValidDirection(world, x, y, z, direction))
				return direction;
		}
	  	return Direction.NONE;
   	}
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
	  	return getFirstValidDirection(world, x, y, z) != Direction.NONE;
   }
	public boolean canBlockStay(World world, int x, int y, int z) {
		Direction direction = getDirection(world.getBlockMetadata(x, y, z) & MASK_DIRECTION);
		return isValidDirection(world, x, y, z, direction);
	}
	protected void dropLantern(World world, int x, int y, int z) {
		this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), (TileEntity)null, (Player)null);
        world.setBlockWithNotify(x, y, z, 0);
	}
	protected boolean dropLanternIfCantPlace(World world, int x, int y, int z) {
      if (!this.canPlaceBlockAt(world, x, y, z)) {
         dropLantern(world, x, y, z);
         return true;
      }
	  return false;
    }
	protected boolean dropLanternIfCantStay(World world, int x, int y, int z) {
      if (!this.canBlockStay(world, x, y, z)) {
         dropLantern(world, x, y, z);
         return true;
      }
	  return false;
    }
	protected void place(World world, int x, int y, int z, Direction direction)
	{
		if (!isValidDirection(world, x, y, z, direction))
		{
			direction = getFirstValidDirection(world, x, y, z);
			if (direction == Direction.NONE)
			{
				dropLantern(world, x, y, z);
				return;
			}
		}
      	world.setBlockMetadataWithNotify(x, y, z, direction.getId());
      	this.dropLanternIfCantStay(world, x, y, z);
	}
	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		place(world, x, y, z, side.getDirection());
    }
	public void onBlockPlacedByWorld(World world, int x, int y, int z) {
		place(world, x, y, z, getDirection(world.getBlockMetadata(x, y, z)));
	}
	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		place(world, x, y, z, side.getDirection());
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		dropLanternIfCantStay(world, x, y, z);
	}

	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this.block)};
	}

	public int getPistonPushReaction(World world, int x, int y, int z) {
		return Material.PISTON_DESTROY_ON_PUSH;
	}
	protected abstract boolean canPutItem(World world, int x, int y, int z, @Nullable ItemStack itemStack);
	protected abstract @Nullable ItemStack putItem(World world, int x, int y, int z, @Nullable ItemStack itemStack, @Nullable Entity entity);

   	public void onActivatorInteract(World world, int x, int y, int z, TileEntityActivator activator, Direction direction)
	{
		ItemStack inputStack = activator.getItem(activator.stackSelector);
		if (!canPutItem(world, x, y, z, inputStack))
			return;
		ItemStack outputStack = putItem(world, x, y, z, inputStack, null);
		
		if (inputStack != null)
		{
			activator.removeItem(activator.stackSelector, 1);
		}
		if (outputStack != null)
		{
			if (inputStack == null || inputStack.stackSize == 0)
				activator.setItem(activator.stackSelector, outputStack);
			else if (inputStack.canStackWith(outputStack))
				activator.pickup(world, null);
			else
				world.dropItem(x, y, z, outputStack);
		}
   	}
	@Override
	public boolean isClimbable(World world, int x, int y, int z) {
		return getDirection(world.getBlockMetadata(x, y, z)).isVertical();
   	}
	@Override
	public void onBlockLeftClicked(World world, int x, int y, int z, Player player, Side side) {
		if (!player.isSneaking()) return;
		ItemStack inputStack = player.getHeldItem();
		if (!canPutItem(world, x, y, z, inputStack))
			return;
		@Nullable ItemStack outputStack = putItem(world, x, y, z, inputStack, null);
		
		if (player.gamemode != Gamemode.creative && inputStack != null)
		{
			inputStack.stackSize--;
		}
		if (player.gamemode == Gamemode.creative && outputStack != null && player.hasItem(outputStack.getItem())) return;
		if (outputStack != null)
		{
			if (inputStack == null || inputStack.stackSize == 0)
				player.inventory.setHeldItemStack(outputStack);
			else
				world.dropItem(x, y, z, outputStack);
		}
	}
	@Override
	public boolean preventsBreaking(World world, int x, int y, int z, Player player, Side side) {
		return player.isSneaking() && canPutItem(world, x, y, z, player.getHeldItem());// && canPutItem(world, x, y, z, player.getHeldItem());
	}
}