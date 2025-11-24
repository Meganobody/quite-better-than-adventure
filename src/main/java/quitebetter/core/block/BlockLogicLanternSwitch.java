package quitebetter.core.block;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import quitebetter.core.util.IRightClickable;

public class BlockLogicLanternSwitch extends BlockLogicLanternRedstone {
    protected final boolean hasInput;

    public BlockLogicLanternSwitch(Block<?> block, boolean isActive, boolean hasInput) {
		super(block, isActive);
        this.hasInput = hasInput;
	}
    public int tickDelay() {
		return 4;
   	}
    @Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(ModBlocks.LANTERN_SWITCH)};
	}
    public static Block<?> GetBlock(boolean isActive, boolean hasInput)
    {
        if (hasInput)
        {
            return isActive ? ModBlocks.LANTERN_SWITCH_INPUT : ModBlocks.LANTERN_SWITCH_IDLE_INPUT;
        }
        else
        {
            return isActive ? ModBlocks.LANTERN_SWITCH : ModBlocks.LANTERN_SWITCH_IDLE;
        }
    }
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
      boolean hasInput = this.getInputSignal(world, x, y, z);
      boolean isActive = this.isActive;
      if (!this.hasInput && hasInput)
      {
        isActive = !isActive;
        world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.2f, 0.5f);
      }
      world.setBlockAndMetadataWithNotify(x, y, z, GetBlock(isActive, hasInput).id(), world.getBlockMetadata(x, y, z));
   	}
    @Override
   	protected @Nullable ItemStack putItem(World world, int x, int y, int z, @Nullable ItemStack itemStack, @Nullable Entity entity)
    {
        world.playSoundEffect(entity, SoundCategory.WORLD_SOUNDS, x, y, z, "random.click", 0.2f, 1f);
        world.setBlockAndMetadataWithNotify(x, y, z, GetBlock(!isActive, hasInput).id(), world.getBlockMetadata(x, y, z));
        return null;
    }
    @Override
	protected boolean canPutItem(World world, int x, int y, int z, @Nullable ItemStack itemStack) {
		return true;
	}
}
