package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockLogicCratePainted extends BlockLogicCrate implements IPainted {
	public BlockLogicCratePainted(Block<?> block, Material material) {
		super(block, material);
	}

	public int getPlacedBlockMetadata(@Nullable Player player, ItemStack stack, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
		return stack.getMetadata() & 15;
	}

	public DyeColor fromMetadata(int meta) {
		return DyeColor.colorFromBlockMeta(meta);
	}

	public int toMetadata(DyeColor color) {
		return color.blockMeta;
	}

	public int stripColorFromMetadata(int meta) {
		return 0;
	}

	public void removeDye(World world, int x, int y, int z) {
		world.setBlockWithNotify(x, y, z, ModBlocks.CRATE.id());
	}

	@Override
	public void setColor(World world, int x, int y, int z, DyeColor color) {
		world.setBlockMetadataWithNotify(x,y,z,toMetadata(color));
	}
}
