package quitebetter.mixin.client;


import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quitebetter.core.item.ModItems;
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
import quitebetter.core.util.PlayerUtil;
import turniplabs.halplibe.helper.EnvironmentHelper;

@Mixin(
	value = {PlayerController.class},
	remap = false
)
public class PlayerControllerMixin {
	@Final
	@Shadow
	protected Minecraft mc;

	@Inject(
		method = {"continueDestroyBlock"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void continueDestroyBlock(int x, int y, int z, Side side, double xHit, double yHit, CallbackInfo ci) {
		PlayerController controller = (PlayerController)(Object)this;
		World world = mc.currentWorld;
		Player player = mc.thePlayer;
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof ILeftClickable)) {
			ILeftClickable logic = (ILeftClickable) block.getLogic();
			if (logic.preventsBreaking(world,x,y,z,player,side)) {
				ci.cancel();
			}
		}
	}

	@Inject(
		method = {"startDestroyBlock"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/controller/PlayerController;sendStartDigPacket(IIILnet/minecraft/core/util/helper/Side;DD)V",
			shift = At.Shift.AFTER,
			ordinal = 1
		),
		cancellable = true
	)
	//NON-CREATIVE CASE
	public void startDestroyBlock(int x, int y, int z, Side side, double xHit, double yHit, boolean repeat, CallbackInfo cir) {
		PlayerController controller = (PlayerController)(Object)this;
		World world = mc.currentWorld;
		Player player = mc.thePlayer;
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof ILeftClickable)) {
			ILeftClickable logic = (ILeftClickable) block.getLogic();
			if (logic.preventsBreaking(world,x,y,z,player,side)) {
				if (EnvironmentHelper.isSinglePlayer()) {
					logic.onBlockLeftClicked(world,x,y,z,player,side);
				}
				cir.cancel();
			}
		}
	}

	@Inject(
		method = {"startDestroyBlock"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/controller/PlayerController;destroyBlock(IIILnet/minecraft/core/util/helper/Side;Lnet/minecraft/core/entity/player/Player;)Z",
			shift = At.Shift.BEFORE
		),
		cancellable = true
	)
	public void startDestroyBlockWithoutAnimation(int x, int y, int z, Side side, double xHit, double yHit, boolean repeat, CallbackInfo cir) {
		PlayerController controller = (PlayerController)(Object)this;
		World world = mc.currentWorld;
		Player player = mc.thePlayer;
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof ILeftClickable)) {
			ILeftClickable logic = (ILeftClickable) block.getLogic();
			if (logic.preventsBreaking(world,x,y,z,player,side)) {
				if (EnvironmentHelper.isSinglePlayer()) {
					logic.onBlockLeftClicked(world,x,y,z,player,side);
				}
				controller.sendStartDigPacket(x, y, z, side, xHit, yHit);
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

	@Inject(
		method = {"getBlockReachDistance"},
		at = @At("RETURN"),
		cancellable = true
	)
	public void getBlockReachDistance(CallbackInfoReturnable<Float> cir) {
		float reach = 0F;
		if (PlayerUtil.isArmorItemThis(mc.thePlayer,2,ModItems.GLOVE_LEATHER)) {
			reach += 3F;
		}
		cir.setReturnValue(mc.thePlayer.getGamemode().getBlockReachDistance()+reach);
	}
}
