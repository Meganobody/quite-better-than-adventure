package quitebetter.core.mixin;


import quitebetter.core.util.ILeftClickable;
import quitebetter.core.util.IRightClickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = {PlayerController.class},
	remap = false
)
public class PlayerControllerMixin {
	@Final
	@Shadow
	protected Minecraft mc;

	@Inject(
		method = {"destroyBlock"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/controller/PlayerController;setMineBlock(Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;)V",
			shift = At.Shift.BEFORE
		),
		cancellable = true
	)
	public void destroyBlock(int x, int y, int z, Side side, Player player, CallbackInfoReturnable<Boolean> cir) {
		PlayerController controller = (PlayerController)(Object)this;
		World world = mc.currentWorld;
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof ILeftClickable)) {
			ILeftClickable logic = (ILeftClickable) block.getLogic();
			if (logic.preventsBreaking(world,x,y,z,player,side)) {
				logic.onBlockLeftClicked(world,x,y,z,player,side);
				cir.cancel();
			}
		}
	}

	@Inject(
		method = {"placeItemStackOnTile"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void placeItemStackOnTile(Player player, World world, ItemStack itemstack, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof IRightClickable)) {
			IRightClickable logic = (IRightClickable) block.getLogic();
			if (logic.preventsInteraction(world,x,y,z,player,side)) {
				logic.onBlockRightClicked(world,x,y,z,player,side);
				cir.cancel();
			}
		}
	}
	@Inject(
		method = {"useOrPlaceItemStackOnTile"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void useOrPlaceItemStackOnTile(Player player, World world, ItemStack itemstack, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof IRightClickable)) {
			IRightClickable logic = (IRightClickable) block.getLogic();
			if (logic.preventsInteraction(world,x,y,z,player,side)) {
				logic.onBlockRightClicked(world,x,y,z,player,side);
				cir.cancel();
			}
		}
	}
}
