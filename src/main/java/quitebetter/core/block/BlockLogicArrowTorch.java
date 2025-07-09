package quitebetter.core.block;

import quitebetter.core.item.ModItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTorch;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockLogicArrowTorch extends BlockLogicTorch {
	public BlockLogicArrowTorch(Block<?> block) {
		super(block);
	}

	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{new ItemStack(ModItems.AMMO_ARROW_TORCH)} : null;
	}

}
