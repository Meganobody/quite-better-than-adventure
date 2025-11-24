package quitebetter.client.blockmodel;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.block.model.BlockModelRope;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.block.model.BlockModelVeryRotatable;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import quitebetter.core.block.BlockLogicLantern;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelLantern<T extends BlockLogicLantern> extends BlockModelVeryRotatable {
	public BlockModelLantern(Block<T> block) {
		super(block);
	}
	private final double VERTICAL_MIN = 0; 
	private final double VERTICAL_MAX = 1;
	private final double HORIZONTAL_AVG = 0.5;
	private final double HORIZONTAL_AMP = 0.4; 
	private void makeLanternModel(Tessellator tessellator, IconCoordinate texIndex, double xd, double yd, double zd, Direction direction)
	{
		double minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV;
		switch (direction) {
			//negative directions
			case NORTH:
			case WEST:
			case DOWN:
				minU = texIndex.getIconUMin();
				maxU = texIndex.getIconUMax();
				minV = texIndex.getIconVMax();
				maxV = texIndex.getIconVMin();
				break;
			default:
				minU = texIndex.getIconUMin();
				maxU = texIndex.getIconUMax();
				minV = texIndex.getIconVMin();
				maxV = texIndex.getIconVMax();
				break;
		}
		switch (direction) {
			case NORTH:
			case SOUTH:
				minX = xd + HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxX = xd + HORIZONTAL_AVG + HORIZONTAL_AMP;
				minZ = zd + VERTICAL_MIN;
				maxZ = zd + VERTICAL_MAX;
				minY = yd + HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxY = yd + HORIZONTAL_AVG + HORIZONTAL_AMP;
				tessellator.addVertexWithUV(minX, minY, maxZ, minU, minV);
				tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
				tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
				tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
				tessellator.addVertexWithUV(maxX, maxY, minZ, minU, maxV);
				tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(minX, minY, maxZ, maxU, minV);
				tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
				tessellator.addVertexWithUV(minX, maxY, minZ, minU, maxV);
				tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);
				tessellator.addVertexWithUV(maxX, minY, maxZ, minU, minV);
				tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
				tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
				break;
			case WEST:
			case EAST:
				minY = yd + HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxY = yd + HORIZONTAL_AVG + HORIZONTAL_AMP;
				minX = xd + VERTICAL_MIN;
				maxX = xd + VERTICAL_MAX;
				minZ = zd + HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxZ = zd + HORIZONTAL_AVG + HORIZONTAL_AMP;
				tessellator.addVertexWithUV(maxX, minY, minZ, minU, minV);
				tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
				tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
				tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
				tessellator.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
				tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, minY, minZ, maxU, minV);
				tessellator.addVertexWithUV(maxX, minY, maxZ, minU, minV);
				tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
				tessellator.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
				tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
				tessellator.addVertexWithUV(minX, maxY, minZ, minU, maxV);
				tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, minV);
				break;
			case DOWN:
			default:
				minX = xd + HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxX = xd + HORIZONTAL_AVG + HORIZONTAL_AMP;
				minY = yd + VERTICAL_MIN;
				maxY = yd + VERTICAL_MAX;
				minZ = zd + HORIZONTAL_AVG - HORIZONTAL_AMP;
				maxZ = zd + HORIZONTAL_AVG + HORIZONTAL_AMP;
				tessellator.addVertexWithUV(minX, maxY, minZ, minU, minV);
				tessellator.addVertexWithUV(minX, minY, minZ, minU, maxV);
				tessellator.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
				tessellator.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
				tessellator.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
				tessellator.addVertexWithUV(minX, minY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(minX, maxY, minZ, maxU, minV);
				tessellator.addVertexWithUV(minX, maxY, maxZ, minU, minV);
				tessellator.addVertexWithUV(minX, minY, maxZ, minU, maxV);
				tessellator.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
				tessellator.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
				tessellator.addVertexWithUV(maxX, maxY, minZ, minU, minV);
				tessellator.addVertexWithUV(maxX, minY, minZ, minU, maxV);
				tessellator.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
				tessellator.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
				break;
		}
	}

	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		if (LightmapHelper.isLightmapEnabled()) {
			brightness = 1.0F;
		}

		float r = 1.0F;
		float g = 1.0F;
		float b = 1.0F;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (renderBlocks.useInventoryTint) {
			int color = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(this.block)).getFallbackColor(metadata);
			r = (float)(color >> 16 & 255) / 255.0F;
			g = (float)(color >> 8 & 255) / 255.0F;
			b = (float)(color & 255) / 255.0F;
		}
		
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
		if (renderBlocks.overrideBlockTexture != null) {
			texIndex = renderBlocks.overrideBlockTexture;
		}
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(r * brightness, g * brightness, b * brightness, alpha);
		if (LightmapHelper.isLightmapEnabled() && lightmapCoordinate != null) {
			tessellator.setLightmapCoord(lightmapCoordinate);
		}

		double xd = 0.0;
		double yd = 0.0;
		double zd = 0.0;
		makeLanternModel(tessellator, texIndex, xd, yd, zd, Direction.getDirectionById(metadata & 7));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	public boolean render(Tessellator tessellator, int x, int y, int z) {
    	float brightness = 1.0F;
    	if (!LightmapHelper.isLightmapEnabled()) {
    	   brightness = this.getBlockBrightness(renderBlocks.blockAccess, x, y, z);
    	} else {
    	   tessellator.setLightmapCoord(this.block.getLightmapCoord(renderBlocks.blockAccess, x, y, z));
    	}	
    	int color = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(this.block)).getWorldColor(renderBlocks.blockAccess, x, y, z);
    	float r = (color >> 16 & 255) / 255.0F;
    	float g = (color >> 8 & 255) / 255.0F;
    	float b = (color & 255) / 255.0F;
    	tessellator.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
    	double xd = (double)x;
    	double yd = (double)y;
    	double zd = (double)z;	
    	int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
    	IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
    	if (renderBlocks.overrideBlockTexture != null) {
    	   texIndex = renderBlocks.overrideBlockTexture;
    	}
    	makeLanternModel(tessellator, texIndex, xd, yd, zd, Direction.getDirectionById(metadata & 7));
    	return true;
	}
	//public boolean renderBlockWithBounds(Tessellator tessellator, int x, int y, int z) {
	//	float brightness = 1.0F;
	//	if (!LightmapHelper.isLightmapEnabled()) {
	//		brightness = this.getBlockBrightness(renderBlocks.blockAccess, x, y, z);
	//	} else {
	//		tessellator.setLightmapCoord(this.block.getLightmapCoord(renderBlocks.blockAccess, x, y, z));
	//	}
	//	int color = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(this.block)).getWorldColor(renderBlocks.blockAccess, x, y, z);
	//	float r = (float)(color >> 16 & 255) / 255.0F;
	//	float g = (float)(color >> 8 & 255) / 255.0F;
	//	float b = (float)(color & 255) / 255.0F;
	//	tessellator.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
	//	double xd = (double)x;
	//	double yd = (double)y;
	//	double zd = (double)z;
	//	
	//	int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
	//	IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
	//	if (renderBlocks.overrideBlockTexture != null) {
	//		texIndex = renderBlocks.overrideBlockTexture;
	//	}
	//	makeLanternModel(tessellator, texIndex, xd, yd, zd, Direction.getDirectionById(metadata & 7));
	//	tessellator.draw();
	//	return true;
	//}

	public boolean shouldItemRender3d() {
		return false;
	}

	public float getItemRenderScale() {
		return 0.5F;
	}
}
