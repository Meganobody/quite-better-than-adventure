package quitebetter.mixin.client;


import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.client.player.controller.PlayerControllerSP;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quitebetter.core.util.ILeftClickable;

import static quitebetter.core.ModCore.LOGGER;

@Mixin(
	value = {PlayerControllerSP.class},
	remap = false
)
public class PlayerControllerSPMixin {

	@Inject(
		method = {"destroyBlock"},
		at = @At("HEAD"),
		cancellable = true
	)
	//NON-CREATIVE CASE
	public void startDestroyBlock(int x, int y, int z, Side side, Player player, CallbackInfoReturnable<Boolean> cir) {
		PlayerController controller = (PlayerController)(Object)this;
		World world = controller.mc.currentWorld;
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof ILeftClickable)) {
			ILeftClickable logic = (ILeftClickable) block.getLogic();
			if (logic.preventsBreaking(world,x,y,z,player,side)) {
				cir.cancel();
			}
		}
	}

}
