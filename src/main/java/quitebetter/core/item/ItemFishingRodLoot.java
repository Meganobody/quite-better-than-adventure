package quitebetter.core.item;

import quitebetter.core.block.ModBlocks;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;

import java.util.Random;

public class ItemFishingRodLoot {
	public static Item GetWaterLoot() {
		Random random = new Random();
		Item[] opts = {
			Items.FOOD_FISH_RAW,
			Items.FOOD_FISH_RAW,
			Items.FOOD_FISH_RAW,
			Items.FOOD_FISH_RAW,
			Items.FOOD_FISH_RAW,
			Items.FOOD_FISH_RAW,
			ModItems.SEASHELL,
			ModItems.SEASHELL,
			ModItems.SEASHELL,
			ModItems.SEASHELL,
			ModItems.SEASHELL,
			Blocks.ALGAE.asItem(),
			Blocks.ALGAE.asItem(),
			Blocks.ALGAE.asItem(),
			Blocks.MUD.asItem(),
			Blocks.MUD.asItem(),
			Blocks.SPONGE_WET.asItem()
		};
		return (opts[random.nextInt(opts.length)]);
	}

	public static Item GetLavaLoot() {
		Random random = new Random();
		Item[] opts = {
			ModBlocks.GEODE.asItem(),
			ModBlocks.GEODE.asItem(),
			ModBlocks.GEODE.asItem(),
			Items.BONE,
			Blocks.OBSIDIAN.asItem(),
			Blocks.OBSIDIAN.asItem(),
		};
		return (opts[random.nextInt(opts.length)]);
	}
}
