package quitebetter.mixin.core.item;

import net.minecraft.core.item.ItemFishingRod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
	value = {ItemFishingRod.class},
	remap = false
)
public class ItemFishingRodMixin {

	@Inject(
		method = {"<init>"},
		at = @At("TAIL")
	)
	public void ItemFishingRod(String name, String namespaceId, int id, CallbackInfo ci) {
		if (name == "tool_steel_fishingrod") {
			ItemFishingRod rod = (ItemFishingRod)(Object)this;
			rod.setMaxDamage(588);
		}
	}
}
