package quitebetter.mixin.core;

import net.minecraft.core.block.BlockLogicMushroom;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.piston.BlockLogicPistonHead;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quitebetter.core.block.BlockLogicPistonBaseFine;
import quitebetter.core.block.ModBlocks;

import static net.minecraft.core.block.piston.BlockLogicPistonHead.getPistonType;

@Mixin(
	value = {BlockLogicPistonHead.class},
	remap = false
)
public class BlockLogicPistonHeadMixin {
	@Inject(
		method = {"getBreakResult"},
		at = @At("RETURN"),
		cancellable = true
	)
	public void getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity, CallbackInfoReturnable<ItemStack[]> cir)
	{
		if (cir.getReturnValue() == null && dropCause == EnumDropCause.PICK_BLOCK && getPistonType(meta) == BlockLogicPistonBaseFine.TYPE_FINE)
		{
			cir.setReturnValue(new ItemStack[]{new ItemStack(ModBlocks.PISTON_BASE_FINE)});
		}
	}
}
