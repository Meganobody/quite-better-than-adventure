package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Random;

public class BlockLogicLanternCoal extends BlockLogic {

	public BlockLogicLanternCoal(Block<?> block, Material material) {
		super(block, material);
		this.setBlockBounds(0.2F,0F,0.2F,0.8F,1F,0.8F);
	}

	public boolean isSolidRender() {
		return false;
	}

	public boolean isCubeShaped() {
		return false;
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.getBlock(x,y+1,z)!=null || world.getBlock(x,y-1,z)!=null;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), (TileEntity)null, (Player)null);
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this.block)};
	}

	public int getPistonPushReaction(World world, int x, int y, int z) {
		return Material.PISTON_DESTROY_ON_PUSH;
	}

	public void animationTick(World world, int x, int y, int z, Random rand) {
		double xPos = (double)x + (double)0.5F;
		double yPos = (double)y + 0.6;
		double zPos = (double)z + (double)0.5F;
		world.spawnParticle("smoke", xPos, yPos, zPos, (double)0.0F, (double)0.0F, (double)0.0F, 0);
		world.spawnParticle("flame", xPos, yPos, zPos, (double)0.0F, (double)0.0F, (double)0.0F, 0);
	}
}
