package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockLogicLanternClock extends BlockLogicLanternPulse {

	public BlockLogicLanternClock(Block<?> block, boolean isActive, boolean hasInput) {
		super(block, isActive, hasInput);
	}
	@Override
	public Block<?> GetBlock(boolean isActive, boolean hasInput)
	{
		if (hasInput)
		{
			return isActive ? ModBlocks.LANTERN_CLOCK_INPUT : ModBlocks.LANTERN_CLOCK_IDLE_INPUT;
		}
		else
		{
			return isActive ? ModBlocks.LANTERN_CLOCK : ModBlocks.LANTERN_CLOCK_IDLE;
		}
	}
	@Override
	public int tickDelay() {
		return 10;
	}
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		boolean hasInput = this.getInputSignal(world, x, y, z);
		boolean isActive = this.isActive;
		if (this.hasInput)
		{
			isActive = !isActive;
			//world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.2f, 1.5f + (isActive ? 0.5f : -0.2f));
		}
		else isActive = false;
		world.setBlockAndMetadataWithNotify(x, y, z, GetBlock(isActive, hasInput).id(), world.getBlockMetadata(x, y, z));
	}
}
