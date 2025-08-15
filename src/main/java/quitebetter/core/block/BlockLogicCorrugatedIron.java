package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.World;

public class BlockLogicCorrugatedIron  extends BlockLogicAxisAligned implements IPaintable {
	public BlockLogicCorrugatedIron(Block<?> block, Material material) {
		super(block, material);
	}

	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this, 1, meta)};
	}

	public String getLanguageKey(int meta) {
		return this.block.getKey();
	}

	@Override
	public void setColor(World world, int x, int y, int z, DyeColor color) {
		int meta = world.getBlockMetadata(x,y,z);
		world.setBlockAndMetadata(x, y, z, ModBlocks.BLOCK_IRON_CORRUGATED_PAINTED.id(),meta);
		((BlockLogicCorrugatedIronPainted)ModBlocks.BLOCK_IRON_CORRUGATED_PAINTED.getLogic()).setColor(world, x, y, z, color);
	}
}
