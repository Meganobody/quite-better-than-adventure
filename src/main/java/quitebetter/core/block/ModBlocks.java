package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.MaterialColor;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.item.block.ItemBlockPainted;
import net.minecraft.core.sound.BlockSounds;
import quitebetter.core.ModConfig;
import turniplabs.halplibe.helper.BlockBuilder;

import static quitebetter.core.ModCore.LOGGER;
import static quitebetter.core.ModCore.MOD_ID;

public class ModBlocks {
	public static Block<?> HAZARD;
	public static Block<?> BLOCK_BONE;
	public static Block<?> OBSIDIAN_POLISHED;
	public static Block<?> HAZARD_PAINTED;
	public static Block<?> TILE_SEASHELL;
	public static Block<BlockLogicStairs> STAIRS_TILE_SEASHELL;
	public static Block<BlockLogicSlab> SLAB_TILE_SEASHELL;
	public static Block<?> FAN;
	public static Block<?> ACTIVE_FAN;
	public static Block<?> IN_FAN;
	public static Block<?> ACTIVE_IN_FAN;
	public static Block<?> BRICK_OLIVINE;
	public static Block<BlockLogicStairs> STAIRS_BRICK_OLIVINE;
	public static Block<BlockLogicSlab> SLAB_BRICK_OLIVINE;
	public static Block<?> SUPPORT_STEEL;
	public static Block<?> SUPPORT_IRON;
	public static Block<?> MUSHROOM_GLOWING;
	public static Block<?> PEDESTAL;
	public static Block<?> BRICK_SNOW;
	public static Block<BlockLogicStairs> STAIRS_BRICK_SNOW;
	public static Block<BlockLogicSlab> SLAB_BRICK_SNOW;
	public static Block<?> ARROW_TORCH;
	public static Block<?> CRATE;
	public static Block<?> CRATE_PAINTED;
	public static Block<?> GEODE;

	public static int startingBlockId = ModConfig.startingBlockId;

	private static int id(String name) {
		try {
			return ModConfig.cfg.getInt(ModConfig.BlockIDs+"."+name);
		}catch (NullPointerException e) {
			ModConfig.properties.addEntry(ModConfig.BlockIDs+"."+name, startingBlockId);
			return startingBlockId++;
		}
	}

	public static void Setup() {
		//BONEBLOCK
		BLOCK_BONE = new BlockBuilder(MOD_ID)
			.setHardness(1.0F).setResistance(1.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("block_bone", id("BLOCK_BONE"), b -> new BlockLogic(b, Material.marble) {});
		//OBSIDIAN POLISHED
		OBSIDIAN_POLISHED = new BlockBuilder(MOD_ID)
			.setHardness(20.0F).setResistance(2000.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("obsidian_polished", id("OBSIDIAN_POLISHED"), b -> new BlockLogic(b, Material.stone) {})
			.withOverrideColor(MaterialColor.paintedBlack);
		//SEASHELLTILES
		TILE_SEASHELL = new BlockBuilder(MOD_ID)
			.setHardness(1.5F).setResistance(2.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("tile_seashell", id("TILE_SEASHELL"), b -> new BlockLogic(b, Material.slate) {});
		STAIRS_TILE_SEASHELL = new BlockBuilder(MOD_ID)
			.setHardness(1.5F).setResistance(2.0F)
			.setUseInternalLight()
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("stairs_tile_seashell", id("STAIRS_TILE_SEASHELL"), b -> new BlockLogicStairs(b, TILE_SEASHELL) {});
		SLAB_TILE_SEASHELL = new BlockBuilder(MOD_ID)
			.setHardness(1.5F).setResistance(2.0F)
			.setUseInternalLight()
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("slab_tile_seashell", id("SLAB_TILE_SEASHELL"), b -> new BlockLogicSlab(b, TILE_SEASHELL) {});
		//HAZARD
		HAZARD = new BlockBuilder(MOD_ID)
			.setHardness(1.5F).setResistance(25.0F)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE)
			.build("hazard", id("HAZARD"), b -> new BlockLogicHazard(b, Material.stone) {})
			.setBlockItem(b -> new ItemBlock(HAZARD));
		HAZARD_PAINTED = new BlockBuilder(MOD_ID)
			.setHardness(1.5F).setResistance(25.0F)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE, BlockTags.NOT_IN_CREATIVE_MENU)
			.setBlockItem(b -> new ItemBlockPainted(b, false))
			.build("hazard_painted", id("HAZARD_PAINTED"), b -> new BlockLogicHazardPainted(b, Material.stone) {});
		//FAN
		FAN = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(25.0F)
			.setTicking(true)
			.setBlockSound(BlockSounds.WOOD)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE, BlockTags.MINEABLE_BY_AXE)
			.build("fan", id("FAN"), b -> new BlockLogicFan(b, Material.wood, false, false) {});
		ACTIVE_FAN = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(25.0F)
			.setTicking(true)
			.setBlockSound(BlockSounds.WOOD)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE, BlockTags.MINEABLE_BY_AXE, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("active_fan", id("ACTIVE_FAN"), b -> new BlockLogicFan(b, Material.wood, true, false) {});
		IN_FAN = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(25.0F)
			.setTicking(true)
			.setBlockSound(BlockSounds.WOOD)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE, BlockTags.MINEABLE_BY_AXE, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("in_fan", id("IN_FAN"), b -> new BlockLogicFan(b, Material.wood, false, true) {});
		ACTIVE_IN_FAN = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(25.0F)
			.setTicking(true)
			.setBlockSound(BlockSounds.WOOD)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE, BlockTags.MINEABLE_BY_AXE, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("active_in_fan", id("ACTIVE_IN_FAN"), b -> new BlockLogicFan(b, Material.wood, true, true) {});
		//OLIVINE BRICKS
		BRICK_OLIVINE = new BlockBuilder(MOD_ID)
			.setHardness(3F).setResistance(10.0F)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("brick_olivine", id("BRICK_OLIVINE"), b -> new BlockLogic(b, Material.stone) {});
		STAIRS_BRICK_OLIVINE = new BlockBuilder(MOD_ID)
			.setHardness(3F).setResistance(10.0F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("stairs_brick_olivine", id("STAIRS_BRICK_OLIVINE"), b -> new BlockLogicStairs(b, BRICK_OLIVINE) {});
		SLAB_BRICK_OLIVINE = new BlockBuilder(MOD_ID)
			.setHardness(3F).setResistance(10.0F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("slab_brick_olivine", id("SLAB_BRICK_OLIVINE"), b -> new BlockLogicSlab(b, BRICK_OLIVINE) {});
		//SUPPORTS
		SUPPORT_STEEL = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(1.0F)
			.setBlockSound(BlockSounds.METAL)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("support_steel", id("SUPPORT_STEEL"), b -> new BlockLogicPillar(b, Material.steel) {});
		SUPPORT_IRON = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(1.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setBlockSound(BlockSounds.METAL)
			.build("support_iron", id("SUPPORT_IRON"), b -> new BlockLogicPillar(b, Material.metal) {});
		//GLOWING MUSHROOM
		MUSHROOM_GLOWING = new BlockBuilder(MOD_ID)
			.setTags(BlockTags.PLANTABLE_IN_JAR)
			.setBlockSound(BlockSounds.GRASS)
			.build("mushroom_glowing", id("MUSHROOM_GLOWING"), b -> new BlockLogicGlowingMushroom(b) {}).withLightEmission(0.95F);
		//PEDESTAL
		PEDESTAL = new BlockBuilder(MOD_ID)
			.setTags(BlockTags.NOT_IN_CREATIVE_MENU)
			.build("pedestal", id("PEDESTAL"), b -> new BlockLogicPedestal(b, Material.cloth) {});
		//SNOW BRICKS
		BRICK_SNOW = new BlockBuilder(MOD_ID)
			.setHardness(0.3F)
			.setBlockSound(BlockSounds.CLOTH)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.MINEABLE_BY_SHOVEL)
			.build("brick_snow", id("BRICK_SNOW"), b -> new BlockLogic(b, Material.snow) {});
		STAIRS_BRICK_SNOW = new BlockBuilder(MOD_ID)
			.setHardness(0.3F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.CLOTH)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.MINEABLE_BY_SHOVEL)
			.build("stairs_brick_snow", id("STAIRS_BRICK_SNOW"), b -> new BlockLogicStairs(b, BRICK_SNOW) {});
		SLAB_BRICK_SNOW = new BlockBuilder(MOD_ID)
			.setHardness(0.3F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.CLOTH)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.MINEABLE_BY_SHOVEL)
			.build("slab_brick_snow", id("SLAB_BRICK_SNOW"), b -> new BlockLogicSlab(b, BRICK_SNOW) {});
		//ARROW TORCH BLOCK
		ARROW_TORCH = new BlockBuilder(MOD_ID)
			.setTags(BlockTags.NOT_IN_CREATIVE_MENU)
			.build("arrow_torch", id("ARROW_TORCH"), b -> new BlockLogicArrowTorch(b) {}).withLightEmission(1F);
		//CRATE
		CRATE = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(25.0F)
			.build("crate", id("CRATE"), b -> new BlockLogicCrate(b, Material.wood) {});
		CRATE_PAINTED = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(25.0F)
			.setBlockItem(b -> new ItemBlockPainted<>(b, false))
			.build("crate_painted", id("CRATE_PAINTED"), b -> new BlockLogicCratePainted(b, Material.wood) {});
		//GEODE
		GEODE = new BlockBuilder(MOD_ID)
			.setHardness(10F).setResistance(25.0F)
			.build("geode", id("GEODE"), b -> new BlockLogicGeode(b, Material.basalt) {});
	}
}
