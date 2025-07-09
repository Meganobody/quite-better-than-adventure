package quitebetter.core.mixin;

import quitebetter.core.tileentity.TileEntityCrate;
import quitebetter.core.tileentity.TileEntityFan;
import quitebetter.core.tileentity.TileEntityGlued;
import quitebetter.core.tileentity.TileEntityPedestal;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.util.collection.NamespaceID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static quitebetter.core.ModCore.MOD_ID;

@Mixin(
	value = {TileEntityDispatcher.class},
	remap = false
)
public class TileEntityDispatcherMixin {
	@Shadow
	private static boolean hasInit;

	@Inject(
		method = "init",
		at = @At("HEAD"),
		locals = LocalCapture.CAPTURE_FAILSOFT

	)
	private static void init(CallbackInfo ci) {
		if (!hasInit) {
			TileEntityDispatcher.addMapping(TileEntityFan.class, NamespaceID.getPermanent(MOD_ID, "Fan"));
			TileEntityDispatcher.addMapping(TileEntityPedestal.class, NamespaceID.getPermanent(MOD_ID, "Pedestal"));
			TileEntityDispatcher.addMapping(TileEntityGlued.class, NamespaceID.getPermanent(MOD_ID, "Glued"));
			TileEntityDispatcher.addMapping(TileEntityCrate.class, NamespaceID.getPermanent(MOD_ID, "Crate"));
		}
	}

}
