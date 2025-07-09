package quitebetter.core.item;

import quitebetter.core.block.ModBlocks;
import net.minecraft.core.item.*;
import net.minecraft.core.item.tag.ItemTags;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.ItemBuilder;

import static quitebetter.core.ModCore.MOD_ID;

public class ModItems {
	public static Item HOOK;
	public static Item WRENCH;
	public static Item META_WRENCH;
	public static Item SEASHELL;
	public static Item TOOL_STEEL_FISHINGROD;
	public static Item PEDESTAL;
	public static Item AMMO_ARROW_TORCH;
	public static Item SACK;

	private static Item finish(@NotNull Item item) {
		item.setKey(item.getKey()
			.substring("item.".length())
		);
		return item;
	}

	public static void Setup() {
		int startingItemId = 20000;
		//CLIMBING HOOK
		HOOK = finish(new ItemBuilder(MOD_ID)
			.setKey("item.climbinghook")
			.setStackSize(1)
			.setMaxDamage(250)
			.build(new ItemHook("climbinghook", MOD_ID+":item/climbinghook", startingItemId++)));
		//WRENCH
		WRENCH = finish(new ItemBuilder(MOD_ID)
			.setKey("item.wrench")
			.setStackSize(1)
			.setMaxDamage(250)
			.setTags(ItemTags.NOT_IN_CREATIVE_MENU)
			.build(new ItemWrench("wrench", MOD_ID+":item/wrench", startingItemId++)));
		//META WRENCH
		META_WRENCH = finish(new ItemBuilder(MOD_ID)
			.setKey("item.meta_wrench")
			.setStackSize(1)
			.setMaxDamage(250)
			.build(new ItemMetaWrench("meta_wrench", MOD_ID+":item/meta_wrench", startingItemId++)));
		//SEASHELL
		SEASHELL = finish(new ItemBuilder(MOD_ID)
			.setKey("item.seashell")
			.build(new Item("seashell", MOD_ID+":item/seashell", startingItemId++)));
		//STEEL FISHING ROD
		TOOL_STEEL_FISHINGROD = finish(new ItemBuilder(MOD_ID)
			.setKey("item.tool.steel.fishingrod")
			.setStackSize(1)
			.build(new ItemFishingRod("tool_steel_fishingrod", MOD_ID+":item/tool_steel_fishingrod", startingItemId++)));
		//PEDESTAL
		PEDESTAL = finish(new ItemBuilder(MOD_ID)
			.setKey("item.pedestal")
			.build(new ItemPlaceable("pedestal", MOD_ID+":item/pedestal", startingItemId++, ModBlocks.PEDESTAL)));
		//AMMO ARROWTORCH
		AMMO_ARROW_TORCH = finish(new ItemBuilder(MOD_ID)
			.setKey("item.ammo.arrow.torch")
			.build(new ItemArrowTorch("arrow.torch", MOD_ID + ":item/ammo_arrow_torch", startingItemId++) ));
		SACK = finish(new ItemBuilder(MOD_ID)
			.setKey("item.sack")
			.build(new ItemSack("sack", MOD_ID+":item/sack", startingItemId++)));
	}
}
