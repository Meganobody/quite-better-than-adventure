package quitebetter.mixin.core.glued;

import quitebetter.core.tileentity.TileEntityGlued;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTrapDoor;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.material.Material;
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
	value = {BlockLogicTrapDoor.class},
	remap = false
)
public class BlockLogicTrapdoorMixin {
	@Inject(
		method = {"<init>"},
		at = @At("TAIL")
	)
	public void BlockLogicTrapDoor(Block block, Material material, CallbackInfo ci) {
		block.withEntity(TileEntityGlued::new);
	}

	@Inject(
		method = {"onBlockRightClicked"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> ci) {
		TileEntity tile = world.getTileEntity(x,y,z);
		if (tile!=null && ((TileEntityGlued)tile).isGlued()) { ci.cancel(); }
	}
	@Inject(
		method = {"onActivatorInteract"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void onActivatorInteract(World world, int x, int y, int z, TileEntityActivator activator, Direction direction, CallbackInfo ci) {
		TileEntity tile = world.getTileEntity(x,y,z);
		if (tile!=null && ((TileEntityGlued)tile).isGlued()) { ci.cancel(); }
	}
	@Inject(
		method = {"onNeighborBlockChange"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId, CallbackInfo ci) {
		TileEntity tile = world.getTileEntity(x,y,z);
		if (tile!=null && ((TileEntityGlued)tile).isGlued()) { ci.cancel(); }
	}
}
