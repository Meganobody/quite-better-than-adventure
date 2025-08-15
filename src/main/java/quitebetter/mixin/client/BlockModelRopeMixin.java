package quitebetter.mixin.client;

import net.minecraft.client.render.block.model.BlockModelRope;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quitebetter.core.block.ModBlocks;

import java.util.Arrays;
import java.util.logging.Logger;

@Mixin(
	value = {BlockModelRope.class},
	remap = false
)
public class BlockModelRopeMixin {
	@Unique
	public Block[] connectables = {ModBlocks.LANTERN_COAL,ModBlocks.LANTERN_REDSTONE,ModBlocks.LANTERN_MUSHROOM};

	@Inject(
		method = {"canConnect"},
		at = @At("RETURN"),
		cancellable = true
	)
	public void canConnect(WorldSource worldSource, int x, int y, int z, Side side, CallbackInfoReturnable<Boolean> cir) {
		if (side.equals(Side.BOTTOM)) {
			Block block = worldSource.getBlock(x,y-1,z);
			if (block!=null && Arrays.asList(connectables).contains(block)) {
				cir.setReturnValue(true);
			}
		}
	}
}
