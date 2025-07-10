package quitebetter.core.crafting;

import net.fabricmc.loader.impl.FabricLoaderImpl;
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

public class ModCrafts {
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
		//////WORKBENCH
		//HOOK
		RecipeBuilder.Shaped(MOD_ID,
				" ##", " @#", "$  ")
			.addInput('#', Items.INGOT_IRON)
			.addInput('@', Items.STICK)
			.addInput('$', Items.ROPE)
			.create("hook_craft", new ItemStack(ModItems.HOOK));
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
				"###", "#@#", "")
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
				"#", "#", "")
			.addInput('#', Blocks.OBSIDIAN)
			.create("polished_obsidian_craft", new ItemStack(ModBlocks.OBSIDIAN_POLISHED,2));
		//SEASHELL TILES
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##", "")
			.addInput('#', ModItems.SEASHELL)
			.create("seashell_tiles_craft", new ItemStack(ModBlocks.TILE_SEASHELL,4));
		//SEASHELL TILES STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.TILE_SEASHELL)
			.create("seashell_tiles_stairs_craft", new ItemStack(ModBlocks.STAIRS_TILE_SEASHELL,4));
		//SEASHELL TILES SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###", "", "")
			.addInput('#', ModBlocks.TILE_SEASHELL)
			.create("seashell_tiles_slab_craft", new ItemStack(ModBlocks.SLAB_TILE_SEASHELL,6));
		//HAZARD BLOCKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##", "")
			.addInput('#', Blocks.WOOL)
			.create("hazard_block_craft", new ItemStack(ModBlocks.HAZARD,8));
		for(DyeColor c : DyeColor.values()) {
			RecipeBuilder.Shaped(MOD_ID,
					"###", "#@#", "###")
				.addInput('#', MOD_ID+":hazards")
				.addInput('@', Items.DYE, c.itemMeta)
				.create("hazard_block_craft", new ItemStack(ModBlocks.HAZARD_PAINTED,8,c.blockMeta));
		}
		RecipeBuilder.Shaped(MOD_ID,
				"###", "#@#", "###")
			.addInput('#', MOD_ID+":hazards")
			.addInput('@', Blocks.SPONGE_WET)
			.create("hazard_block_clean_craft", new ItemStack(ModBlocks.HAZARD,8));
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
				"##", "##", "")
			.addInput('#', Items.OLIVINE)
			.create("olivine_bricks_craft", new ItemStack(ModBlocks.BRICK_OLIVINE,4));
		//OLIVINE BRICKS STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.BRICK_OLIVINE)
			.create("olivine_bricks_stairs_craft", new ItemStack(ModBlocks.STAIRS_BRICK_OLIVINE,4));
		//OLIVINE BRICKS SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###", "", "")
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
		//SNOW BRICKS
		RecipeBuilder.Shaped(MOD_ID,
				"##", "##", "")
			.addInput('#', Blocks.BLOCK_SNOW)
			.create("snow_bricks_craft", new ItemStack(ModBlocks.BRICK_SNOW,4));
		//SNOW BRICKS STAIRS
		RecipeBuilder.Shaped(MOD_ID,
				"#", "##", "###")
			.addInput('#', ModBlocks.BRICK_SNOW)
			.create("snow_bricks_stairs_craft", new ItemStack(ModBlocks.STAIRS_BRICK_SNOW,4));
		//SNOW BRICKS SLAB
		RecipeBuilder.Shaped(MOD_ID,
				"###", "", "")
			.addInput('#', ModBlocks.BRICK_SNOW)
			.create("snow_bricks_slab_craft", new ItemStack(ModBlocks.SLAB_BRICK_SNOW,6));
		//CRATE
		RecipeBuilder.Shaped(MOD_ID,
				"#@#", "@ @", "#@#")
			.addInput('#', "minecraft:logs")
			.addInput('@', "minecraft:planks")
			.create("crate_craft", new ItemStack(ModBlocks.CRATE,1));
		for(DyeColor c : DyeColor.values()) {
			RecipeBuilder.Shaped(MOD_ID,
					"###", "#@#", "###")
				.addInput('#', MOD_ID+":crates")
				.addInput('@', Items.DYE, c.itemMeta)
				.create("crate_painted_craft", new ItemStack(ModBlocks.CRATE_PAINTED,8,c.blockMeta));
		}
		RecipeBuilder.Shaped(MOD_ID,
				"###", "#@#", "###")
			.addInput('#', MOD_ID+":crates")
			.addInput('@', Blocks.SPONGE_WET)
			.create("crate_clean_craft", new ItemStack(ModBlocks.CRATE,8));
	}

	public static void SetupCorrections() {
		//TROMMEL SAND > SEASHELL
		RecipeCorrections.addToTrommelRecipe("minecraft:trommel/sand",ModItems.SEASHELL,1,5,45);
	}
}
