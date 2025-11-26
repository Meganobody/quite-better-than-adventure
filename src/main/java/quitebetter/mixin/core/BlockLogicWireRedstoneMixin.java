package quitebetter.mixin.core;

import net.minecraft.core.block.*;
import net.minecraft.core.util.helper.*;
import net.minecraft.core.world.WorldSource;
import quitebetter.core.block.BlockLogicLanternRedstone;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = {BlockLogicWireRedstone.class},
	remap = false
)
public class BlockLogicWireRedstoneMixin {
	@Inject(
		method = "shouldConnectTo",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void onShouldConnectTo(WorldSource worldSource, int x, int y, int z, int data, CallbackInfoReturnable<Boolean> ci) {
		int blockId = worldSource.getBlockId(x, y, z);
		if (blockId == 0) return;
		Block<?> block = Blocks.getBlock(blockId);
		if (block == null) return;
		BlockLogic logic = block.getLogic();
		if (!(logic instanceof BlockLogicLanternRedstone)) return;
		BlockLogicLanternRedstone lanternLogic = (BlockLogicLanternRedstone)logic;
		ci.setReturnValue(lanternLogic.shouldSideEmitSignal(worldSource, x, y, z, Direction.horizontalDirections[data & 3].getSide()));
	}
}
