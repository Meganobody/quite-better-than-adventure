package quitebetter.mixin.core.item;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quitebetter.core.tileentity.TileEntityCrate;
import quitebetter.core.tileentity.TileEntityGlued;

import java.util.Random;

@Mixin(
	value = {Item.class},
	remap = false
)
public class ItemMixin {
	@Inject(
		method = {"onUseItemOnBlock"},
		at = @At("RETURN")
	)
	public void onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		TileEntity tile = world.getTileEntity(x,y,z);
		if (tile instanceof TileEntityGlued || tile instanceof TileEntityCrate) {
			if (entityplayer.getHeldItem()!=null && entityplayer.getHeldItem().getItem().equals(Items.SLIMEBALL)) {
				((TileEntityGlued) tile).applyGlue();
			}
		}
	}

	@Inject(
		method = {"onUseByActivator"},
		at = @At("RETURN")
	)
	public void onUseByActivator(ItemStack itemStack, TileEntityActivator activatorBlock, World world, Random random, int x, int y, int z, double offX, double offY, double offZ, Direction direction, CallbackInfo ci) {
		TileEntity tile = world.getTileEntity(x+direction.getOffsetX(),y+direction.getOffsetY(),z+direction.getOffsetZ());
		if (tile instanceof TileEntityGlued || tile instanceof TileEntityCrate) {
			if (activatorBlock.getItem(activatorBlock.stackSelector)!=null && activatorBlock.getItem(activatorBlock.stackSelector).getItem().equals(Items.SLIMEBALL)) {
				((TileEntityGlued) tile).applyGlue();
			}
		}
	}
}
