package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.block.BlockLogicMushroom;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockLogicGlowingMushroom extends BlockLogicMushroom {
	public BlockLogicGlowingMushroom(Block<?> block) {
		super(block);
		float f = 0.2F;
		this.setBlockBounds((double)(0.5F - f), (double)0.0F, (double)(0.5F - f), (double)(0.5F + f), (double)(f * 2.0F), (double)(0.5F + f));
		this.canBeBonemealed = true;
		block.setTicking(true);
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (rand.nextInt(25) == 0) {
			int x1 = x + rand.nextInt(3) - 1;
			int randY = y + rand.nextInt(2) - rand.nextInt(2);
			int z1 = z + rand.nextInt(3) - 1;
			if (world.isAirBlock(x1, randY, z1) && this.canBlockStay(world, x1, randY, z1)) {
				int x2 = x + rand.nextInt(3) - 1;
				int z2 = z + rand.nextInt(3) - 1;
				if (world.isAirBlock(x2, randY, z2) && this.canBlockStay(world, x2, randY, z2)) {
					world.setBlockWithNotify(x2, randY, z2, this.block.id());
				}
			}
		}
		if (world.getBlock(x,y-1,z)!=null && world.getBlock(x,y-1,z).equals(Blocks.COBBLE_STONE_MOSSY)) {
			if (rand.nextInt(2)==0) {
				BlockLogicMushroom that = (BlockLogicMushroom)(Object)this;
				that.updateTick(world, x, y, z, rand);
			}
		}
	}

	protected boolean mayPlaceOn(int blockId) {
		return Blocks.solid[blockId];
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		if (y >= 0 && y < world.getHeightBlocks()) {
			return this.mayPlaceOn(world.getBlockId(x, y - 1, z));
		} else {
			return false;
		}
	}

	public boolean onBonemealUsed(ItemStack itemstack, @Nullable Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		Random rand = world.rand;
		if (!world.isClientSide && this.canBeBonemealed) {
			if (player == null || player.getGamemode().consumeBlocks()) {
				--itemstack.stackSize;
			}

			label38:
			for(int j1 = 0; j1 < 32; ++j1) {
				int _x = blockX;
				int _y = blockY;
				int _z = blockZ;

				for(int j2 = 0; j2 < j1 / 16; ++j2) {
					_x += rand.nextInt(3) - 1;
					_y += (rand.nextInt(3) - 1) * rand.nextInt(3) / 2;
					_z += rand.nextInt(3) - 1;
					if (!this.mayPlaceOn(world.getBlockId(_x, _y - 1, _z))) {
						continue label38;
					}
				}

				if (world.getBlockId(_x, _y, _z) == 0 && (double)rand.nextFloat() > 0.85) {
					world.setBlockWithNotify(_x, _y, _z, this.block.id());
				}
			}

			return true;
		} else {
			return this.canBeBonemealed;
		}
	}
}
