package quitebetter.client.blockmodel;

import quitebetter.core.block.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.*;
import net.minecraft.core.util.helper.Side;

import static quitebetter.core.ModCore.MOD_ID;

@Environment(EnvType.CLIENT)
public class BlockModels {
	private static String PathTo(String texture) {
		return MOD_ID+":block/"+texture;
	}
	private static String PathTo(String folder, String texture) {
		return MOD_ID+":block/"+folder+"/"+texture;
	}
	private static String PathTo(String namespace, String folder, String texture) {
		return namespace+":block/"+folder+"/"+texture;
	}

	public static void Setup(BlockModelDispatcher d) {
		//BONEBLOCK
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BLOCK_BONE).setAllTextures(0,PathTo("block_bone")));
		//OBSIDIAN POLISHED
		d.addDispatch(new BlockModelStandard<>(ModBlocks.OBSIDIAN_POLISHED).setAllTextures(0,PathTo("obsidian_polished")));
		//OLIVINEBRICKS
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_OLIVINE).setAllTextures(0,PathTo("brick_olivine")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_BRICK_OLIVINE).setAllTextures(0,PathTo("brick_olivine")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_BRICK_OLIVINE).setAllTextures(0,PathTo("brick_olivine")));
		//SEASHELLTILES
		d.addDispatch(new BlockModelStandard<>(ModBlocks.TILE_SEASHELL).setAllTextures(0,PathTo("tile_seashell")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_TILE_SEASHELL).setAllTextures(0,PathTo("tile_seashell")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_TILE_SEASHELL).setAllTextures(0,PathTo("tile_seashell")));
		//HAZARD BLOCK
		d.addDispatch(new BlockModelStandard<>(ModBlocks.HAZARD).setAllTextures(0,PathTo("hazard_block","default")));
		d.addDispatch(new BlockModelHazardPainted<>(ModBlocks.HAZARD_PAINTED));
		//FAN
		d.addDispatch(new BlockModelFan<>(ModBlocks.FAN)
							.setTex(0, PathTo("fan", "bottom"), Side.BOTTOM)
							.setTex(0, PathTo("fan", "top"), Side.TOP)
							.setTex(0, PathTo("fan", "side"), Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST));
		d.addDispatch(new BlockModelFan<>(ModBlocks.ACTIVE_FAN)
							.setTex(0, PathTo("fan", "bottom"), Side.BOTTOM)
							.setTex(0, PathTo("fan", "active_top"), Side.TOP)
							.setTex(0, PathTo("fan", "active_side"), Side.NORTH, Side.WEST)
							.setTex(0, PathTo("fan", "active_side_flipped"), Side.SOUTH, Side.EAST));
		d.addDispatch(new BlockModelFan<>(ModBlocks.IN_FAN)
							.setTex(0, PathTo("fan", "bottom"), Side.BOTTOM)
							.setTex(0, PathTo("fan", "top"), Side.TOP)
							.setTex(0, PathTo("fan", "side_suction"), Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST));
		d.addDispatch(new BlockModelFan<>(ModBlocks.ACTIVE_IN_FAN)
							.setTex(0, PathTo("fan", "bottom"), Side.BOTTOM)
							.setTex(0, PathTo("fan", "active_top"), Side.TOP)
							.setTex(0, PathTo("fan", "active_side_suction"), Side.NORTH, Side.WEST)
							.setTex(0, PathTo("fan", "active_side_flipped_suction"), Side.SOUTH, Side.EAST));
		//SUPPORTS
		d.addDispatch(new BlockModelSupports<>(ModBlocks.SUPPORT_STEEL).withTextures(PathTo("support","steel_top"),PathTo("support","steel")));
		d.addDispatch(new BlockModelSupports<>(ModBlocks.SUPPORT_IRON).withTextures(PathTo("support","iron_top"),PathTo("support","iron")));
		d.addDispatch(new BlockModelSupports<>(ModBlocks.SUPPORT_WOOD).withTextures(PathTo("support","wood_top"),PathTo("support","wood")));
		//GLOWING MUSHROOM
		d.addDispatch(new BlockModelCrossedSquares<>(ModBlocks.MUSHROOM_GLOWING).setAllTextures(0,PathTo("mushroom_glowing")));
		//PEDESTAL
		d.addDispatch(new BlockModelPedestal<>(ModBlocks.PEDESTAL));
		//SNOW BRICKS
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_SNOW).setAllTextures(0,PathTo("brick_snow")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_BRICK_SNOW).setAllTextures(0,PathTo("brick_snow")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_BRICK_SNOW).setAllTextures(0,PathTo("brick_snow")));
		//ARROW TORCH
		d.addDispatch(new BlockModelTorch<>(ModBlocks.ARROW_TORCH).setAllTextures(0,PathTo("arrow_torch")));
		//CRATE
		d.addDispatch(new BlockModelStandard<>(ModBlocks.CRATE).setAllTextures(0,PathTo("crate","default")));
		d.addDispatch(new BlockModelCratePainted<>(ModBlocks.CRATE_PAINTED));
		//GEODE
		d.addDispatch(new BlockModelStandard<>(ModBlocks.GEODE).setAllTextures(0,PathTo("geode")));
		//BRICK OBSIDIAN
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_OBSIDIAN).setAllTextures(0,PathTo("brick_obsidian")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_BRICK_OBSIDIAN).setAllTextures(0,PathTo("brick_obsidian")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_BRICK_OBSIDIAN).setAllTextures(0,PathTo("brick_obsidian")));
		//LANTERN
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_COAL).setAllTextures(0,PathTo("lantern","coal")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_EMPTY).setAllTextures(0,PathTo("lantern","empty")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_REDSTONE).setAllTextures(0,PathTo("lantern","redstone")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_REDSTONE_IDLE).setAllTextures(0,PathTo("lantern","redstone_idle")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_LATCH).setAllTextures(0,PathTo("lantern","steel")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_LATCH_IDLE).setAllTextures(0,PathTo("lantern","steel_idle")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_LATCH_INPUT).setAllTextures(0,PathTo("lantern","steel_input")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_LATCH_IDLE_INPUT).setAllTextures(0,PathTo("lantern","steel_idle_input")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_PULSE).setAllTextures(0,PathTo("lantern","gold")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_PULSE_IDLE).setAllTextures(0,PathTo("lantern","gold_idle")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_PULSE_INPUT).setAllTextures(0,PathTo("lantern","gold_input")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_PULSE_IDLE_INPUT).setAllTextures(0,PathTo("lantern","gold_idle_input")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_CLOCK).setAllTextures(0,PathTo("lantern","diamond")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_CLOCK_IDLE).setAllTextures(0,PathTo("lantern","diamond_idle")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_CLOCK_INPUT).setAllTextures(0,PathTo("lantern","diamond_input")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_CLOCK_IDLE_INPUT).setAllTextures(0,PathTo("lantern","diamond_idle_input")));
		d.addDispatch(new BlockModelLantern<>(ModBlocks.LANTERN_MUSHROOM).setAllTextures(0,PathTo("lantern","mushroom")));

		d.addDispatch(new BlockModelFullyRotatable<>(ModBlocks.PIPE).setTex(0, PathTo("pipe", "section"), Side.BOTTOM, Side.TOP)
			.setTex(0, PathTo("pipe", "side"), Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST));
		//SEASHELL
		d.addDispatch(new BlockModelOverlaySeashell<>(ModBlocks.OVERLAY_SEASHELL));
		//BRICKS MUD
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_MUD).setAllTextures(0,PathTo("brick_mud")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_BRICK_MUD).setAllTextures(0,PathTo("brick_mud")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_BRICK_MUD).setAllTextures(0,PathTo("brick_mud")));
		//BRICKS MUD BAKED
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_MUD_BAKED).setAllTextures(0,PathTo("brick_mud_baked")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_BRICK_MUD_BAKED).setAllTextures(0,PathTo("brick_mud_baked")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_BRICK_MUD_BAKED).setAllTextures(0,PathTo("brick_mud_baked")));
		//BRICKS CLAY MOSSY
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_CLAY_MOSSY).setAllTextures(0,PathTo("brick_clay_mossy")));
		//CORRUGATED IRON BLOCK
		d.addDispatch(new BlockModelAxisAligned<>(ModBlocks.BLOCK_IRON_CORRUGATED).setAllTextures(0,PathTo("corrugated_iron","default")));
		d.addDispatch(new BlockModelAxisAlignedPainted<>(ModBlocks.BLOCK_IRON_CORRUGATED_PAINTED));
	}
}
