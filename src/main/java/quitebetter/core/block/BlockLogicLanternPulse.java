package quitebetter.core.block;

import java.util.Random;

import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class BlockLogicLanternPulse extends BlockLogicLanternLatch {
	public BlockLogicLanternPulse(Block<?> block, boolean isActive, boolean hasInput) {
		super(block, isActive, hasInput);
	}
	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(GetBlock(false, false))};
	}
	@Override
	public Block<?> GetBlock(boolean isActive, boolean hasInput)
	{
		if (hasInput)
		{
			return isActive ? ModBlocks.LANTERN_PULSE_INPUT : ModBlocks.LANTERN_PULSE_IDLE_INPUT;
		}
		else
		{
			return isActive ? ModBlocks.LANTERN_PULSE : ModBlocks.LANTERN_PULSE_IDLE;
		}
	}
	public int tickDelay() {
		return isActive ? 10 : 2;
	}
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		boolean hasInput = this.getInputSignal(world, x, y, z);
		boolean isActive = this.isActive;
		if (!this.hasInput && hasInput)
		{
			isActive = true;
			world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.2f, 2f);
		}
		else isActive = false;
		world.setBlockAndMetadataWithNotify(x, y, z, GetBlock(isActive, hasInput).id(), world.getBlockMetadata(x, y, z));
	}
}
