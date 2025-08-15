package quitebetter.mixin.core.item;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemFlag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = {ItemFlag.class},
	remap = false
)
public class ItemFlagMixin {
	@Inject(
		method = {"onUseItemOnBlock"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/world/World;playBlockSoundEffect(Lnet/minecraft/core/entity/Entity;DDDLnet/minecraft/core/block/Block;Lnet/minecraft/core/enums/EnumBlockSoundEffectType;)V",
			shift = At.Shift.BEFORE
		),
		cancellable = true
	)
	private void onUseItemOnBlock(ItemStack stack, Player entityplayer, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
//		Block<?> block = world.getBlock(x-side.getOffsetX(),y-side.getOffsetY(),z-side.getOffsetZ());
//		if (block==null || side.isHorizontal() && !block.isCubeShaped()) {
//			cir.cancel();
//		}
	}

	@Inject(
		method = {"onUseItemOnBlock"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/world/World;setBlockAndMetadataWithNotify(IIIII)Z",
			shift = At.Shift.AFTER
		)
	)
	private void onUseItemOnBlockDirection(ItemStack stack, Player entityplayer, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlock(x, y, z)!=null && world.getBlock(x, y, z).equals(Blocks.FLAG)) {
			if ( side.isHorizontal()) {
				world.setBlockMetadata(x,y,z,side.getDirection().getId());
			}
		}
	}
}
