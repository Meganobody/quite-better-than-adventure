package quitebetter.mixin.core;

import quitebetter.core.tileentity.TileEntityCrate;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(
	value = {TileEntityBasket.class},
	remap = false
)
public class TileEntityBasketMixin {
	@Shadow
	private Map<TileEntityBasket.BasketEntry, Integer> contents;

	@Inject(
		method = {"importItemStack"},
		at = @At("HEAD"),
		cancellable = true
	)
	private void importItemStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		TileEntityBasket tile = (TileEntityBasket)(Object)this;
		World world = tile.worldObj;
		if (world.getTileEntity(tile.x,tile.y-1,tile.z)!=null && world.getTileEntity(tile.x,tile.y-1,tile.z) instanceof TileEntityCrate) {
			TileEntityCrate crate = (TileEntityCrate) world.getTileEntity(tile.x,tile.y-1,tile.z);
			if (crate.canTakeStack(stack) && crate.pushStack(stack)!=null) {
				cir.setReturnValue(false);
			}
		}
	}
}
