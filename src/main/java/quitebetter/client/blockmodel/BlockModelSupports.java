package quitebetter.client.blockmodel;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelSupports<T extends BlockLogic> extends BlockModelStandard<T> {

	public BlockModelSupports(Block<T> block) {
		super(block);
	}

	public boolean shouldRenderSide(Side side, int x, int y, int z, int meta) {
		Block frontblock = renderBlocks.blockAccess.getBlock(x-side.getOffsetX(),y-side.getOffsetY(),z-side.getOffsetZ());
		return (!getBlockTextureFromSideAndMetadata(side, meta).getImageSource().contains("top") || (frontblock==null || !frontblock.isCubeShaped()) );
	}

	@Override
	public void renderBlockWithBounds(Tessellator tessellator, AABB bounds, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		if (LightmapHelper.isLightmapEnabled() && lightmapCoordinate != null) {
			LightmapHelper.setLightmapCoord(lightmapCoordinate);
		}

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		this.renderTopFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockTextureFromSideAndMetadata(Side.TOP, metadata));
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		this.renderNorthFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata));
		this.renderNorthFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.99F, this.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata));
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		this.renderSouthFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata));
		this.renderSouthFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)-0.99F, this.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata));
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		this.renderWestFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockTextureFromSideAndMetadata(Side.WEST, metadata));
		this.renderWestFace(tessellator, bounds, (double)0.99F, (double)0.0F, (double)0.0F, this.getBlockTextureFromSideAndMetadata(Side.WEST, metadata));
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		this.renderEastFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockTextureFromSideAndMetadata(Side.EAST, metadata));
		this.renderEastFace(tessellator, bounds, (double)-0.99F, (double)0.0F, (double)0.0F, this.getBlockTextureFromSideAndMetadata(Side.EAST, metadata));
		tessellator.draw();
		if (this.hasOverbright()) {
			brightness = 1.0F;
			if (renderBlocks.useInventoryTint) {
				int color = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(this.block)).getFallbackColor(metadata);
				float r = (float)(color >> 16 & 255) / 255.0F;
				float g = (float)(color >> 8 & 255) / 255.0F;
				float b = (float)(color & 255) / 255.0F;
				GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
			} else {
				GL11.glColor4f(brightness, brightness, brightness, alpha);
			}

			if (LightmapHelper.isLightmapEnabled()) {
				LightmapHelper.setLightmapCoord(15, 15);
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockOverbrightTextureFromSideAndMeta(Side.BOTTOM, metadata));
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockOverbrightTextureFromSideAndMeta(Side.TOP, metadata));
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			this.renderNorthFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockOverbrightTextureFromSideAndMeta(Side.NORTH, metadata));
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			this.renderSouthFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockOverbrightTextureFromSideAndMeta(Side.SOUTH, metadata));
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderWestFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockOverbrightTextureFromSideAndMeta(Side.WEST, metadata));
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			this.renderEastFace(tessellator, bounds, (double)0.0F, (double)0.0F, (double)0.0F, this.getBlockOverbrightTextureFromSideAndMeta(Side.EAST, metadata));
			tessellator.draw();
		}

	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Axis axis = BlockLogicAxisAligned.metaToAxis(meta);
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
		AABB bounds = this.block.getBounds();
		int color = ((BlockColor) BlockColorDispatcher.getInstance().getDispatch(this.block)).getWorldColor(renderBlocks.blockAccess, x, y, z);
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		renderBlocks.enableAO = true;
		renderBlocks.cache.setupCache(this.block, renderBlocks.blockAccess, x, y, z);
		//OUTSIDE
		for(Side side : Side.sides) {
			Block frontblock = renderBlocks.blockAccess.getBlock(x+side.getOffsetX(),y+side.getOffsetY(),z+side.getOffsetZ());
			if (!getBlockTextureFromSideAndMetadata(side, meta).getImageSource().contains("top") || (frontblock==null || !frontblock.isCubeShaped()) ) {
				renderBlocks.renderSide(tessellator, this, bounds, x, y, z, r, g, b, side, meta);
			}
		}
		//INSIDE
		double in=0.995;
		Side side;
		side = Side.SOUTH;
		if (shouldRenderSide(side,x,y,z,meta)) { renderBlocks.renderSouthFace(tessellator, bounds, x-side.getOffsetX()*in,y-side.getOffsetY()*in,z-side.getOffsetZ()*in, getBlockTextureFromSideAndMetadata(side,meta)); }
		side = Side.WEST;
		if (shouldRenderSide(side,x,y,z,meta)) { renderBlocks.renderWestFace(tessellator, bounds, x-side.getOffsetX()*in,y-side.getOffsetY()*in,z-side.getOffsetZ()*in, getBlockTextureFromSideAndMetadata(side,meta)); }
		side = Side.NORTH;
		if (shouldRenderSide(side,x,y,z,meta)) { renderBlocks.renderNorthFace(tessellator, bounds, x-side.getOffsetX()*in,y-side.getOffsetY()*in,z-side.getOffsetZ()*in, getBlockTextureFromSideAndMetadata(side,meta)); }
		side = Side.EAST;
		if (shouldRenderSide(side,x,y,z,meta)) { renderBlocks.renderEastFace(tessellator, bounds, x-side.getOffsetX()*in,y-side.getOffsetY()*in,z-side.getOffsetZ()*in, getBlockTextureFromSideAndMetadata(side,meta)); }
		side = Side.TOP;
		if (shouldRenderSide(side,x,y,z,meta)) { renderBlocks.renderTopFace(tessellator, bounds, x-side.getOffsetX()*in,y-side.getOffsetY()*in,z-side.getOffsetZ()*in, getBlockTextureFromSideAndMetadata(side,meta)); }
		side = Side.BOTTOM;
		if (shouldRenderSide(side,x,y,z,meta)) { renderBlocks.renderBottomFace(tessellator, bounds, x-side.getOffsetX()*in,y-side.getOffsetY()*in,z-side.getOffsetZ()*in, getBlockTextureFromSideAndMetadata(side,meta)); }
		renderBlocks.enableAO = false;
		this.resetRenderBlocks();
		return true;
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		return 6 * (data & 3) + side.getId() >= Sides.orientationLookUpXYZAligned.length ? TextureRegistry.getTexture("minecraft:block/grass_top") : super.getBlockTextureFromSideAndMetadata(Side.getSideById(Sides.orientationLookUpXYZAligned[6 * (data & 3) + side.getId()]), data);
	}
}
