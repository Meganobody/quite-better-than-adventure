package quitebetter.core.mixin;

import quitebetter.client.tileentityrender.TileEntityRendererPedestal;
import quitebetter.core.tileentity.TileEntityPedestal;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(
	value = {TileEntityRenderDispatcher.class},
	remap = false
)
public class TileEntityRenderDispatcherMixin {
	@Shadow
	private Map<Class<?>, TileEntityRenderer<?>> renderers;

	@Inject(
		method = "<init>",
		at = @At("TAIL"),
		locals = LocalCapture.CAPTURE_FAILSOFT

	)
	private void init(CallbackInfo ci) {
		renderers.put(TileEntityPedestal.class, new TileEntityRendererPedestal());
	}

}
