package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityFallingBlock;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import quitebetter.core.util.IFalling;

import java.util.Random;

public class BlockLogicMudSlab extends BlockLogicSlab implements IFalling {
	private final boolean isHardened;
	private final Block<?> altBlock;

	public BlockLogicMudSlab(Block<?> block, Block<?> modelBlock, boolean isHardened, Block<?> altBlock) {
		super(block, modelBlock);
		this.isHardened = isHardened;
		this.altBlock = altBlock;
	}

	public int tickDelay() {
		return 5;
	}

	public void onBlockPlacedByWorld(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.block.id(), this.tickDelay());
	}

	private boolean isWaterNearby(World world, int x, int y, int z, int range) {
		for(int x1 = x - range; x1 <= x + range; ++x1) {
			for(int y1 = y - range; y1 <= y + range; ++y1) {
				for(int z1 = z - range; z1 <= z + range; ++z1) {
					if (Blocks.hasTag(world.getBlockId(x1, y1, z1), BlockTags.IS_WATER)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean isLavaNearby(World world, int x, int y, int z) {
		for(int x1 = x - 1; x1 <= x + 1; ++x1) {
			for(int y1 = y - 1; y1 <= y + 1; ++y1) {
				for(int z1 = z - 1; z1 <= z + 1; ++z1) {
					if (Blocks.hasTag(world.getBlockId(x1, y1, z1), BlockTags.IS_LAVA)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean canBecomeWet(World world, int x, int y, int z) {
		if (this.isWaterNearby(world, x, y, z, 1)) {
			return true;
		} else {
			if (this.isWaterNearby(world, x, y, z, 3)) {
				for(int x1 = x - 1; x1 <= x + 1; ++x1) {
					for(int y1 = y - 1; y1 <= y + 1; ++y1) {
						for(int z1 = z - 1; z1 <= z + 1; ++z1) {
							int bID = world.getBlockId(x1, y1, z1);
							if (bID == Blocks.MUD.id() || bID == Blocks.FARMLAND_DIRT.id() && BlockLogicFarmland.isWet(world.getBlockMetadata(x1, y1, z1))) {
								return true;
							}
						}
					}
				}
			}

			return false;
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		world.scheduleBlockUpdate(x, y, z, this.block.id(), this.tickDelay());
	}

	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this, 1, meta)};
	}

	private void tryToFall(World world, int x, int y, int z) {
		if (!isHardened) {
			int meta = world.getBlockMetadata(x,y,z);
			if (canFallBelow(world, x, y - 1, z, this.block.id(), meta) && y >= 0) {
				byte byte0 = 32;
				if (!world.areBlocksLoaded(x - byte0, y - byte0, z - byte0, x + byte0, y + byte0, z + byte0)) {
					world.setBlockWithNotify(x, y, z, 0);

					while(canFallBelow(world, x, y - 1, z, this.block.id(), meta) && y > 0) {
						--y;
					}

					if (y > 0) {
						world.setBlockWithNotify(x, y, z, this.block.id());
					}
				} else {
					EntityFallingBlock entityFallingBlock = new EntityFallingBlock(world, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, this.block.id(), world.getBlockMetadata(x,y,z), (TileEntity)null);
					world.entityJoinedWorld(entityFallingBlock);
				}
			}

		}
	}

	public static boolean canFallBelow(World world, int x, int y, int z, int id, int meta) {
		int blockId = world.getBlockId(x, y, z);
		return meta == 2 || (blockId == id && world.getBlockMetadata(x,y,z)!=1) || blockId == 0 || blockId == Blocks.FIRE.id() || Blocks.hasTag(blockId, BlockTags.IS_WATER) || Blocks.hasTag(blockId, BlockTags.IS_LAVA );
	}

	public boolean FellOnGround(EntityFallingBlock fallingBlock, int x, int y, int z) {
		World world = fallingBlock.world;
		if (world.getBlockId(x,y-1,z)==block.id() && world.getBlockMetadata(x,y-1,z)!=1) {
			world.setBlockAndMetadataWithNotify(x,y-1,z,block.id(),1);
			fallingBlock.remove();
			world.setBlockWithNotify(x,y,z,0);
			return false;
		} else if (fallingBlock.carriedBlock.metadata==2) {
			world.setBlockMetadataWithNotify(x,y,z,0);
			fallingBlock.remove();
			return false;
		}
		return true;
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (this.isHardened && !this.isLavaNearby(world, x, y, z) && this.canBecomeWet(world, x, y, z)) {
			world.setBlockAndMetadataWithNotify(x, y, z, altBlock.id(),world.getBlockMetadata(x,y,z));
		} else if (!this.isHardened && (this.isLavaNearby(world, x, y, z) || world.dimension == Dimension.NETHER)) {
			world.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

			for(int l = 0; l < 8; ++l) {
				world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), (double)0.0F, (double)0.0F, (double)0.0F, 0);
			}

			world.setBlockAndMetadataWithNotify(x, y, z, altBlock.id(),world.getBlockMetadata(x,y,z));
		} else {
			this.tryToFall(world, x, y, z);
		}
	}
}
