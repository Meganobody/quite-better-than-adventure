package quitebetter.core.mixin;

import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.render.dynamictexture.DynamicTextureCustom;
import net.minecraft.client.render.texture.meta.AnimationProperties;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;

import static quitebetter.core.ModCore.MOD_ID;

@Mixin(
	value = {TextureManager.class},
	remap = false
)
public class TextureMagangerMixin {

	@Shadow
	public final Map<String, DynamicTexture> dynamicTextures = new HashMap();

	@Unique
	public void addDynamicTexture(DynamicTexture texture) {
		this.addDynamicTextureOverride(texture, false);
	}

	@Unique
	public void addDynamicTextureOverride(DynamicTexture texture, boolean override) {
		if (override || !this.dynamicTextures.containsKey(texture.targetTexture.namespaceId)) {
			this.dynamicTextures.put(texture.targetTexture.namespaceId.namespace() + ":" + (String)TextureRegistry.stitcherMapReverse.get(texture.targetTexture.parentAtlas) + "/" + texture.targetTexture.namespaceId.value(), texture);
		}
	}

	@Inject(
		method = "addNativeDynamicTextures",
		at = @At("HEAD"),
		locals = LocalCapture.CAPTURE_FAILSOFT

	)
	public void addNativeDynamicTextures(int state, CallbackInfo ci) {
		if (state > 0) {
			//FAN
			String[] activesides = new String[]{"top", "side", "side_inverted"};
			for(String side : activesides) {
				IconCoordinate texture = TextureRegistry.getTexture(MOD_ID + ":block/fan/active_" + side);
				this.addDynamicTexture(new DynamicTextureCustom(texture, (AnimationProperties) texture.getMeta("animation", AnimationProperties.class)));
			}
		}
	}
}
