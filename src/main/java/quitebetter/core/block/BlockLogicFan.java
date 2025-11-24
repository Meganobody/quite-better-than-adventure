package quitebetter.core.block;

import java.util.Arrays;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicVeryRotatable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import quitebetter.core.tileentity.TileEntityFan;

public class BlockLogicFan extends BlockLogicVeryRotatable {
	public static final int MASK_SIGNALOVERRIDE = 0b00001000;
	public boolean isActive;
	public boolean isInverted;

	private int setbit(int data, int pos) { pos -= 1; data = data | (1 << pos); data |= 1 << pos; return data;}
	private int unsetbit(int data, int pos) { pos -= 1; data = data & ~(1 << pos); data &= ~(1 << pos); return data;}

	public boolean isSignalOverriden(World world, int x, int y, int z) {
		return (world.getBlockMetadata(x,y,z) & MASK_SIGNALOVERRIDE) != 0;
	}

	public void overrideSignal(World world, int x, int y, int z) {
		world.setBlockMetadataWithNotify(x,y,z,
			this.setbit(world.getBlockMetadata(x,y,z),4)
		);
	}

	public static boolean isInverted(Block fan) {
		return fan.equals(ModBlocks.IN_FAN) || fan.equals(ModBlocks.ACTIVE_IN_FAN);
	}

	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{new ItemStack(ModBlocks.FAN)} : null;
	}

	public static void toggleInverted(World world, int x, int y, int z) {
		Block fan = world.getBlock(x,y,z);
		if (fan != null) {
			BlockLogicFan logic = (BlockLogicFan) fan.getLogic();
			world.setBlockAndMetadataWithNotify(x, y, z,
				(!isInverted(fan) ? (logic.isActive ? ModBlocks.ACTIVE_IN_FAN : ModBlocks.IN_FAN) : (logic.isActive ? ModBlocks.ACTIVE_FAN : ModBlocks.FAN)).id(),
				world.getBlockMetadata(x,y,z));
		}
	}

	public static boolean isFan(Block block) {
		Block[] fans = {ModBlocks.FAN,ModBlocks.ACTIVE_FAN,ModBlocks.IN_FAN,ModBlocks.ACTIVE_IN_FAN};
		return block != null && Arrays.asList(fans).contains(block);
	}

	public BlockLogicFan(Block<?> block, Material material, boolean isActive, boolean isIn) {
		super(block, material);
		this.isActive = isActive;
		this.isInverted = isIn;
		block.withEntity(TileEntityFan::new);
	}

	private int getSightRange(World world, double x, double y, double z, Direction facing) {
		if (facing == Direction.NONE) {
			return 0;
		} else {
			int range = 4;
			for(int i = 1; i <= range; ++i) {
				int x1 = MathHelper.round(x + (double)(facing.getOffsetX() * i));
				int y1 = MathHelper.round(y + (double)(facing.getOffsetY() * i));
				int z1 = MathHelper.round(z + (double)(facing.getOffsetZ() * i));
				int id = world.getBlockId(x1, y1, z1);
				if (Blocks.solid[id]) {
					return i - 1;
				}
			}
			return range;
		}
	}

	@Override
	public void animationTick(World world, int x, int y, int z, Random rand) {
		if (this.isActive) {
			if (this.isModifierFire(world,x,y,z)) {
				Direction dir = getDirection(world.getBlockMetadata(x, y, z));
				int len = this.getSightRange(world, x, y, z, dir);
				for (int i=1;i<5;i++) {
					double xPos = (double) x + (double) rand.nextFloat();
					double yPos = (double) y + (double) rand.nextFloat() * (double) 0.5F;
					double zPos = (double) z + (double) rand.nextFloat();
					world.spawnParticle("flame", xPos + dir.getOffsetX()*getSightRange(world,x,y,z,dir)*rand.nextFloat(), yPos + dir.getOffsetY()*getSightRange(world,x,y,z,dir)*rand.nextFloat(), zPos + dir.getOffsetZ()*getSightRange(world,x,y,z,dir)*rand.nextFloat(), 0, 0, 0, 0);
				}
			} else {
				Direction dir = getDirection(world.getBlockMetadata(x, y, z));
				int len = this.getSightRange(world, x, y, z, dir);
				double xPos = (double) x + (double) rand.nextFloat();
				double yPos = (double) y + (double) rand.nextFloat() * (double) 0.5F;
				double zPos = (double) z + (double) rand.nextFloat();
				world.spawnParticle("smoke", xPos + dir.getOffsetX() * 1.3, yPos + dir.getOffsetY() * 1.3, zPos + dir.getOffsetZ() * 1.3, 0, 0, 0, 0);
			}
		}
	}


	public void setActive(World world, int x, int y, int z, boolean isActive) {
		world.setBlockAndMetadataWithNotify(x,y,z,(( isSignalOverriden(world, x, y, z) || isActive ) ? (isInverted(block) ? ModBlocks.ACTIVE_IN_FAN : ModBlocks.ACTIVE_FAN) : (isInverted(block) ? ModBlocks.IN_FAN : ModBlocks.FAN)).id(), world.getBlockMetadata(x,y,z));
		if (isActive) {
			world.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z,"fire.ignite", 0.1F, 2F);
		}
	}

	public static Direction getDirection(int data) {
		return Direction.getDirectionById(data & MASK_DIRECTION);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		this.setActive(world, x, y, z, this.checkIfActive(world, x, y, z));
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

	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		world.setBlockMetadataWithNotify(x, y, z, setDirection(0, side.getDirection()));
		this.setActive(world, x, y, z, this.checkIfActive(world, x, y, z));
	}

	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		Direction direction = mob.getPlacementDirection(side).getOpposite();
		world.setBlockMetadataWithNotify(x, y, z, setDirection(0, direction));
		this.setActive(world, x, y, z, this.checkIfActive(world, x, y, z));
	}

	public static int setDirection(int meta, Direction direction) {
		return (meta & -8) | direction.getId();
	}

	public Block getModifierBlock(World world, double x, double y, double z) {
		Direction dir = BlockLogicFan.getDirection(world.getBlockMetadata((int)x,(int)y,(int)z));
		return world.getBlock((int)x+dir.getOffsetX(),(int)y+dir.getOffsetY(),(int)z+dir.getOffsetZ());
	}

	public boolean isModifierFire(World world, double x, double y, double z) {
		Block block = getModifierBlock(world, x, y, z);
		return this.getModifierBlock(world, x, y, z) != null && (this.getModifierBlock(world, x, y, z).equals(Blocks.FIRE) || this.getModifierBlock(world, x, y, z).equals(Blocks.BRAZIER_ACTIVE));
	}

}
