package quitebetter.core.block;

import quitebetter.core.tileentity.TileEntityCrate;
import quitebetter.core.util.ILeftClickable;
import quitebetter.core.util.IRightClickable;
import quitebetter.core.util.PlayerUtil;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.IPaintable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BlockLogicCrate extends BlockLogic implements IPaintable, ILeftClickable, IRightClickable {
	public BlockLogicCrate(Block<?> block, Material material) {
		super(block, material);
		block.withEntity(TileEntityCrate::new);
	}

	public static boolean isCrate(Block block) {
		Block[] fans = {ModBlocks.CRATE,ModBlocks.CRATE_PAINTED};
		return block != null && Arrays.asList(fans).contains(block);
	}

	@Override
	public boolean preventsBreaking(World world, int x, int y, int z, Player player, Side side) {
		TileEntityCrate tile = (TileEntityCrate) world.getTileEntity(x,y,z);
		return tile!=null && tile.ItemId!=null && !player.isSneaking();
	}

	@Override
	public void onBlockLeftClicked(World world, int x, int y, int z, Player player, Side side) {
		TileEntityCrate tile = (TileEntityCrate) world.getTileEntity(x,y,z);
		if (tile!=null) {
			ItemStack stack = tile.takeStack();
			if (stack != null && (player.getHeldItem()==null || TileEntityCrate.canUseToTake(player.getHeldItem()) )) {
				if (PlayerUtil.storeItemStack(player, stack) == -1) {
					world.entityJoinedWorld(new EntityItem(world, player.x, player.y, player.z, stack));
				}
			}
		}
	}

	@Override
	public boolean preventsInteraction(World world, int x, int y, int z, Player player, Side side) {
		TileEntityCrate tile = (TileEntityCrate) world.getTileEntity(x,y,z);
		return tile!=null && (tile.ItemId!=null || !player.isSneaking()) && (player.getHeldItem()==null || TileEntityCrate.canUseToTake(player.getHeldItem()));
	}

	@Override
	public void onBlockRightClicked(World world, int x, int y, int z, Player player, Side side) {
		Random rand = new Random();
		TileEntityCrate tile = (TileEntityCrate) world.getTileEntity(x,y,z);
		if (tile!=null) {
			tile.pushItems(player);
		} else {
			for(int j = 0; j < 4; ++j) {
				world.spawnParticle("smoke", x+rand.nextFloat(), y+rand.nextFloat(), z+rand.nextFloat(), (double)(rand.nextInt(11)-5)/30, (double)(rand.nextInt(11)-5)/30, (double)(rand.nextInt(11)-5)/30, 0);
			}
		}
		player.swingItem();
	}

	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		ArrayList<ItemStack> result = new ArrayList<>();
		if (tileEntity!=null) {
			result = ((TileEntityCrate)tileEntity).getBreakResult();
			result.add(new ItemStack(this.block, 1, meta));
		}
		result.add(new ItemStack(this.block, 1, meta));
		EntityItem.enableItemClumping = true;
		return result.toArray(new ItemStack[]{});
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		TileEntityCrate tile = (TileEntityCrate) world.getTileEntity(x,y,z);
		if (tile!=null) {
			tile.takeOutOfBasket();
		}
	}

	@Override
	public void setColor(World world, int x, int y, int z, DyeColor color) {
		world.setBlockRaw(x, y, z, ModBlocks.CRATE_PAINTED.id());
		((BlockLogicCratePainted)ModBlocks.CRATE_PAINTED.getLogic()).setColor(world, x, y, z, color);
	}
}
