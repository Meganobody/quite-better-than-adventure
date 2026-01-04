package quitebetter.core.block;

import net.fabricmc.loader.impl.lib.tinyremapper.extension.mixin.common.Logger;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicRepeater;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import quitebetter.core.item.ModItems;

import java.util.Random;

public class BlockLogicDelayer extends BlockLogic {
	private static final int defaultTickDelay = 1;
	//private static final int[] tickDelayMap = new int[]{-8, -4, -2, -1, 1, 2, 4, 8};
	//public static final double[] torchPosOffset = new double[]{0.3125, 0.25, 0.125, 0.0625, -0.0625, -0.125, -0.25, -0.3125};
	private static final int[] tickDelayMap = new int[]{4, 1, -1, -4};
	public static final double[] torchPosOffset = new double[]{-0.3125, -0.125, 0.125, 0.3125};
	public static final int MASK_DIRECTION = 0b00011;
	public static final int MASK_TICK_DELAY = 0b01100;
	public final boolean isRepeaterPowered;

	public BlockLogicDelayer(Block<?> block, boolean flag) {
		super(block, Material.decoration);
		this.isRepeaterPowered = flag;
		this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
	}

	public boolean isCubeShaped() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.canPlaceOnSurfaceOfBlock(x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z);
	}

	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(ModItems.DELAYER)};
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.canPlaceOnSurfaceOfBlock(x, y - 1, z) && super.canBlockStay(world, x, y, z);
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		int meta = world.getBlockMetadata(x, y, z);
		boolean flag = this.isGettingPower(world, x, y, z, meta);
		if (this.isRepeaterPowered && !flag) {
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.DELAYER_IDLE.id(), meta);
		} else if (!this.isRepeaterPowered) {
			world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.DELAYER_ACTIVE.id(), meta);
			if (!flag) {
				int i1 = (meta & MASK_TICK_DELAY) >> 2;
				world.scheduleBlockUpdate(x, y, z, ModBlocks.DELAYER_ACTIVE.id(), tickDelayMap[i1] * 2);
			}
		}

	}

	public boolean getDirectSignal(World world, int x, int y, int z, Side side) {
		return this.getSignal(world, x, y, z, side);
	}

	public boolean getSignal(WorldSource worldSource, int x, int y, int z, Side side) {
		if (!this.isRepeaterPowered) {
			return false;
		} else {
			int meta = worldSource.getBlockMetadata(x, y, z) & MASK_DIRECTION;
			if (meta == 0 && side == Side.SOUTH) {
				return true;
			} else if (meta == 1 && side == Side.WEST) {
				return true;
			} else if (meta == 2 && side == Side.NORTH) {
				return true;
			} else {
				return meta == 3 && side == Side.EAST;
			}
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), null, null);
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			int meta = world.getBlockMetadata(x, y, z);
			boolean flag = this.isGettingPower(world, x, y, z, meta);
			int tickDelayIndex = (meta & MASK_TICK_DELAY) >> 2;
			int tickDelay = Math.abs(tickDelayMap[tickDelayIndex]) * 2;
			boolean extendSignal = tickDelayMap[tickDelayIndex] >= 0;
			if (!flag && this.isRepeaterPowered) {
				//extend
				world.scheduleBlockUpdate(x, y, z, this.id(), defaultTickDelay * 2 + (extendSignal ? tickDelay : 0));
			} else if (flag && !this.isRepeaterPowered) {
				//shorten
				world.scheduleBlockUpdate(x, y, z, this.id(), defaultTickDelay * 2 + (extendSignal ? 0 : tickDelay));
			}

		}
	}

	private boolean isGettingPower(World world, int x, int y, int z, int meta) {
		int direction = meta & MASK_DIRECTION;
		switch (direction) {
			case 0:
				return world.getSignal(x, y, z + 1, Side.SOUTH) || world.getBlockId(x, y, z + 1) == Blocks.WIRE_REDSTONE.id() && world.getBlockMetadata(x, y, z + 1) > 0;
			case 1:
				return world.getSignal(x - 1, y, z, Side.WEST) || world.getBlockId(x - 1, y, z) == Blocks.WIRE_REDSTONE.id() && world.getBlockMetadata(x - 1, y, z) > 0;
			case 2:
				return world.getSignal(x, y, z - 1, Side.NORTH) || world.getBlockId(x, y, z - 1) == Blocks.WIRE_REDSTONE.id() && world.getBlockMetadata(x, y, z - 1) > 0;
			case 3:
				return world.getSignal(x + 1, y, z, Side.EAST) || world.getBlockId(x + 1, y, z) == Blocks.WIRE_REDSTONE.id() && world.getBlockMetadata(x + 1, y, z) > 0;
			default:
				return false;
		}
	}

	public boolean onBlockRightClicked(World world, int x, int y, int z, @Nullable Player player, Side side, double xHit, double yHit) {
		int metadata = world.getBlockMetadata(x, y, z);
		int tickDelay = (metadata & MASK_TICK_DELAY) >> 2;
		tickDelay = tickDelay + 1 << 2 & MASK_TICK_DELAY;
		world.setBlockMetadataWithNotify(x, y, z, tickDelay | metadata & MASK_DIRECTION);
		return true;
	}

	public void onActivatorInteract(World world, int x, int y, int z, TileEntityActivator activator, Direction direction) {
		this.onBlockRightClicked(world, x, y, z, null, Side.NONE, 0.5, 0.5);
	}

	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		int l = mob.getHorizontalPlacementDirection(side).index;
		world.setBlockMetadataWithNotify(x, y, z, l);
		boolean flag = this.isGettingPower(world, x, y, z, l);
		if (flag) {
			world.scheduleBlockUpdate(x, y, z, this.id(), 1);
		}

	}

	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		if (!side.isHorizontal()) {
			side = Side.NORTH;
		}

		int l = side.getDirection().index;
		world.setBlockMetadataWithNotify(x, y, z, l);
		boolean flag = this.isGettingPower(world, x, y, z, l);
		if (flag) {
			world.scheduleBlockUpdate(x, y, z, this.id(), 1);
		}

	}

	public void onBlockPlacedByWorld(World world, int x, int y, int z) {
		world.notifyBlocksOfNeighborChange(x + 1, y, z, this.id());
		world.notifyBlocksOfNeighborChange(x - 1, y, z, this.id());
		world.notifyBlocksOfNeighborChange(x, y, z + 1, this.id());
		world.notifyBlocksOfNeighborChange(x, y, z - 1, this.id());
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id());
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.id());
	}

	public boolean isSolidRender() {
		return false;
	}

	public void animationTick(World world, int x, int y, int z, Random rand) {
		if (this.isRepeaterPowered) {
			int meta = world.getBlockMetadata(x, y, z);
			double px = x + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
			double py = y + 0.4 + (rand.nextFloat() - 0.5) * 0.2;
			double pz = z + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
			double xOffset = 0.0;
			double zOffset = 0.0;
			int redstoneBrightness = 15;
			int tickDelay = (meta & MASK_TICK_DELAY) >> 2;
			switch (meta & MASK_DIRECTION) {
				case 0:
					zOffset = torchPosOffset[tickDelay];
					break;
				case 1:
					xOffset = -torchPosOffset[tickDelay];
					break;
				case 2:
					zOffset = -torchPosOffset[tickDelay];
					break;
				case 3:
					xOffset = torchPosOffset[tickDelay];
			}
			world.spawnParticle("reddust", px + xOffset, py, pz + zOffset, 0.0, 0.0, 0.0, 15);
		}
	}
}
