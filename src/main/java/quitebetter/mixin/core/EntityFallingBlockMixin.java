package quitebetter.mixin.core;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityFallingBlock;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import quitebetter.core.util.IFalling;

@Mixin(
	value = {EntityFallingBlock.class},
	remap = false
)
public class EntityFallingBlockMixin {
	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/world/World;setBlockMetadata(IIII)Z",
			shift = At.Shift.BEFORE
		),
		cancellable = true
	)
	private void tick(CallbackInfo ci) {
		EntityFallingBlock ent = (EntityFallingBlock)(Object)this;
		Block<?> block = ent.carriedBlock.block();
		if (block.getLogic() instanceof IFalling) {
			if (!((IFalling) block.getLogic()).FellOnGround(
				ent,
				MathHelper.round(ent.x - (double)0.5F),
				MathHelper.round(ent.y),
				MathHelper.round(ent.z - (double)0.5F)
				)) {
				ci.cancel();
			}
		}
	}
}
