package quitebetter.core.mixin.glued;

import quitebetter.core.tileentity.TileEntityGlued;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicNote;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(
	value = {BlockLogicNote.class},
	remap = false
)
public class BlockLogicNoteMixin {
	@Inject(
		method = {"<init>"},
		at = @At("TAIL")
	)
	public void BlockLogicNote(Block block, CallbackInfo ci) {
		block.withEntity(TileEntityGlued::new);
	}

	@Inject(
		method = {"onBlockRightClicked"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Random rand = new Random();
		TileEntity tile = world.getTileEntity(x,y,z);
		if (player.getHeldItem()!=null && player.getHeldItem().getItem().equals(Items.SLIMEBALL) && (tile instanceof TileEntityGlued) && !((TileEntityGlued)tile).Glued) {
			((TileEntityGlued)tile).Glued = true;
			cir.setReturnValue(true);
			for(int j = 0; j < 4; ++j) {
				world.spawnParticle("item", x+rand.nextFloat(), y+rand.nextFloat(), z+rand.nextFloat(), (double)(rand.nextInt(11)-5)/10, (double)(rand.nextInt(11)-5)/10, (double)(rand.nextInt(11)-5)/10, Items.SLIMEBALL.id);
			}
			world.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z, "mob.slimeattack", 0.25F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
		} else if (tile!=null && ((TileEntityGlued)tile).Glued) {
			cir.cancel();
		}
	}
	@Inject(
		method = {"onActivatorInteract"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void onActivatorInteract(World world, int x, int y, int z, TileEntityActivator activator, Direction direction, CallbackInfo ci) {
		Random rand = new Random();
		TileEntity tile = world.getTileEntity(x,y,z);
		if (tile!=null && ((TileEntityGlued)tile).Glued) {
			ci.cancel();
		}
	}
}
