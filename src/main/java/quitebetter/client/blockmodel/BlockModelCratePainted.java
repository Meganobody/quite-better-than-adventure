package quitebetter.client.blockmodel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;

import static quitebetter.core.ModCore.MOD_ID;

@Environment(EnvType.CLIENT)
public class BlockModelCratePainted<T extends BlockLogic> extends BlockModelStandard<T> {
	public static final IconCoordinate[] texCoords = new IconCoordinate[16];
	public BlockModelCratePainted(Block block) {
		super(block);
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		return texCoords[data & 15];
	}

	static {
		for(DyeColor c : DyeColor.blockOrderedColors()) {
			texCoords[c.blockMeta] = TextureRegistry.getTexture(MOD_ID+":block/crate/" + c.colorID);
		}

	}
}
