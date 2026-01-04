package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.BlockLogicVeryRotatable;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.entity.projectile.ProjectileArrowGolden;
import net.minecraft.core.entity.projectile.ProjectileCannonball;
import net.minecraft.core.item.IDispensable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import quitebetter.core.tileentity.TileEntityFetcher;
import quitebetter.core.util.ItemUtil;

import java.util.Random;

public class BlockLogicFetcher extends BlockLogicMachine {
	public BlockLogicFetcher(Block<?> block) {
		super(block);
		block.withEntity(TileEntityFetcher::new);
	}
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		if (world.isClientSide) return true;
		TileEntityFetcher tileEntity = (TileEntityFetcher) world.getTileEntity(x, y, z);
		Direction direction = metaToDirection(world.getBlockMetadata(x, y, z));
		final int outX = x + direction.getOffsetX(),
			outY = y + direction.getOffsetY(),
			outZ = z + direction.getOffsetZ();
		interact(world, player, tileEntity, outX, outY, outZ);
		return true;
	}
	private static void interact(World world, Player player, TileEntityFetcher tileEntity, int x, int y, int z) {
		if (tileEntity.fetchedItemStack == null)
		{
			tileEntity.fetchedItemStack = ItemUtil.takeItemStack(world, x, y, z);
			if (tileEntity.fetchedItemStack == null)
				world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.3f, 1.5F);
			else
				world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.pop", 0.3f, 2);
		}
		else
		{
			ItemStack itemStack = tileEntity.takeItem();
			ItemUtil.putItemStack(world, x, y, z, itemStack);
			world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.pop", 0.3f, 0);
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		super.updateTick(world, x, y, z, rand);
		TileEntityFetcher tileEntity = (TileEntityFetcher)world.getTileEntity(x, y, z);
		Direction direction = metaToDirection(world.getBlockMetadata(x, y, z));
		final int outX = x + direction.getOffsetX(),
			outY = y + direction.getOffsetY(),
			outZ = z + direction.getOffsetZ();
		interact(world, null, tileEntity, outX, outY, outZ);
	}
}
