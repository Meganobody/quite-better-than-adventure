package quitebetter.core.crafting;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryDyeing;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryUndyeing;
import net.minecraft.core.item.Item;
import quitebetter.core.block.ModBlocks;
import quitebetter.core.item.ModItems;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.util.ArrayList;
import java.util.List;

import static quitebetter.core.ModCore.MOD_ID;

public class ModRecipes {
	public static  final RecipeGroup<RecipeEntryCrafting<?, ?>> GROUP_WORKBENCH = ((RecipeGroup<RecipeEntryCrafting<?, ?>>) RecipeBuilder.getRecipeGroup(MOD_ID, "workbench", new RecipeSymbol(Blocks.WORKBENCH.getDefaultStack())));

	public static void SetupNamespace() {
		RecipeBuilder.initNameSpace(MOD_ID);
	}

	public static void SetupCrafts() {
		//////GROUPS
		List<ItemStack> list;
		//HAZARDS
		list = new ArrayList();
		list.add(ModBlocks.HAZARD.getDefaultStack());
		for (DyeColor color : DyeColor.values()) {
			list.add(new ItemStack(ModBlocks.HAZARD_PAINTED, 1, color.blockMeta));
		}
		Registries.ITEM_GROUPS.register(MOD_ID + ":hazards", list);
		//CRATES
		list = new ArrayList();
		list.add(ModBlocks.CRATE.getDefaultStack());
		for (DyeColor color : DyeColor.values()) {
			list.add(new ItemStack(ModBlocks.CRATE_PAINTED, 1, color.blockMeta));
		}
		Registries.ITEM_GROUPS.register(MOD_ID + ":crates", list);
		//CORRUGATED IRON BLOCKS
		list = new ArrayList();
		list.add(ModBlocks.BLOCK_IRON_CORRUGATED.getDefaultStack());
		for (DyeColor color : DyeColor.values()) {
			list.add(new ItemStack(ModBlocks.BLOCK_IRON_CORRUGATED_PAINTED, 1, color.blockMeta << 4));
		}
		Registries.ITEM_GROUPS.register(MOD_ID + ":corrugated_irons", list);
		//////WORKBENCH
		//HOOK
		RecipeBuilder.Shaped(MOD_ID,
				" ##", " @#", "$  ")
			.addInput('#', Items.INGOT_IRON)
			.addInput('@', Items.STICK)
			.addInput('$', Items.ROPE)
			.create("hook_craft", new ItemStack(ModItems.CLIMBING_HOOK));
		//STEEL FISHING ROD
		if (!FabricLoaderImpl.INSTANCE.isModLoaded("stardew")) {
			RecipeBuilder.Shaped(MOD_ID,
					"  #", " #@", "# @")
				.addInput('#', Items.INGOT_STEEL)
				.addInput('@', Items.ROPE)
				.create("steel_fishing_rod_craft", new ItemStack(ModItems.TOOL_STEEL_FISHINGROD));
		}
		//ARROW TORCH
		RecipeBuilder.Shaped(MOD_ID,
				"@", "#", "$")
			.addInput('@', Items.NETHERCOAL)
			.addInput('#', Items.STICK)
			.addInput('$', Items.FEATHER_CHICKEN)
			.create("torch_arrow_craft", new ItemStack(ModItems.AMMO_ARROW_TORCH,4));
		RecipeBuilder.Shaped(MOD_ID,
				" @ ", "@#@", " @ ")
			.addInput('@', Items.AMMO_ARROW)
			.addInput('#', Items.NETHERCOAL)
			.create("torch_arrow_convert_craft", new ItemStack(ModItems.AMMO_ARROW_TORCH,4));
		//SACK
		RecipeBuilder.Shaped(MOD_ID,
				" @ ", "#$#", "###")
			.addInput('@', Items.ROPE)
			.addInput('#', Items.LEATHER)
			.addInput('$', Items.INGOT_GOLD)
			.create("sack_craft", new ItemStack(ModItems.SACK,1));
		//PEDESTAL
		RecipeBuilder.Shaped(MOD_ID,
				"###", "#@#")
			.addInput('#', Items.CLOTH)
			.addInput('@', "minecraft:planks")
			.create("sack_craft", new ItemStack(ModItems.PEDESTAL,2));
		//BONE BLOCK
		RecipeBuilder.Shaped(MOD_ID,
				"###", "###", "###")
			.addInput('#', Items.BONE)
			.create("boneblock_craft", new ItemStack(ModBlocks.BLOCK_BONE,1));
		//POLISHED OBSIDIAN
		RecipeBuilder.Shaped(MOD_ID,
				"#", "#")
			.addInput('#', Blocks.OBSIDIAN)
			.create("polished_obsidian_craft", new ItemStack(ModBlocks.OBSIDIAN_POLISHED,2));
		//SEASHELL TILES
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##")
			.addInput('#', ModItems.SEASHELL)
			.create("seashell_tiles_craft", new ItemStack(ModBlocks.TILE_SEASHELL,4));
		//SEASHELL TILES STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.TILE_SEASHELL)
			.create("seashell_tiles_stairs_craft", new ItemStack(ModBlocks.STAIRS_TILE_SEASHELL,4));
		//SEASHELL TILES SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###")
			.addInput('#', ModBlocks.TILE_SEASHELL)
			.create("seashell_tiles_slab_craft", new ItemStack(ModBlocks.SLAB_TILE_SEASHELL,6));
		//HAZARD BLOCKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##")
			.addInput('#', Blocks.WOOL)
			.create("hazard_block_craft", new ItemStack(ModBlocks.HAZARD,8));
		GROUP_WORKBENCH.register("hazard_dyeing", new RecipeEntryDyeing(new RecipeSymbol(MOD_ID+":hazards") , new ItemStack(ModBlocks.HAZARD_PAINTED), false, false));
		GROUP_WORKBENCH.register("hazard_undyeing", new RecipeEntryUndyeing(new RecipeSymbol(MOD_ID+":hazards") , new ItemStack(ModBlocks.HAZARD)));
		//FAN
		RecipeBuilder.Shaped(MOD_ID,
				" # ", "@$@", "@%@")
			.addInput('#', Items.INGOT_IRON)
			.addInput('@', "minecraft:planks")
			.addInput('$', Items.ROPE)
			.addInput('%', Items.DUST_REDSTONE)
			.create("fan_craft", new ItemStack(ModBlocks.FAN,1));
		//OLIVINE BRICKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##")
			.addInput('#', Items.OLIVINE)
			.create("olivine_bricks_craft", new ItemStack(ModBlocks.BRICK_OLIVINE,4));
		//OLIVINE BRICKS STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.BRICK_OLIVINE)
			.create("olivine_bricks_stairs_craft", new ItemStack(ModBlocks.STAIRS_BRICK_OLIVINE,4));
		//OLIVINE BRICKS SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###")
			.addInput('#', ModBlocks.BRICK_OLIVINE)
			.create("olivine_bricks_slab_craft", new ItemStack(ModBlocks.SLAB_BRICK_OLIVINE,6));
		//STEEL SUPPORTS
		RecipeBuilder.Shaped(MOD_ID,
				"# #", "###", "# #")
			.addInput('#', Items.INGOT_STEEL_CRUDE)
			.create("steel_supports_craft", new ItemStack(ModBlocks.SUPPORT_STEEL,6));
		//IRON SUPPORTS
		RecipeBuilder.Shaped(MOD_ID,
				"# #", "###", "# #")
			.addInput('#', Items.INGOT_IRON)
			.create("iron_supports_craft", new ItemStack(ModBlocks.SUPPORT_IRON,6));
		//WOOD SUPPORTS
		RecipeBuilder.Shaped(MOD_ID,
				"# #", "#@#", "# #")
			.addInput('#', "minecraft:logs")
			.addInput('@', "minecraft:planks")
			.create("wood_supports_craft", new ItemStack(ModBlocks.SUPPORT_WOOD,12));
		//SNOW BRICKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##")
			.addInput('#', Blocks.BLOCK_SNOW)
			.create("snow_bricks_craft", new ItemStack(ModBlocks.BRICK_SNOW,4));
		//SNOW BRICKS STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.BRICK_SNOW)
			.create("snow_bricks_stairs_craft", new ItemStack(ModBlocks.STAIRS_BRICK_SNOW,4));
		//SNOW BRICKS SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###")
			.addInput('#', ModBlocks.BRICK_SNOW)
			.create("snow_bricks_slab_craft", new ItemStack(ModBlocks.SLAB_BRICK_SNOW,6));
		//CRATE
		RecipeBuilder.Shaped(MOD_ID,
				"#@#", "@ @", "#@#")
			.addInput('#', "minecraft:logs")
			.addInput('@', "minecraft:planks")
			.create("crate_craft", new ItemStack(ModBlocks.CRATE,4));
		GROUP_WORKBENCH.register("crate_dyeing", new RecipeEntryDyeing(new RecipeSymbol(MOD_ID+":crates") , new ItemStack(ModBlocks.CRATE_PAINTED), false, false));
		GROUP_WORKBENCH.register("crate_undyeing", new RecipeEntryUndyeing(new RecipeSymbol(MOD_ID+":crates") , new ItemStack(ModBlocks.CRATE)));
		//OBSIDIAN BRICKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##")
			.addInput('#', Blocks.OBSIDIAN)
			.create("obsidian_bricks_craft", new ItemStack(ModBlocks.BRICK_OBSIDIAN,4));
		//OBSIDIAN BRICKS STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.BRICK_OBSIDIAN)
			.create("obsidian_bricks_stairs_craft", new ItemStack(ModBlocks.STAIRS_BRICK_OBSIDIAN,4));
		//OBSIDIAN BRICKS SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###")
			.addInput('#', ModBlocks.BRICK_OBSIDIAN)
			.create("obsidian_bricks_slab_craft", new ItemStack(ModBlocks.SLAB_BRICK_OBSIDIAN,6));
		//LANTERN COAL
		RecipeBuilder.Shaped(MOD_ID,
						"@",
						"#")
			.addInput('#', Items.INGOT_IRON)
			.addInput('@', Blocks.TORCH_COAL)
			.create("lantern_coal_craft", new ItemStack(ModBlocks.LANTERN_COAL,1));
		//LANTERN EMPTY
		RecipeBuilder.Shaped(MOD_ID,
				"#",
				"#")
			.addInput('#', Items.INGOT_IRON)
			.create("lantern_empty_craft", new ItemStack(ModBlocks.LANTERN_EMPTY,2));
		//LANTERN REDSTONE
		RecipeBuilder.Shaped(MOD_ID,
				"@",
				"#")
			.addInput('#', Items.INGOT_IRON)
			.addInput('@', Blocks.TORCH_REDSTONE_ACTIVE)
			.create("lantern_redstone_craft", new ItemStack(ModBlocks.LANTERN_REDSTONE,1));
		//LANTERN SWITCH
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(ModBlocks.LANTERN_REDSTONE)
			.addInput(Items.TOOL_CLOCK)
			.create("lantern_switch_craft", new ItemStack(ModBlocks.LANTERN_LATCH,1));
		//LANTERN MUSHROOM
		RecipeBuilder.Shaped(MOD_ID,
				"@",
				"#")
			.addInput('#', Items.INGOT_IRON)
			.addInput('@', ModBlocks.MUSHROOM_GLOWING)
			.create("lantern_glowing_craft", new ItemStack(ModBlocks.LANTERN_MUSHROOM,1));
		//HOOK
		RecipeBuilder.Shaped(MOD_ID,
				"CLC", "OGO", "ORO")
			.addInput('G', Items.INGOT_GOLD)
			.addInput('C', Items.CLOTH)
			.addInput('R', Items.DUST_REDSTONE)
			.addInput('O', Blocks.OBSIDIAN)
			.addInput('L', "minecraft:logs")
			.create("hook_craft", new ItemStack(ModBlocks.PISTON_BASE_FINE));
		//WRENCH
		RecipeBuilder.Shaped(MOD_ID,
				"# #",
				" # ",
				" # ")
			.addInput('#', Items.INGOT_GOLD)
			.create("wrench_craft", new ItemStack(ModItems.WRENCH,1));
		//GLOWING MUSHROOM USE
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(ModBlocks.MUSHROOM_GLOWING)
			.create("mushroom_glowing_dye_craft", new ItemStack(Items.DYE,1,10));
		RecipeBuilder.Furnace(MOD_ID)
			.setInput(ModBlocks.MUSHROOM_GLOWING)
			.create("mushroom_glowing_furnace_dye_craft", new ItemStack(Items.DYE,1,2));
		//SEASHELL USE
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(ModItems.SEASHELL)
			.create("seashell_dye_craft", new ItemStack(Items.DYE,2,15));
		RecipeBuilder.Furnace(MOD_ID)
			.setInput(ModItems.SEASHELL)
			.create("seashell_furnace_dye_craft", new ItemStack(Items.DYE,1,13));
		//MUD BRICKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##")
			.addInput('#', Blocks.MUD)
			.create("mud_bricks_craft", new ItemStack(ModBlocks.BRICK_MUD,4));
		//MUD BRICKS STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.BRICK_MUD)
			.create("mud_bricks_stairs_craft", new ItemStack(ModBlocks.STAIRS_BRICK_MUD,4));
		//MUD BRICKS SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###")
			.addInput('#', ModBlocks.BRICK_MUD)
			.create("mud_bricks_slab_craft", new ItemStack(ModBlocks.SLAB_BRICK_MUD,6));
		//BAKED MUD BRICKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##")
			.addInput('#', Blocks.MUD_BAKED)
			.create("mud_baked_bricks_craft", new ItemStack(ModBlocks.BRICK_MUD_BAKED,4));
		//BAKED MUD BRICKS STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.BRICK_MUD_BAKED)
			.create("mud_baked_bricks_stairs_craft", new ItemStack(ModBlocks.STAIRS_BRICK_MUD_BAKED,4));
		//BAKED MUD BRICKS SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###")
			.addInput('#', ModBlocks.BRICK_MUD_BAKED)
			.create("mud_baked_bricks_slab_craft", new ItemStack(ModBlocks.SLAB_BRICK_MUD_BAKED,6));
		//CORRUGATED IRON BLOCKS
		RecipeBuilder.Shaped(MOD_ID,
				"###", "###")
			.addInput('#', Items.INGOT_IRON)
			.create("corrugated_iron_block_craft", new ItemStack(ModBlocks.BLOCK_IRON_CORRUGATED,8));
		GROUP_WORKBENCH.register("corrugated_iron_block_dyeing", new RecipeEntryDyeing(new RecipeSymbol(MOD_ID+":corrugated_irons") , new ItemStack(ModBlocks.BLOCK_IRON_CORRUGATED_PAINTED), true, false));
		GROUP_WORKBENCH.register("corrugated_iron_block_undyeing", new RecipeEntryUndyeing(new RecipeSymbol(MOD_ID+":corrugated_irons") , new ItemStack(ModBlocks.BLOCK_IRON_CORRUGATED)));
	}

	private static WeightedRandomLootObject BagEntry(Item item, int yieldMin, int yieldMax) {
		return new WeightedRandomLootObject(new ItemStack(item), yieldMin, yieldMax);
	}

	public static void SetupCorrections() {
		//TROMMEL SAND > SEASHELL
		RecipeBuilder.ModifyTrommel("minecraft","sand").addEntry(BagEntry(ModItems.SEASHELL, 1, 3), 40);
	}
}
