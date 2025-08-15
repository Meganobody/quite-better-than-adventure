package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.block.IPainted;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.World;

public class BlockLogicAxisAlignedPainted extends BlockLogic implements IPainted {
	public BlockLogicAxisAlignedPainted(Block<?> block, Material material) {
		super(block, material);
	}

	public static int axisToMeta(int meta, Axis axis) {
		return meta | BlockLogicAxisAligned.axisToMeta(axis);
	}

	public static Axis metaToAxis(int meta) {
		return BlockLogicAxisAligned.metaToAxis(meta & 3);
	}

	public int stripAxisFromMetadata(int meta) {
		return meta & -4;
	}

	@Override
	public DyeColor fromMetadata(int meta) {
		return DyeColor.colorFromBlockMeta((meta & 240) >> 4);
	}

	@Override
	public int toMetadata(DyeColor dyeColor) {
		return dyeColor.blockMeta << 4;
	}

	@Override
	public int stripColorFromMetadata(int meta) {
		return meta & -241;
	}

	@Override
	public void removeDye(World world, int i, int j, int k) {}
}
