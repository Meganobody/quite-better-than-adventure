package quitebetter.core.item;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import quitebetter.core.ModConfig;
import quitebetter.core.block.ModBlocks;
import net.minecraft.core.item.*;
import net.minecraft.core.item.tag.ItemTags;
import turniplabs.halplibe.helper.ItemBuilder;

import static quitebetter.core.ModCore.MOD_ID;

public class ModItems {
	public static Item CLIMBING_HOOK;
	public static Item WRENCH;
	public static Item META_WRENCH;
	public static Item SEASHELL;
	public static Item TOOL_STEEL_FISHINGROD;
	public static Item PEDESTAL;
	public static Item AMMO_ARROW_TORCH;
	public static Item SACK;
	public static Item GLOVE_LEATHER;
	public static Item GLOVE_STEEL;

	public static int startingItemId = ModConfig.startingItemId;

	public static String name(String name) {
		return name.replace('_','.');
	}
	public static Integer id(String name) {
		try {
			return ModConfig.cfg.getInt(ModConfig.ItemIDs+"."+name);
		}catch (NullPointerException e) {
			return startingItemId++;
		}
	}
	public static String key(String name) {
		return MOD_ID+":item/"+(name.replace('.','_'));
	}

	public static void Setup() {
		//CLIMBING HOOK

		CLIMBING_HOOK = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.setMaxDamage(250)
			.build(
				new ItemHook(
					name("climbing_hook"),
					key("climbing_hook"),
					id("CLIMBING_HOOK")
				)
			);

		//WRENCH
		WRENCH = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.build(
				new ItemWrench(
					name("wrench"),
					key("wrench"),
					id("WRENCH")
				)
			);

		//META WRENCH
		META_WRENCH = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.build(
				new ItemMetaWrench(
					name("meta_wrench"),
					key("meta_wrench"),
					id("META_WRENCH")
				)
			);

		//SEASHELL
		SEASHELL = new ItemBuilder(MOD_ID)
			.build(
				new ItemSeashell(
					name("seashell"),
					key("seashell"),
					id("SEASHELL")
				)
			);

		//STEEL FISHING ROD
		TOOL_STEEL_FISHINGROD = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.build(
				new ItemFishingRod(
					name("tool_steel_fishingrod"),
					key("tool_steel_fishingrod"),
					id("TOOL_STEEL_FISHINGROD")
				)
			);
		if (FabricLoaderImpl.INSTANCE.isModLoaded("stardew")) {
			TOOL_STEEL_FISHINGROD = TOOL_STEEL_FISHINGROD.withTags(ItemTags.NOT_IN_CREATIVE_MENU);
		}

		//PEDESTAL
		PEDESTAL = new ItemBuilder(MOD_ID)
			.build(
				new ItemPlaceable(
					name("pedestal"),
					key("pedestal"),
					id("PEDESTAL"),
					ModBlocks.PEDESTAL
				)
			);
		//AMMO ARROW TORCH
		AMMO_ARROW_TORCH = new ItemBuilder(MOD_ID)
			.build(
				new ItemArrowTorch(
					name("ammo_arrow_torch"),
					key("ammo_arrow_torch"),
					id("AMMO_ARROW_TORCH")
				)
			);

		//SACK
		SACK = new ItemBuilder(MOD_ID)
			.build(
				new ItemSack(
					name("sack"),
					key("sack"),
					id("SACK")
				)
			);
	}
}
