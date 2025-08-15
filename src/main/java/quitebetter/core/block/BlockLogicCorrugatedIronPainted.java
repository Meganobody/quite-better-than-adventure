package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.PlacementMode;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLogicCorrugatedIronPainted extends BlockLogicAxisAlignedPainted implements IPainted {
	public BlockLogicCorrugatedIronPainted(Block<?> block, Material material) {
		super(block, material);
	}

	public int getPlacedBlockMetadata(@Nullable Player player, ItemStack stack, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		return stack.getMetadata();
	}

	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		int meta = world.getBlockMetadata(x, y, z);
		Axis axis = mob.getPlacementDirection(side, PlacementMode.SIDE).getAxis();
		world.setBlockMetadataWithNotify(x, y, z, axisToMeta(meta, axis));
	}

	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		int meta = world.getBlockMetadata(x, y, z);
		Axis axis = side.getAxis();
		world.setBlockMetadataWithNotify(x, y, z, axisToMeta(meta, axis));
	}

	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this, 1, this.stripAxisFromMetadata(meta))};
	}

	public void removeDye(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		world.setBlockAndMetadataWithNotify(x, y, z, ModBlocks.BLOCK_IRON_CORRUGATED.id(), this.stripColorFromMetadata(meta));
	}
}
