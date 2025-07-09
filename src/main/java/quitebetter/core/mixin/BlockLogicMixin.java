package quitebetter.core.mixin;

import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
	value = {BlockLogic.class},
	remap = false
)
public class BlockLogicMixin {
	@Inject(
		method = {"onBlockPlacedOnSide"},
		at = @At("TAIL")
	)
	public void onBlockPlacedOnSide(World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfo ci) {
	}

	@Inject(
		method = {"onNeighborBlockChange"},
		at = @At("TAIL")
	)
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId, CallbackInfo ci) {
	}
}
