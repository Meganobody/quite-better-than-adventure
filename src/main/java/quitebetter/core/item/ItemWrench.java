package quitebetter.core.item;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.Renderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.piston.BlockLogicPistonBase;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import quitebetter.core.block.BlockLogicAxisAlignedPainted;
import quitebetter.core.block.BlockLogicCorrugatedIron;
import quitebetter.core.util.BlockUtil;

import java.util.Random;

import static quitebetter.core.ModCore.LOGGER;

public class ItemWrench extends Item {

	static final Direction[] ZAxisDirections = {
		Direction.UP,
		Direction.EAST,
		Direction.DOWN,
		Direction.WEST
	};
	static final Direction[] XAxisDirections = {
		Direction.UP,
		Direction.NORTH,
		Direction.DOWN,
		Direction.SOUTH
	};
	static final Direction[] YAxisDirections = {
		Direction.NORTH,
		Direction.EAST,
		Direction.SOUTH,
		Direction.WEST
	};
	static final BlockUtil.Stairs.StairsDirection[] XAxisStairsDirections = {
		new BlockUtil.Stairs.StairsDirection(Direction.NORTH,Direction.DOWN),
		new BlockUtil.Stairs.StairsDirection(Direction.NORTH,Direction.UP),
		new BlockUtil.Stairs.StairsDirection(Direction.SOUTH,Direction.UP),
		new BlockUtil.Stairs.StairsDirection(Direction.SOUTH,Direction.DOWN)
	};
	static final BlockUtil.Stairs.StairsDirection[] ZAxisStairsDirections = {
		new BlockUtil.Stairs.StairsDirection(Direction.WEST,Direction.DOWN),
		new BlockUtil.Stairs.StairsDirection(Direction.EAST,Direction.DOWN),
		new BlockUtil.Stairs.StairsDirection(Direction.EAST,Direction.UP),
		new BlockUtil.Stairs.StairsDirection(Direction.WEST,Direction.UP)
	};

	static Direction rotateRotatable(Direction dir, Side side) {
		Axis dax = dir.getAxis();
		Axis sax = side.getAxis();
		if (dax!=sax && sax.equals(Axis.Y)) {
			return YAxisDirections[ArrayUtils.indexOf(YAxisDirections, dir)+1&3];
		}
		return dir;
	}

	static Direction rotateVeryRotatable(Direction dir, Side side) {
		Axis dax = dir.getAxis();
		Axis sax = side.getAxis();
		if (dax!=sax) {
			switch (sax) {
				case X:
					return XAxisDirections[ArrayUtils.indexOf(XAxisDirections, dir)+1&3];
				case Y:
					return YAxisDirections[ArrayUtils.indexOf(YAxisDirections, dir)+1&3];
				case Z:
					return ZAxisDirections[ArrayUtils.indexOf(ZAxisDirections, dir)+1&3];
				default:
					break;
			}
		}
		return dir;
	}

	static Axis rotateAxisAligned(Axis axis, Side side) {
		Axis sax = side.getAxis();
		if (axis!=sax) {
			switch (sax) {
				case X:
					return axis.equals(Axis.Y) ? Axis.Z : Axis.Y;
				case Y:
					return axis.equals(Axis.X) ? Axis.Z : Axis.X;
				case Z:
					return axis.equals(Axis.Y) ? Axis.X : Axis.Y;
				default:
					break;
			}
		}
		return axis;
	}

	static int rotateStairs(int meta, Side side) {
		BlockUtil.Stairs.StairsDirection dir = BlockUtil.Stairs.getDirection(meta);
		Axis sax = side.getAxis();
		Axis hdax = dir.hDir.getAxis();
		BlockUtil.Stairs.StairsDirection sdir = null;
		if (sax!=hdax) {
			switch (sax) {
				case X:
					dir = XAxisStairsDirections[ArrayUtils.indexOf(XAxisStairsDirections, dir)+1&3];
					break;
				case Y:
					dir = new BlockUtil.Stairs.StairsDirection(
						YAxisDirections[ArrayUtils.indexOf(YAxisDirections, dir.hDir)+1&3],
						dir.vDir
					);
					break;
				case Z:
					dir = ZAxisStairsDirections[ArrayUtils.indexOf(ZAxisStairsDirections, dir)+1&3];
					break;
				default:
					break;
			}
		}
		meta = BlockUtil.Stairs.getMeta(dir);
		return meta;
	}

	public ItemWrench(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

	public boolean rotateBlock(World world, int x, int y, int z, Side side) {
		boolean success = false;

		Block<?> block = world.getBlock(x, y, z);
		if (block != null) {
			BlockLogic logic = block.getLogic();
			int meta = world.getBlockMetadata(x,y,z);

			if (logic instanceof BlockLogicRotatable) {
				world.setBlockMetadataWithNotify(x, y, z,
					BlockLogicRotatable.setDirection(meta,
						rotateRotatable(BlockLogicRotatable.getDirectionFromMeta(meta), side)
					)
				);
				success = true;
			} else if (logic instanceof BlockLogicAxisAlignedPainted) {
				world.setBlockMetadataWithNotify(x, y, z,
					((BlockLogicAxisAlignedPainted) logic).stripAxisFromMetadata(meta) |
						BlockLogicAxisAligned.axisToMeta(
							rotateAxisAligned(BlockLogicAxisAlignedPainted.metaToAxis(meta), side)
						)
				);
				success = true;
			} else if (logic instanceof BlockLogicVeryRotatable) {
				world.setBlockMetadataWithNotify(x, y, z,
					BlockLogicVeryRotatable.setDirection(meta,
						rotateVeryRotatable(BlockLogicVeryRotatable.metaToDirection(meta), side)
					)
				);
				success = true;
			} else if (logic instanceof BlockLogicAxisAligned) {
				world.setBlockMetadataWithNotify(x, y, z,
					BlockLogicAxisAligned.axisToMeta(
						rotateAxisAligned(BlockLogicAxisAligned.metaToAxis(meta), side)
					)
				);
				success = true;
			} else if (logic instanceof BlockLogicStairsPainted) {
				world.setBlockMetadataWithNotify(x, y, z,
					(meta & 240) | rotateStairs(meta & 15, side)
				);
				success = true;
			} else if (logic instanceof BlockLogicStairs) {
				world.setBlockMetadataWithNotify(x, y, z,
					rotateStairs(meta,side)
				);
				success = true;
			} else if (logic instanceof BlockLogicPistonBase) {
				if (!BlockLogicPistonBase.isPowered(meta)) {
					world.setBlockMetadataWithNotify(x, y, z,BlockLogicVeryRotatable.setDirection(meta,
							rotateVeryRotatable(BlockLogicPistonBase.getDirection(meta), side)
						)
					);
				}
				success = true;
			}
		}
		if (success) {
			Random random = new Random();
			world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.door_open", 0.25F, 3F + (random.nextFloat() - random.nextFloat()) * 0.4F);
		}
		return success;
	}

	public boolean onUseItemOnBlock(ItemStack itemstack, @Nullable Player player, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		return rotateBlock(world, x, y, z, side);
	}

	public void onUseByActivator(ItemStack itemStack, TileEntityActivator activatorBlock, World world, Random random, int x, int y, int z, double offX, double offY, double offZ, Direction direction) {
		rotateBlock(world, x+direction.getOffsetX(), y+direction.getOffsetY(), z+direction.getOffsetZ(), direction.getSide().getOpposite());
	}
}
