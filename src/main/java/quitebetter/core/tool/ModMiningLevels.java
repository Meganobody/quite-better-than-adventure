package quitebetter.core.tool;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import quitebetter.core.block.ModBlocks;

public class ModMiningLevels {
	public static void Setup() {
		ItemToolPickaxe.miningLevels.put(ModBlocks.OBSIDIAN_POLISHED, 3);
		ItemToolPickaxe.miningLevels.put(ModBlocks.BRICK_OBSIDIAN, 3);
	}
}
