package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockLogicConveyor extends BlockLogicRotatable {
	public static final int MASK_DIRECTION = 0b00000111;
	public static final int MASK_PARENTED = 0b00001000;
	private int setbit(int data, int pos, boolean active) {
		if (active) {
			pos -= 1; data = data | (1 << pos); data |= 1 << pos; return data;
		} else {
			pos -= 1; data = data & ~(1 << pos); data &= ~(1 << pos); return data;
		}
	}

	public BlockLogicConveyor(Block<?> block, Material material) {
		super(block, material);
	}

	public boolean isParented(World world, int x, int y, int z) {
		return (world.getBlockMetadata(x,y,z) & MASK_PARENTED) == 1;
	}

	public void setActive(World world, int x, int y, int z, Boolean isActive) {
		world.setBlockAndMetadata(x,y,z,(isActive ? ModBlocks.ACTIVE_CONVEYOR : ModBlocks.CONVEYOR).id(),world.getBlockMetadata(x,y,z));
		if (isActive) {
			world.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z,"fire.ignite", 0.1F, 0.5F);
		}
		updateChild(world, x, y, z, isActive);
	}

	public boolean isConveyor(Block block) {
		return block != null && (block.equals(ModBlocks.CONVEYOR) || block.equals(ModBlocks.ACTIVE_CONVEYOR));
	}

	public void updateChild(World world, int x, int y, int z, Boolean isActive) {
		Direction dir = getDirection(world.getBlockMetadata(x,y,z));
		int cx = x+dir.getOffsetX();
		int cy = y+dir.getOffsetY();
		int cz = z+dir.getOffsetZ();
		Block block = world.getBlock(cx, cy, cz);
		if ((block != null && block.equals(isActive ? ModBlocks.CONVEYOR : ModBlocks.ACTIVE_CONVEYOR)) && getDirection(world.getBlockMetadata(cx,cy,cz)).getOpposite() != dir) {
			if ((world.getBlockMetadata(cx,cy,cz) & MASK_PARENTED) != (isActive ? 1 : 0)) {
				setActive(world, cx, cy, cz, isActive);
				setbit(world.getBlockMetadata(cx,cy,cz),4,isActive);
			}
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		this.setActive(world, x, y, z, this.checkIfActive(world, x, y, z));
	}

	public static Direction getDirection(int data) {
		return Direction.getDirectionById(data & MASK_DIRECTION);
	}

	public boolean checkIfActive(World world, int x, int y, int z) {
		Direction direction = getDirection(world.getBlockMetadata(x, y, z));
		if (direction != Direction.DOWN && world.getSignal(x, y - 1, z, Side.BOTTOM)) {
			return true;
		} else if (direction != Direction.UP && world.getSignal(x, y + 1, z, Side.TOP)) {
			return true;
		} else if (direction != Direction.NORTH && world.getSignal(x, y, z - 1, Side.NORTH)) {
			return true;
		} else if (direction != Direction.SOUTH && world.getSignal(x, y, z + 1, Side.SOUTH)) {
			return true;
		} else if (direction != Direction.EAST && world.getSignal(x + 1, y, z, Side.EAST)) {
			return true;
		} else if (direction != Direction.WEST && world.getSignal(x - 1, y, z, Side.WEST)) {
			return true;
		} else if (world.getSignal(x, y, z, Side.BOTTOM)) {
			return true;
		} else if (direction != Direction.UP && world.getSignal(x, y + 2, z, Side.TOP)) {
			return true;
		} else if (world.getSignal(x, y + 1, z - 1, Side.NORTH)) {
			return true;
		} else if (world.getSignal(x, y + 1, z + 1, Side.SOUTH)) {
			return true;
		} else {
			return world.getSignal(x - 1, y + 1, z, Side.WEST) ? true : world.getSignal(x + 1, y + 1, z, Side.EAST);
		}
	}
}
