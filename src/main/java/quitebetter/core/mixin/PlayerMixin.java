package quitebetter.core.mixin;

import quitebetter.core.item.ModItems;
import quitebetter.core.util.PlayerUtil;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = {Player.class},
	remap = false
)
public class PlayerMixin {
	@Inject(
		method = {"getNextArrow"},
		at = @At("RETURN"),
		cancellable = true
	)
	public void getNextArrow(CallbackInfoReturnable<Item> cir) {
		Player player = (Player)(Object)this;
		Item nextArrow = null;
		ItemStack quiverSlot = player.inventory.armorItemInSlot(2);
		/// PRIORITY: HOTBAR > QUIVER > OUT OF HOTBAR
		//HOTBAR
		if (PlayerUtil.hasItemInHotbar(player,Items.AMMO_ARROW_GOLD)) {
			nextArrow = Items.AMMO_ARROW_GOLD;
		} else if (PlayerUtil.hasItemInHotbar(player,Items.AMMO_ARROW)) {
			nextArrow = Items.AMMO_ARROW;
		} else if (PlayerUtil.hasItemInHotbar(player,ModItems.AMMO_ARROW_TORCH)) {
			nextArrow = ModItems.AMMO_ARROW_TORCH;
		} else {
			//QUIVER
			if (quiverSlot != null && quiverSlot.itemID == Items.ARMOR_QUIVER_GOLD.id) {
				nextArrow = Items.AMMO_ARROW_PURPLE;
			} else if (quiverSlot != null && quiverSlot.itemID == Items.ARMOR_QUIVER.id && quiverSlot.getMetadata() < quiverSlot.getMaxDamage()) {
				nextArrow = Items.AMMO_ARROW;
			} else {
				//OUT OF HOTBAR
				if (PlayerUtil.hasItemOutOfHotbar(player,Items.AMMO_ARROW_GOLD)) {
					nextArrow = Items.AMMO_ARROW_GOLD;
				} else if (PlayerUtil.hasItemOutOfHotbar(player, Items.AMMO_ARROW)) {
					nextArrow = Items.AMMO_ARROW;
				} else if (PlayerUtil.hasItemOutOfHotbar(player, ModItems.AMMO_ARROW_TORCH)) {
					nextArrow = ModItems.AMMO_ARROW_TORCH;
				}
			}
		}
		cir.setReturnValue(nextArrow);
	}
}
