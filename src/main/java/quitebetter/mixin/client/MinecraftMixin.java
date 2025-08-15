package quitebetter.mixin.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quitebetter.core.ModCore;

@Mixin(
	value = {Minecraft.class},
	remap = false
)
public class MinecraftMixin {
	@Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/achievement/stat/StatList;init()V"))
	public void initStats(CallbackInfo ci) {
		ModCore.onStatInitialize();
	}
}
