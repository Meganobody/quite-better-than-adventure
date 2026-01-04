package quitebetter.core.block;

import java.util.Random;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import quitebetter.core.util.IRightClickable;

public class BlockLogicLanternLatch extends BlockLogicLanternRedstone implements IRightClickable {
    protected final boolean hasInput;

    public BlockLogicLanternLatch(Block<?> block, boolean isActive, boolean hasInput) {
		super(block, isActive);
        this.hasInput = hasInput;
	}
    @Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(GetBlock(true, false))};
	}
    public Block<?> GetBlock(boolean isActive, boolean hasInput)
    {
        if (hasInput)
        {
            return isActive ? ModBlocks.LANTERN_LATCH_INPUT : ModBlocks.LANTERN_LATCH_IDLE_INPUT;
        }
        else
        {
            return isActive ? ModBlocks.LANTERN_LATCH : ModBlocks.LANTERN_LATCH_IDLE;
        }
    }
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
     	boolean hasInput = this.getInputSignal(world, x, y, z);
     	boolean isActive = this.isActive;
		if (!this.hasInput && hasInput)
		{
			isActive = !isActive;
			//world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.2f, 0.5f);
		}
     	world.setBlockAndMetadataWithNotify(x, y, z, GetBlock(isActive, hasInput).id(), world.getBlockMetadata(x, y, z));
   	}

	@Override
	public boolean preventsInteraction(World world, int x, int y, int z, Player player, Side side) {
		return player.isSneaking();
	}

	@Override
	public void onBlockRightClicked(World world, int x, int y, int z, Player player, Side side) {
		//world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.2f, 1f);
		world.setBlockAndMetadataWithNotify(x, y, z, GetBlock(!isActive, hasInput).id(), world.getBlockMetadata(x, y, z));
	}
}
