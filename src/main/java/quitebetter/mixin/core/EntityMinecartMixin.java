package quitebetter.mixin.core;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityFishingBobber;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.data.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.xml.crypto.Data;

import static quitebetter.core.ModCore.LOGGER;

@Mixin(
	value = {EntityMinecart.class},
	remap = false
)
public class EntityMinecartMixin {
	@Inject(
		method = {"interact"},
		at = @At("HEAD"),
		cancellable = true
	)
	private void interact(Player player, CallbackInfoReturnable<Boolean> cir) {
		EntityMinecart cart = (EntityMinecart)(Object)this;
		ItemStack heldstack = player.getHeldItem();
		if (heldstack!=null && heldstack.getItem().equals(Items.ROPE)) {
			CompoundTag data = heldstack.getData();
			Integer firstCart = data.getInteger("MinecartConnectionFirstID");
			player.swingItem();
			if (firstCart == 0) { //FIRST
				data.putInt("MinecartConnectionFirstID", cart.id);
			} else if (firstCart != cart.id) { //SECOND
				for (Entity entity : cart.world.getLoadedEntityList()) {
					if (entity!=null && ((Integer)entity.id).equals((Integer)firstCart)) {
						cart.x = entity.x;
						cart.y = entity.y;
						cart.z = entity.z;
						LOGGER.info(firstCart+" "+entity.id);
					}
				}
			} else {
				data.putInt("MinecartConnectionFirstID", 0);
			}
			cir.cancel();
		}
	}

	@Inject(
		method = {"tick"},
		at = @At("TAIL"),
		cancellable = true
	)
	private void tick(CallbackInfo ci) {
//		EntityMinecart cart = (EntityMinecart)(Object)this;
//		SynchedEntityData.DataItem parentid = cart.getEntityData().itemsById.get(2502);
//		if (parentid!=null) {
//			Integer parentid = (SynchedEntityData.DataItem) parentid
//			LOGGER.info(cart.id+" to "+parentid);
//			for (Entity entity : cart.world.getLoadedEntityList()) {
//				if (entity!=null && ((Integer)entity.id).equals(parentid)) {
//					cart.x = entity.x;
//					cart.y = entity.y;
//					cart.z = entity.z;
//				}
//			}
//		}
	}
}
