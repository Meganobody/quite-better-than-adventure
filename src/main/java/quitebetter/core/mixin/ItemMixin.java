package quitebetter.core.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(
	value = {Item.class},
	remap = false
)
public class ItemMixin {
	@Inject(
		method = {"onUseItemOnBlock"},
		at = @At("RETURN"),
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true
	)
	public void onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Block block = world.getBlock(x,y,z);
	}
}
