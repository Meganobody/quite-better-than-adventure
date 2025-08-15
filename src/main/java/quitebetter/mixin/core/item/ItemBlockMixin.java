package quitebetter.mixin.core.item;

import quitebetter.core.block.BlockLogicFan;
import quitebetter.core.block.ModBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(
	value = {ItemBlock.class},
	remap = false
)
public class ItemBlockMixin {
	@Inject(
		method = {"onUseItemOnBlock"},
		at = @At("HEAD"),
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true
	)
	public void onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Block block = world.getBlock(x,y,z);
		if (block!=null) {
			if (itemstack.getItem().equals(Blocks.COBBLE_NETHERRACK.asItem())) {
				if (block.equals(ModBlocks.FAN)) {
					BlockLogicFan logic = (BlockLogicFan) block.getLogic();
					logic.overrideSignal(world, x, y, z);
					logic.setActive(world, x, y, z, true);
					cir.setReturnValue(true);
				}
			} else if (itemstack.getItem().equals(Blocks.TORCH_REDSTONE_ACTIVE.asItem()) && !entityplayer.isSneaking()) {
				if (BlockLogicFan.isFan(block)) {
					BlockLogicFan logic = (BlockLogicFan) block.getLogic();
					logic.toggleInverted(world, x, y, z);
					cir.setReturnValue(true);
				}
			}
		}
	}

	@Inject(
		method = {"onUseByActivator"},
		at = @At("HEAD"),
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true
	)
	public void onUseByActivator(ItemStack itemStack, TileEntityActivator activatorBlock, World world, Random random, int blockX, int blockY, int blockZ, double offX, double offY, double offZ, Direction direction, CallbackInfo cir) {
		int x = blockX + direction.getOffsetX();
		int y = blockY + direction.getOffsetY();
		int z = blockZ + direction.getOffsetZ();
		Block<?> block = world.getBlock(x, y, z);
		if (itemStack.getItem().equals(Blocks.TORCH_REDSTONE_ACTIVE.asItem())) {
			if (BlockLogicFan.isFan(block)) {
				BlockLogicFan logic = (BlockLogicFan) block.getLogic();
				logic.toggleInverted(world, x, y, z);
				cir.cancel();
			}
		}
	}
}
