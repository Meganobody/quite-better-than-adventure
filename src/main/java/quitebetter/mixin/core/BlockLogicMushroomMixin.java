package quitebetter.mixin.core;

import net.minecraft.core.block.BlockLogicMushroom;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(
	value = {BlockLogicMushroom.class},
	remap = false
)
public class BlockLogicMushroomMixin {
	@Inject(
		method = {"updateTick"},
		at = @At("TAIL")
	)
	public void updateTick(World world, int x, int y, int z, Random rand, CallbackInfo ci) {
		if (world.getBlock(x,y-1,z)!=null && world.getBlock(x,y-1,z).equals(Blocks.COBBLE_STONE_MOSSY)) {
			if (rand.nextInt(2)==0) {
				BlockLogicMushroom that = (BlockLogicMushroom)(Object)this;

				for (int i=1;i<=3;i++) {
					int x1 = x + rand.nextInt(3) - 1;
					int randY = y + rand.nextInt(2) - rand.nextInt(2);
					int z1 = z + rand.nextInt(3) - 1;
					if (world.isAirBlock(x1, randY, z1) && that.canBlockStay(world, x1, randY, z1)) {
						int x2 = x + rand.nextInt(3) - 1;
						int z2 = z + rand.nextInt(3) - 1;
						if (world.isAirBlock(x2, randY, z2) && that.canBlockStay(world, x2, randY, z2)) {
							world.setBlockWithNotify(x2, randY, z2, that.block.id());
						}
					}
				}
			}
		}
	}
}
