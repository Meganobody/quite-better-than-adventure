package quitebetter.mixin.core.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFishingRod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(value = {ItemFishingRod.class}, remap = false)
public class ItemFishingRodMixin {
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/ItemFishingRod;setMaxDamage(I)Lnet/minecraft/core/item/Item;"))
	private Item itemFishingRod(ItemFishingRod instance, int i, Operation<Item> original, String name, String namespaceId, int id) {
		if (Objects.equals(name, "tool_steel_fishingrod")) return original.call(instance, 588);
		return original.call(instance, i);
	}
}
