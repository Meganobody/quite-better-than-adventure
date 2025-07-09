package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockLogicHazard extends BlockLogic implements IPaintable {
	public BlockLogicHazard(Block<?> block, Material material) {
		super(block, material);
	}

	public int getPlacedBlockMetadata(@Nullable Player player, ItemStack stack, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		return stack.getMetadata() & 15;
	}

	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this, 1, meta)};
	}

	public DyeColor fromMetadata(int meta) {
		return DyeColor.colorFromBlockMeta(meta);
	}

	public int toMetadata(DyeColor color) {
		return color.blockMeta;
	}

	public int stripColorFromMetadata(int meta) {
		return 0;
	}

	public void removeDye(World world, int x, int y, int z) {
		world.setBlockMetadataWithNotify(x, y, z, 0);
	}

	public String getLanguageKey(int meta) {
		return this.block.getKey();
	}

	@Override
	public void setColor(World world, int x, int y, int z, DyeColor color) {
		world.setBlock(x, y, z, ModBlocks.HAZARD_PAINTED.id());
		((BlockLogicHazardPainted)ModBlocks.HAZARD_PAINTED.getLogic()).setColor(world, x, y, z, color);
	}
}
