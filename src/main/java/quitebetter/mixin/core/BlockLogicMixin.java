package quitebetter.mixin.core;

import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(
	value = {BlockLogic.class},
	remap = false
)
public class BlockLogicMixin {
	@Inject(
		method = {"onBlockPlacedByMob"},
		at = @At("HEAD")
	)
	public void onBlockPlacedOnSide(World world, int x, int y, int z, Side side, Mob mob, double xPlaced, double yPlaced, CallbackInfo ci) {
	}

	@Inject(
		method = {"onNeighborBlockChange"},
		at = @At("HEAD")
	)
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId, CallbackInfo ci) {
	}

	@Inject(
		method = {"getBlockBoundsFromState"},
		at = @At("RETURN"),
		cancellable = true
	)
	public void getBlockBoundsFromState(WorldSource world, int x, int y, int z, CallbackInfoReturnable<AABB> cir) {
		if (Objects.equals(world.getBlock(x, y, z), Blocks.FLAG)) {
			Direction dir = Direction.getDirectionById(world.getBlockMetadata(x,y,z));
			if (dir.isHorizontal()) {
				float f = 0.125F;
				float f1 = 1.0F;
				float dx = dir.getOffsetX()*0.4F;
				float dz = dir.getOffsetZ()*0.4F;
				cir.setReturnValue(AABB.getTemporaryBB(
					(double)(0.5F - f)-dx,
					(double)0.0F,
					(double)(0.5F - f)-dz,
					(double)(0.5F + f)-dx,
					(double)f1,
					(double)(0.5F + f)-dz
				));
			}
		}
	}
}
