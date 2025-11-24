package quitebetter.mixin.core;

import net.minecraft.core.block.BlockLogicMushroom;
import net.minecraft.core.block.BlockLogicWireRedstone;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import quitebetter.core.block.BlockLogicLanternRedstone;
import quitebetter.core.block.ModBlocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

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
		if (blockId == ModBlocks.LANTERN_REDSTONE_ENCASED.id() || blockId == ModBlocks.LANTERN_REDSTONE_ENCASED_IDLE.id()) {
			BlockLogicLanternRedstone logic = (BlockLogicLanternRedstone)Blocks.getBlock(blockId).getLogic();
			Side[] lookup = new Side[]{Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST};
			ci.setReturnValue(logic.shouldSideEmitSignal(worldSource, x, y, z, lookup[data]));
		}
	}
}