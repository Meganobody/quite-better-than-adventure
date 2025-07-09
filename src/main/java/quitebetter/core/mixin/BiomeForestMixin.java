package quitebetter.core.mixin;

import quitebetter.core.feature.WorldFeatureGlowingMushrooms;
import net.minecraft.core.world.biome.BiomeForest;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(
	value = {BiomeForest.class},
	remap = false
)
public class BiomeForestMixin {
	@Inject(
		method = {"getRandomWorldGenForTrees"},
		at = @At("RETURN"),
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true
	)
	public void getRandomWorldGenForTrees(Random random, CallbackInfoReturnable<WorldFeature> cir) {
		if (random.nextInt(30) == 1) {
			cir.setReturnValue(new WorldFeatureGlowingMushrooms());
		}
	}
}
