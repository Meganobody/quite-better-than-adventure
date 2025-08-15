package quitebetter.client.blockmodel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelAxisAligned;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.*;
import net.minecraft.core.util.helper.*;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import quitebetter.core.block.BlockLogicAxisAlignedPainted;

import static quitebetter.core.ModCore.MOD_ID;

@Environment(EnvType.CLIENT)
public class BlockModelAxisAlignedPainted<T extends BlockLogic> extends BlockModelStandard<T> {
	public static final IconCoordinate[] texCoords = new IconCoordinate[16];
	public BlockModelAxisAlignedPainted(Block block) {
		super(block);
	}

	static {
		for(DyeColor c : DyeColor.blockOrderedColors()) {
			texCoords[c.blockMeta] = TextureRegistry.getTexture(MOD_ID+":block/corrugated_iron/" + c.colorID);
		}
	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Axis axis = BlockLogicAxisAlignedPainted.metaToAxis(meta);
		switch (axis) {
			case Y:
				renderBlocks.uvRotateEast = 0;
				renderBlocks.uvRotateWest = 0;
				renderBlocks.uvRotateSouth = 0;
				renderBlocks.uvRotateNorth = 0;
				break;
			case Z:
				renderBlocks.uvRotateSouth = 1;
				renderBlocks.uvRotateNorth = 1;
				break;
			case X:
				renderBlocks.uvRotateEast = 1;
				renderBlocks.uvRotateWest = 1;
				renderBlocks.uvRotateTop = 1;
				renderBlocks.uvRotateBottom = 1;
		}

		this.renderStandardBlock(tessellator, this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z), x, y, z);
		this.resetRenderBlocks();
		return true;
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		return texCoords[(data & 240) >> 4];
	}
}
