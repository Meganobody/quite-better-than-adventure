package quitebetter.core.mixin;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(
	value = {EntityRenderDispatcher.class},
	remap = false
)
public class EntityRenderDispatcherMixin {
	@Shadow
	private Map<Class<?>, EntityRenderer<?>> renderers;

	@Inject(
		method = {"<init>"},
		at = @At("TAIL")
	)
	public void init(CallbackInfo ci) {

	}
}
