package quitebetter.client.blockmodel;

import quitebetter.core.block.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.*;

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
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BLOCK_BONE).withTextures(PathTo("block_bone")));
		//OBSIDIAN POLISHED
		d.addDispatch(new BlockModelStandard<>(ModBlocks.OBSIDIAN_POLISHED).withTextures(PathTo("obsidian_polished")));
		//OLIVINEBRICKS
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_OLIVINE).withTextures(PathTo("bricks_olivine")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_BRICK_OLIVINE).withTextures(PathTo("bricks_olivine")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_BRICK_OLIVINE).withTextures(PathTo("bricks_olivine")));
		//SEASHELLTILES
		d.addDispatch(new BlockModelStandard<>(ModBlocks.TILE_SEASHELL).withTextures(PathTo("tiles_seashell")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_TILE_SEASHELL).withTextures(PathTo("tiles_seashell")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_TILE_SEASHELL).withTextures(PathTo("tiles_seashell")));
		//HAZARD BLOCK
		d.addDispatch(new BlockModelStandard<>(ModBlocks.HAZARD).withTextures(PathTo("hazard_block","default")));
		d.addDispatch(new BlockModelHazardPainted<>(ModBlocks.HAZARD_PAINTED));
		//FAN
		d.addDispatch(new BlockModelFan<>(ModBlocks.FAN,false,false));
		d.addDispatch(new BlockModelFan<>(ModBlocks.ACTIVE_FAN,true,false));
		d.addDispatch(new BlockModelFan<>(ModBlocks.IN_FAN,false,true));
		d.addDispatch(new BlockModelFan<>(ModBlocks.ACTIVE_IN_FAN,true,true));
		//CONVEYOR
		d.addDispatch(new BlockModelConveyor<>(ModBlocks.CONVEYOR,false));
		d.addDispatch(new BlockModelConveyor<>(ModBlocks.ACTIVE_CONVEYOR,true));
		//SUPPORTS
		d.addDispatch(new BlockModelSupports<>(ModBlocks.SUPPORT_STEEL).withTextures(PathTo("support","steel_top"),PathTo("support","steel")));
		d.addDispatch(new BlockModelSupports<>(ModBlocks.SUPPORT_IRON).withTextures(PathTo("support","iron_top"),PathTo("support","iron")));
		//GLOWING MUSHROOM
		d.addDispatch(new BlockModelCrossedSquares<>(ModBlocks.MUSHROOM_GLOWING).withTextures(PathTo("mushroom_glowing")));
		//PEDESTAL
		d.addDispatch(new BlockModelPedestal<>(ModBlocks.PEDESTAL));
		//SNOW BRICKS
		d.addDispatch(new BlockModelStandard<>(ModBlocks.BRICK_SNOW).withTextures(PathTo("bricks_snow")));
		d.addDispatch(new BlockModelStairs<>(ModBlocks.STAIRS_BRICK_SNOW).withTextures(PathTo("bricks_snow")));
		d.addDispatch(new BlockModelSlab<>(ModBlocks.SLAB_BRICK_SNOW).withTextures(PathTo("bricks_snow")));
		//ARROW TORCH
		d.addDispatch(new BlockModelTorch<>(ModBlocks.ARROW_TORCH).withTextures(PathTo("arrow_torch")));
		//CRATE
		d.addDispatch(new BlockModelStandard<>(ModBlocks.CRATE).withTextures(PathTo("crate","default")));
		d.addDispatch(new BlockModelCratePainted<>(ModBlocks.CRATE_PAINTED));
		//GEODE
		d.addDispatch(new BlockModelStandard<>(ModBlocks.GEODE).withTextures(PathTo("geode")));
	}
}
