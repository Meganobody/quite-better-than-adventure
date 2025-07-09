package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockLogicGeode extends BlockLogic {
	public static class GeodeDropEntry {
		Item item;int minCount;int maxCount;
		public GeodeDropEntry(Item item, int minCount, int maxCount) {
			this.item = item;
			this.minCount = minCount;
			this.maxCount = maxCount;
		}
	}
	public static GeodeDropEntry getDrop() {
		Random random = new Random();
		GeodeDropEntry[] opts = {
			new GeodeDropEntry(Items.QUARTZ,6,16),
			new GeodeDropEntry(Items.QUARTZ,6,16),
			new GeodeDropEntry(Items.QUARTZ,6,16),
			new GeodeDropEntry(Items.ORE_RAW_GOLD, 6,16),
			new GeodeDropEntry(Items.ORE_RAW_GOLD, 6,16),
			new GeodeDropEntry(Items.ORE_RAW_GOLD, 6,16),
			new GeodeDropEntry(Items.FLINT, 4,16),
			new GeodeDropEntry(Items.FLINT, 4,16),
			new GeodeDropEntry(Items.FLINT, 4,16),
			new GeodeDropEntry(Items.DIAMOND, 1,1),
		};
		return (opts[random.nextInt(opts.length)]);
	}

	public BlockLogicGeode(Block<?> block, Material material) {
		super(block, material);
	}

	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		if (dropCause==EnumDropCause.PISTON_CRUSH) {
			Random rand = new Random();
			GeodeDropEntry drop = getDrop();
			EntityItem.enableItemClumping = true;
			return new ItemStack[]{new ItemStack(drop.item,drop.minCount+rand.nextInt(drop.maxCount))};
		}
		return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{new ItemStack(this.block)} : null;
	}
}
