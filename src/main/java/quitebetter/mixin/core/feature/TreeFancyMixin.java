package quitebetter.mixin.core.feature;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quitebetter.core.feature.ModFeatures;

import java.util.Random;

@Mixin(
	value = {WorldFeatureTreeFancy.class},
	remap = false
)
public class TreeFancyMixin {
	@Inject(
		method = {"place"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/generate/feature/tree/WorldFeatureTree;onTreeGrown(Lnet/minecraft/core/world/World;III)V", shift = At.Shift.BEFORE),
		cancellable = true
	)
	private void place(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if (!ModFeatures.TreeGeneration(world, random, x, y, z, WorldFeatureTreeFancy.class)) { cir.setReturnValue(false); }
	}
}
