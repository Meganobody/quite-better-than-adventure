package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import quitebetter.core.item.ModItems;

public class BlockLogicOverlaySeashells extends BlockLogic {

	public BlockLogicOverlaySeashells(Block<?> block, Material material) {
		super(block, material);
		this.setBlockBounds((double)0.0F, (double)0.0F, (double)0.0F, (double)1.0F, (double)0.0625F, (double)1.0F);
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		Block<?> block = world.getBlock(x, y - 1, z);
		return block != null && block.getMaterial().isSolid();
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return this.canPlaceBlockAt(world, x, y, z);
	}

	public boolean isSolidRender() {
		return false;
	}

	public AABB getCollisionBoundingBoxFromPool(WorldSource world, int x, int y, int z) {
		return null;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), (TileEntity)null, (Player)null);
			world.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean isCubeShaped() {
		return false;
	}

	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return dropCause == EnumDropCause.PICK_BLOCK ? new ItemStack[]{new ItemStack(ModItems.SEASHELL, 1)} : new ItemStack[]{new ItemStack(ModItems.SEASHELL, meta + 1)};
	}
}
