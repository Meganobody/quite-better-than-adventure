package quitebetter.mixin.core;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.block.model.BlockModelPistonHead;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.piston.BlockLogicPistonHead;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quitebetter.client.blockmodel.BlockModels;

@Mixin(
	value = {BlockModelPistonHead.class},
	remap = false
)
public class BlockModelPistonHeadMixin {
	@Shadow
	protected IconCoordinate pistonHead;
	@Shadow
	protected IconCoordinate pistonDry;
	@Shadow
	protected IconCoordinate pistonSteel;
	@Shadow
	protected IconCoordinate pistonSticky;
	@Shadow
	protected IconCoordinate faceTextureOverride;
	@Inject(
		method = {"getBlockTextureFromSideAndMetadata"},
		at = @At("RETURN"),
		cancellable = true
	)
	public void getBlockTextureFromSideAndMetadata(Side side, int data, CallbackInfoReturnable<IconCoordinate> cir, @Local Direction direction) {
		if (side.getDirection() == direction.getOpposite())
		{
			if (this.faceTextureOverride != null) {
				cir.setReturnValue(this.faceTextureOverride);
			}
		}
	}
}
