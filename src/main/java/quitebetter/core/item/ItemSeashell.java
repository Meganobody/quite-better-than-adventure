package quitebetter.core.item;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectilePebble;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.IDispensable;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import quitebetter.core.block.BlockLogicOverlaySeashells;
import quitebetter.core.block.ModBlocks;

import java.util.Random;

public class ItemSeashell extends Item implements IDispensable {
	public ItemSeashell(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
		this.maxStackSize = 64;
	}

	public boolean onUseItemOnBlock(ItemStack itemstack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		int id = world.getBlockId(blockX, blockY, blockZ);
		int meta = world.getBlockMetadata(blockX, blockY, blockZ);
		if (
			id != ModBlocks.OVERLAY_SEASHELL.id() &&
			Blocks.blocksList[id] != null &&
			Blocks.blocksList[id].hasTag(BlockTags.PLACE_OVERWRITES)
		) {
			id = 0;
			meta = 0;
		}

		if (itemstack.stackSize <= 0) {
			return false;
		} else if (blockY == world.getHeightBlocks() - 1 && ModBlocks.OVERLAY_SEASHELL.getMaterial().isSolid()) {
			return false;
		} else {
			if (id == ModBlocks.OVERLAY_SEASHELL.id() && side == Side.TOP) {
				int newMeta = meta + 1;
				if (!world.isBlockOpaqueCube(blockX, blockY - 1, blockZ)) {
					return false;
				}

				if (newMeta < 3) {
					world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, ModBlocks.OVERLAY_SEASHELL.id(), newMeta);
					world.playBlockSoundEffect(player, (double)((float)blockX + 0.5F), (double)((float)blockY + 0.5F), (double)((float)blockZ + 0.5F), ModBlocks.OVERLAY_SEASHELL, EnumBlockSoundEffectType.PLACE);
					itemstack.consumeItem(player);
					return true;
				}
			}

			if (id != 0) {
				blockX += side.getOffsetX();
				blockY += side.getOffsetY();
				blockZ += side.getOffsetZ();
				id = world.getBlockId(blockX, blockY, blockZ);
				meta = world.getBlockMetadata(blockX, blockY, blockZ);
			}

			if (id == ModBlocks.OVERLAY_SEASHELL.id()) {
				int newMeta = meta + 1;
				AABB bbBox = AABB.getTemporaryBB((double)blockX, (double)blockY, (double)blockZ, (double)((float)blockX + 1.0F), (double)((float)blockY + (float)(2 * (newMeta + 1)) / 16.0F), (double)((float)blockZ + 1.0F));
				if (!world.checkIfAABBIsClear(bbBox) || !world.isBlockOpaqueCube(blockX, blockY - 1, blockZ)) {
					return false;
				}

				if (newMeta < 3) {
					world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, ModBlocks.OVERLAY_SEASHELL.id(), newMeta);
					world.playBlockSoundEffect(player, (double)((float)blockX + 0.5F), (double)((float)blockY + 0.5F), (double)((float)blockZ + 0.5F), ModBlocks.OVERLAY_SEASHELL, EnumBlockSoundEffectType.PLACE);
					itemstack.consumeItem(player);
					return true;
				}
			}

			if (world.canBlockBePlacedAt(ModBlocks.OVERLAY_SEASHELL.id(), blockX, blockY, blockZ, false, side) && world.isBlockOpaqueCube(blockX, blockY - 1, blockZ) && world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, ModBlocks.OVERLAY_SEASHELL.id(), 0)) {
				ModBlocks.OVERLAY_SEASHELL.onBlockPlacedByMob(world, blockX, blockY, blockZ, side, player, xPlaced, yPlaced);
				world.playBlockSoundEffect(player, (double)((float)blockX + 0.5F), (double)((float)blockY + 0.5F), (double)((float)blockZ + 0.5F), ModBlocks.OVERLAY_SEASHELL, EnumBlockSoundEffectType.PLACE);
				itemstack.consumeItem(player);
				return true;
			} else {
				return false;
			}
		}
	}

	public void onUseByActivator(ItemStack itemStack, TileEntityActivator activatorBlock, World world, Random random, int blockX, int blockY, int blockZ, double offX, double offY, double offZ, Direction direction) {
		ProjectilePebble projectilePebble = new ProjectilePebble(world, (double)blockX + offX, (double)blockY + offY, (double)blockZ + offZ);
		projectilePebble.setHeading((double)direction.getOffsetX() * 0.6, direction.getOffsetY() == 0 ? 0.1 : (double)direction.getOffsetY() * 0.6, (double)((float)direction.getOffsetZ() * 0.6F), 1.1F, 6.0F);
		world.entityJoinedWorld(projectilePebble);
		--itemStack.stackSize;
	}

	public void onDispensed(ItemStack itemStack, World world, double x, double y, double z, int xOffset, int yOffset, int zOffset, Random random) {
		ProjectilePebble projectilePebble = new ProjectilePebble(world, x, y, z);
		projectilePebble.setHeading((double)xOffset, (double)yOffset + 0.1, (double)zOffset, 1.1F, 6.0F);
		world.entityJoinedWorld(projectilePebble);
	}
}
