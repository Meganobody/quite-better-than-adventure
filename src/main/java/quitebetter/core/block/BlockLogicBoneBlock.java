package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityFallingBlock;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import quitebetter.core.util.IFalling;

import java.util.Random;

public class BlockLogicBoneBlock extends BlockLogic implements IFalling {
	public static boolean fallInstantly = false;

	public BlockLogicBoneBlock(Block<?> block, Material material) {
		super(block, material);
	}

	public void onBlockPlacedByWorld(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.block.id(), this.tickDelay());
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		world.scheduleBlockUpdate(x, y, z, this.block.id(), this.tickDelay());
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		this.tryToFall(world, x, y, z);
	}

	private void tryToFall(World world, int x, int y, int z) {
		Random rand = new Random();
		if (canFallBelow(world, x, y - 1, z) && y >= 0) {
			byte byte0 = 32;
			if (!fallInstantly && world.areBlocksLoaded(x - byte0, y - byte0, z - byte0, x + byte0, y + byte0, z + byte0)) {
				EntityFallingBlock entityFallingBlock = new EntityFallingBlock(world, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, this.block.id(), 0, (TileEntity)null);
				world.entityJoinedWorld(entityFallingBlock);
			} else {
				world.setBlockWithNotify(x, y, z, 0);

				while(canFallBelow(world, x, y - 1, z) && y > 0) {
					--y;
				}

				if (y > 0) {
					world.setBlockWithNotify(x, y, z, this.block.id());
				}
			}
		}

	}

	public int tickDelay() {
		return 3;
	}

	public static boolean canFallBelow(World world, int x, int y, int z) {
		Block<?> block = world.getBlock(x, y, z);
		return block == null || block.hasTag(BlockTags.PLACE_OVERWRITES);
	}

	public boolean FellOnGround(EntityFallingBlock fallingBlock, int x, int y, int z) {
		Random rand = new Random();
		assert fallingBlock.world!=null;
		for (int i=1;i<25;i++) {
			fallingBlock.world.spawnParticle("block",
				x+0.5,
				y+0.5,
				z+0.5,
				(double)(rand.nextInt(11)-5),
				(double)(rand.nextInt(11)-5),
				(double)(rand.nextInt(11)-5),
				block.id()
			);
		}
		fallingBlock.world.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z, "mob.skeletonhurt", 0.25F, 1F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
		return true;
	}

	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		Random rand = new Random();
		return dropCause == EnumDropCause.SILK_TOUCH || dropCause == EnumDropCause.PICK_BLOCK ? new ItemStack[]{new ItemStack(this.block)} : new ItemStack[]{new ItemStack(Items.BONE,rand.nextInt(4))};
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, Side side, int meta, Player player, Item item) {
		Random rand = new Random();
		world.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z, "mob.skeletonhurt", 0.5F, 0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
	}
}
