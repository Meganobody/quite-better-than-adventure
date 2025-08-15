package quitebetter.core.block;

import quitebetter.core.item.ModItems;
import quitebetter.core.tileentity.TileEntityPedestal;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class BlockLogicPedestal extends BlockLogic {
	public BlockLogicPedestal(Block<?> block, Material material) {
		super(block, material);
		this.setBlockBounds(0,0,0,1,0.1,1);
		block.withEntity(TileEntityPedestal::new);
	}

	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		TileEntityPedestal tile = (TileEntityPedestal)world.getTileEntity(x, y, z);
		ItemStack heldItem = player.getHeldItem();
		boolean flag = tile.setDisplayItem(player, heldItem);
		if (heldItem != null && heldItem.stackSize <= 0) {
			player.inventory.setItem(player.inventory.getCurrentItemIndex(), (ItemStack)null);
		}

		return flag;
	}

	public boolean mayPlaceOn(int blockId) {
		Block[] excepts = {
			ModBlocks.SUPPORT_IRON,
			ModBlocks.SUPPORT_STEEL,
			ModBlocks.SUPPORT_WOOD,
			ModBlocks.FAN,
			ModBlocks.ACTIVE_FAN,
			ModBlocks.IN_FAN,
			ModBlocks.ACTIVE_IN_FAN,
			Blocks.PISTON_BASE,
			Blocks.PISTON_HEAD,
			Blocks.BRAZIER_INACTIVE,
			Blocks.BRAZIER_ACTIVE, //Wanted to remove, but it looks cool.
			Blocks.MESH,
			Blocks.MESH_GOLD,
			Blocks.ICE,
			Blocks.PERMAICE
		};
		return Blocks.solid[blockId] || Arrays.asList(excepts).contains(Blocks.getBlock(blockId));
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		if (y >= 0 && y < world.getHeightBlocks()) {
			return this.mayPlaceOn(world.getBlockId(x, y - 1, z));
		} else {
			return false;
		}
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return super.canPlaceBlockAt(world, x, y, z) && this.mayPlaceOn(world.getBlockId(x, y - 1, z));
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), (TileEntity)null, (Player)null);
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	public boolean isCubeShaped() {
		return false;
	}

	public boolean isSolidRender() {
		return false;
	}

	public boolean getSignal(WorldSource worldSource, int x, int y, int z, Side side) {
		return ((TileEntityPedestal) worldSource.getTileEntity(x,y,z)).displayItem != null;
	}

	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(ModItems.PEDESTAL)};
	}

	public static void updateNeighbours(World world, int x, int y, int z, Block block) {
		world.notifyBlocksOfNeighborChange(x, y - 1, z, block.id());
		world.notifyBlocksOfNeighborChange(x, y + 1, z, block.id());
		world.notifyBlocksOfNeighborChange(x - 1, y, z, block.id());
		world.notifyBlocksOfNeighborChange(x + 1, y, z, block.id());
		world.notifyBlocksOfNeighborChange(x, y, z - 1, block.id());
		world.notifyBlocksOfNeighborChange(x, y, z + 1, block.id());

		world.notifyBlocksOfNeighborChange(x, y - 1, z, block.id());
		world.notifyBlocksOfNeighborChange(x, y - 1, z, block.id());
		world.notifyBlocksOfNeighborChange(x - 1, y - 1, z, block.id());
		world.notifyBlocksOfNeighborChange(x + 1, y - 1, z, block.id());
		world.notifyBlocksOfNeighborChange(x, y - 1, z - 1, block.id());
		world.notifyBlocksOfNeighborChange(x, y - 1, z + 1, block.id());
	}
}
