package quitebetter.mixin.core.item;

import quitebetter.core.item.ModItems;
import quitebetter.core.projectile.ProjectileArrowTorch;
import quitebetter.core.util.PlayerUtil;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.entity.projectile.ProjectileArrowGolden;
import net.minecraft.core.entity.projectile.ProjectileArrowPurple;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBow;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(
	value = {ItemBow.class},
	remap = false
)
public class ItemBowMixin {
	@Inject(
		method = {"onUseItem"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void onUseItem(ItemStack itemstack, World world, Player entityplayer, CallbackInfoReturnable<ItemStack> cir) {
		Random rand = new Random();
		Item item = entityplayer.getNextArrow();
		if (item!=null) {
			PlayerUtil.consumeBowAmmo(entityplayer);
			itemstack.damageItem(1, entityplayer);
			world.playSoundAtEntity(entityplayer, entityplayer, "random.bow", 0.3F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			//ARROW CASES
			if (!world.isClientSide) {
				if (item.equals(Items.AMMO_ARROW)) {
					world.entityJoinedWorld(new ProjectileArrow(world, entityplayer, true, 0));
				} else if (item.equals(Items.AMMO_ARROW_GOLD)) {
					world.entityJoinedWorld(new ProjectileArrowGolden(world, entityplayer, true));
				} else if (item.equals(Items.AMMO_ARROW_PURPLE)) {
					world.entityJoinedWorld(new ProjectileArrowPurple(world, entityplayer, false));
				} else if (item.equals(ModItems.AMMO_ARROW_TORCH)) {
					world.entityJoinedWorld(new ProjectileArrowTorch(world, entityplayer, true));
				}
			}
		}
		cir.setReturnValue(itemstack);
		cir.cancel();
	}
}
