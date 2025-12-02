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
	public static Block<?> SUPPORT_WOOD;
	public static Block<?> MUSHROOM_GLOWING;
	public static Block<?> PEDESTAL;
	public static Block<?> BRICK_SNOW;
	public static Block<BlockLogicStairs> STAIRS_BRICK_SNOW;
	public static Block<BlockLogicSlab> SLAB_BRICK_SNOW;
	public static Block<?> ARROW_TORCH;
	public static Block<?> CRATE;
	public static Block<?> CRATE_PAINTED;
	public static Block<?> GEODE;

	public static Block<?> BRICK_OBSIDIAN;
	public static Block<BlockLogicStairs> STAIRS_BRICK_OBSIDIAN;
	public static Block<BlockLogicSlab> SLAB_BRICK_OBSIDIAN;

	public static Block<?> LANTERN_COAL;
	public static Block<?> LANTERN_EMPTY;
	public static Block<?> LANTERN_REDSTONE;
	public static Block<?> LANTERN_REDSTONE_IDLE;
	public static Block<?> LANTERN_LATCH;
	public static Block<?> LANTERN_LATCH_IDLE;
	public static Block<?> LANTERN_LATCH_INPUT;
	public static Block<?> LANTERN_LATCH_IDLE_INPUT;
	public static Block<?> LANTERN_PULSE;
	public static Block<?> LANTERN_PULSE_IDLE;
	public static Block<?> LANTERN_PULSE_INPUT;
	public static Block<?> LANTERN_PULSE_IDLE_INPUT;
	public static Block<?> LANTERN_CLOCK;
	public static Block<?> LANTERN_CLOCK_IDLE;
	public static Block<?> LANTERN_CLOCK_INPUT;
	public static Block<?> LANTERN_CLOCK_IDLE_INPUT;
	public static Block<?> LANTERN_MUSHROOM;

	public static Block<?> PIPE;

	public static Block<?> OVERLAY_SEASHELL;

	public static Block<?> BRICK_MUD;
	public static Block<BlockLogicStairs> STAIRS_BRICK_MUD;
	public static Block<BlockLogicSlab> SLAB_BRICK_MUD;

	public static Block<?> BRICK_MUD_BAKED;
	public static Block<BlockLogicStairs> STAIRS_BRICK_MUD_BAKED;
	public static Block<BlockLogicSlab> SLAB_BRICK_MUD_BAKED;

	public static Block<?> BRICK_CLAY_MOSSY;

	public static Block<?> BLOCK_IRON_CORRUGATED;
	public static Block<?> BLOCK_IRON_CORRUGATED_PAINTED;

	public static int startingBlockId = ModConfig.startingBlockId;

	public static Integer id(String name) {
		try {
			return ModConfig.cfg.getInt(ModConfig.BlockIDs+"."+name);
		}catch (NullPointerException e) {
			return startingBlockId++;
		}
	}

	public static void Setup() {
		PIPE = new BlockBuilder(MOD_ID)
			.setHardness(1F).setResistance(6f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.MINEABLE_BY_PICKAXE)
			.build("pipe", id("PIPE"), b -> new BlockLogicPipe(b) {});
		//LANTERN
		LANTERN_COAL = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS)
			.build("lantern_coal", id("LANTERN_COAL"), b -> new BlockLogicLanternCoal(b) {});
		LANTERN_EMPTY = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS)
			.build("lantern_empty", id("LANTERN_EMPTY"), b -> new BlockLogicLanternEmpty(b) {});
		LANTERN_REDSTONE = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS)
			.build("lantern_redstone", id("LANTERN_REDSTONE"), b -> new BlockLogicLanternRedstone(b, true) {});
		LANTERN_REDSTONE_IDLE = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_redstone_idle", id("LANTERN_REDSTONE_IDLE"), b -> new BlockLogicLanternRedstone(b, false) {});
		LANTERN_LATCH = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS)
			.build("lantern_latch", id("LANTERN_LATCH"), b -> new BlockLogicLanternLatch(b, true, false) {});
		LANTERN_LATCH_IDLE = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
				.build("lantern_latch_idle", id("LANTERN_LATCH_IDLE"), b -> new BlockLogicLanternLatch(b, false, false) {});
		LANTERN_LATCH_INPUT = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_latch_input", id("LANTERN_LATCH_INPUT"), b -> new BlockLogicLanternLatch(b, true, true) {});
		LANTERN_LATCH_IDLE_INPUT = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_latch_idle_input", id("LANTERN_LATCH_IDLE_INPUT"), b -> new BlockLogicLanternLatch(b, false, true) {});
		LANTERN_PULSE = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_pulse", id("LANTERN_PULSE"), b -> new BlockLogicLanternPulse(b, true, false) {});
		LANTERN_PULSE_IDLE = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS)
			.build("lantern_pulse_idle", id("LANTERN_PULSE_IDLE"), b -> new BlockLogicLanternPulse(b, false, false) {});
		LANTERN_PULSE_INPUT = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_pulse_input", id("LANTERN_PULSE_INPUT"), b -> new BlockLogicLanternPulse(b, true, true) {});
		LANTERN_PULSE_IDLE_INPUT = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_pulse_idle_input", id("LANTERN_PULSE_IDLE_INPUT"), b -> new BlockLogicLanternPulse(b, false, true) {});
		LANTERN_CLOCK = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
				.build("lantern_clock", id("LANTERN_CLOCK"), b -> new BlockLogicLanternClock(b, true, false) {});
		LANTERN_CLOCK_IDLE = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS)
			.build("lantern_clock_idle", id("LANTERN_CLOCK_IDLE"), b -> new BlockLogicLanternClock(b, false, false) {});
		LANTERN_CLOCK_INPUT = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_clock_input", id("LANTERN_CLOCK_INPUT"), b -> new BlockLogicLanternClock(b, true, true) {});
		LANTERN_CLOCK_IDLE_INPUT = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF, BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("lantern_clock_idle_input", id("LANTERN_CLOCK_IDLE_INPUT"), b -> new BlockLogicLanternClock(b, false, true) {});
		LANTERN_MUSHROOM = new BlockBuilder(MOD_ID)
			.setHardness(0.1F).setResistance(3.5f)
			.setTicking(true)
			.setTags(BlockTags.CAN_HANG_OFF,BlockTags.BROKEN_BY_FLUIDS)
			.build("lantern_mushroom", id("LANTERN_MUSHROOM"), b -> new BlockLogicLantern(b) {})
			.withLightEmission(15);

		//BONEBLOCK
		BLOCK_BONE = new BlockBuilder(MOD_ID)
			.setHardness(0.2F).setResistance(6f)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setBlockSound(BlockSounds.METAL)
			.build("block_bone", id("BLOCK_BONE"), b -> new BlockLogicBoneBlock(b, Material.marble) {});
		//OBSIDIAN POLISHED
		OBSIDIAN_POLISHED = new BlockBuilder(MOD_ID)
			.setHardness(10.0F).setResistance(1200.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.PISTON_CRUSHING)
			.build("obsidian_polished", id("OBSIDIAN_POLISHED"), b -> new BlockLogic(b, Material.stone) {})
			.withOverrideColor(MaterialColor.paintedBlack);
		//SEASHELL TILES
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
		SUPPORT_WOOD = new BlockBuilder(MOD_ID)
			.setHardness(0.3F)
			.setTags(BlockTags.MINEABLE_BY_AXE)
			.setBlockSound(BlockSounds.WOOD)
			.build("support_wood", id("SUPPORT_WOOD"), b -> new BlockLogicPillar(b, Material.wood) {});
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
		//BRICK OBSIDIAN
		BRICK_OBSIDIAN = new BlockBuilder(MOD_ID)
			.setHardness(10F).setResistance(2000.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.PISTON_CRUSHING)
			.setBlockSound(BlockSounds.STONE)
			.build("brick_obsidian", id("BRICK_OBSIDIAN"), b -> new BlockLogic(b, Material.stone) {})
			.withOverrideColor(MaterialColor.paintedBlack);
		STAIRS_BRICK_OBSIDIAN = new BlockBuilder(MOD_ID)
			.setHardness(10F).setResistance(2000.0F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.PISTON_CRUSHING)
			.build("stairs_brick_obsidian", id("STAIRS_BRICK_OBSIDIAN"), b -> new BlockLogicStairs(b, BRICK_OBSIDIAN) {});
		SLAB_BRICK_OBSIDIAN = new BlockBuilder(MOD_ID)
			.setHardness(10F).setResistance(2000.0F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE,BlockTags.PISTON_CRUSHING)
			.build("slab_brick_obsidian", id("SLAB_BRICK_OBSIDIAN"), b -> new BlockLogicSlab(b, BRICK_OBSIDIAN) {});

		//OVERLAY SEASHELL
		OVERLAY_SEASHELL = new BlockBuilder(MOD_ID)
			.setTags(BlockTags.BROKEN_BY_FLUIDS,BlockTags.NOT_IN_CREATIVE_MENU)
			.build("overlay_seashell", id("OVERLAY_SEASHELL"), b -> new BlockLogicOverlaySeashells(b, Material.decoration) {});
		//BRICKS MUD
		BRICK_MUD = new BlockBuilder(MOD_ID)
			.setHardness(0.6F)
			.setBlockSound(BlockSounds.GRAVEL)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.GROWS_FLOWERS, BlockTags.GROWS_CACTI, BlockTags.GROWS_TREES, BlockTags.GROWS_SUGAR_CANE)
			.build("brick_mud", id("BRICK_MUD"), b -> new BlockLogicMudBlock(b, Material.dirt, false, BRICK_MUD_BAKED) {})
			.withOverrideColor(MaterialColor.dirt);
		STAIRS_BRICK_MUD = new BlockBuilder(MOD_ID)
			.setHardness(0.6F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.GRAVEL)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL)
			.build("stairs_brick_mud", id("STAIRS_BRICK_MUD"), b -> new BlockLogicMudStairs(b, BRICK_MUD, false, STAIRS_BRICK_MUD_BAKED) {});
		SLAB_BRICK_MUD = new BlockBuilder(MOD_ID)
			.setHardness(0.6F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.GRAVEL)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL)
			.build("slab_brick_mud", id("SLAB_BRICK_MUD"), b -> new BlockLogicMudSlab(b, BRICK_MUD, false, SLAB_BRICK_MUD_BAKED) {});
		//BRICKS MUD BACKED
		BRICK_MUD_BAKED = new BlockBuilder(MOD_ID)
			.setHardness(1.5F)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.GROWS_FLOWERS, BlockTags.GROWS_CACTI, BlockTags.GROWS_TREES, BlockTags.GROWS_SUGAR_CANE)
			.build("brick_mud_baked", id("BRICK_MUD_BAKED"), b -> new BlockLogicMudBlock(b, Material.steel, true, BRICK_MUD) {})
			.withOverrideColor(MaterialColor.dirt);
		STAIRS_BRICK_MUD_BAKED = new BlockBuilder(MOD_ID)
			.setHardness(1.5F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("stairs_brick_mud_baked", id("STAIRS_BRICK_MUD_BAKED"), b -> new BlockLogicMudStairs(b, BRICK_MUD_BAKED, true, STAIRS_BRICK_MUD) {});
		SLAB_BRICK_MUD_BAKED = new BlockBuilder(MOD_ID)
			.setHardness(1.5F)
			.setUseInternalLight()
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("slab_brick_mud_baked", id("SLAB_BRICK_MUD_BAKED"), b -> new BlockLogicMudSlab(b, BRICK_MUD_BAKED, true, SLAB_BRICK_MUD) {});
		BRICK_CLAY_MOSSY = new BlockBuilder(MOD_ID)
			.setHardness(1F)
			.setResistance(10.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("brick_clay_mossy", id("BRICK_CLAY_MOSSY"), b -> new BlockLogic(b, Material.stone) {});
		//CORRUGATED IRON BLOCK
		BLOCK_IRON_CORRUGATED = new BlockBuilder(MOD_ID)
			.setHardness(2F).setResistance(25.0F)
			.setTags(BlockTags.CHAINLINK_FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE)
			.build("block_iron_corrugated", id("BLOCK_IRON_CORRUGATED"), b -> new BlockLogicCorrugatedIron(b, Material.metal) {})
			.setBlockItem(b -> new ItemBlock<>(BLOCK_IRON_CORRUGATED));
		BLOCK_IRON_CORRUGATED_PAINTED = new BlockBuilder(MOD_ID)
			.setHardness(2F).setResistance(25.0F)
			.setTags(BlockTags.CHAINLINK_FENCES_CONNECT, BlockTags.MINEABLE_BY_PICKAXE, BlockTags.NOT_IN_CREATIVE_MENU)
			.setBlockItem(b -> new ItemBlockPainted<>(b, true))
			.build("block_iron_corrugated_painted", id("BLOCK_IRON_CORRUGATED_PAINTED"), b -> new BlockLogicCorrugatedIronPainted(b, Material.metal) {});
	}
}
