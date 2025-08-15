package quitebetter.mixin.core;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFence;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = {BlockLogicFence.class},
	remap = false
)
public class BlockLogicFenceMixin {
	@Inject(
		method = {"canConnectTo"},
		at = @At("RETURN"),
		cancellable = true
	)
	private void canConnectTo(WorldSource worldSource, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		Block<?> block = worldSource.getBlock(x, y, z);
		int meta = worldSource.getBlockMetadata(x, y, z);
//		if (block!=null && block.equals(Blocks.FLAG)) {
//			Direction dir = Direction.getDirectionById(meta);
//			if (dir.isHorizontal()) {
//				cir.setReturnValue(true);
//			}
//		}
	}
}
