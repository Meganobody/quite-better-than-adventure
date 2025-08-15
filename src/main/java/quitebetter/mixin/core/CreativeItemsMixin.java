package quitebetter.mixin.core;

import quitebetter.core.block.ModBlocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;


@Mixin(
	value = {MenuInventoryCreative.class},
	remap = false
)
public class CreativeItemsMixin {
	@Shadow
	public static List<ItemStack> creativeItems;

	@Shadow
	public static int creativeItemsCount;
	@Unique
	private static int extraCount = 0;

	@Inject(
		method = "<clinit>",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;hasTag(Lnet/minecraft/core/data/tag/Tag;)Z"),
		locals = LocalCapture.CAPTURE_FAILSOFT

	)
	private static void addBlocks(CallbackInfo ci, int count, int id) {
		if (id == ModBlocks.HAZARD_PAINTED.id()) {
			int before = creativeItems.size();
			for(int i = 0; i < 16; ++i) {
				creativeItems.add(new ItemStack(ModBlocks.HAZARD_PAINTED, 1, i));
				++count;
			}
			extraCount += creativeItems.size() - before;
		} else if (id == ModBlocks.CRATE_PAINTED.id()) {
			int before = creativeItems.size();
			for(int i = 1; i < 16; ++i) {
				creativeItems.add(new ItemStack(ModBlocks.CRATE_PAINTED, 1, i));
				++count;
			}
			extraCount += creativeItems.size() - before;
		} if (id == ModBlocks.BLOCK_IRON_CORRUGATED_PAINTED.id()) {
			int before = creativeItems.size();
			for(int i = 0; i < 16; ++i) {
				creativeItems.add(new ItemStack(ModBlocks.BLOCK_IRON_CORRUGATED_PAINTED, 1, i << 4));
				++count;
			}
			extraCount += creativeItems.size() - before;
		}
	}

	@Inject(
		method = "<clinit>",
		at = @At(value = "FIELD", target = "Lnet/minecraft/core/player/inventory/menu/MenuInventoryCreative;creativeItemsCount:I", shift = At.Shift.AFTER)
	)
	private static void addUpItemCount(CallbackInfo ci) {
		creativeItemsCount += extraCount;
	}
}
