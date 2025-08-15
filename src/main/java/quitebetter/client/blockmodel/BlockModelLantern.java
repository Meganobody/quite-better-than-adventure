package quitebetter.client.blockmodel;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelLantern<T extends BlockLogic> extends BlockModelCrossedSquares<T> {
	public BlockModelLantern(Block<T> block) {
		super(block);
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

		float yOffset = 0.0F;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
		if (renderBlocks.overrideBlockTexture != null) {
			texIndex = renderBlocks.overrideBlockTexture;
		}

		double minU = texIndex.getIconUMin();
		double maxU = texIndex.getIconUMax();
		double minV = texIndex.getIconVMin();
		double maxV = texIndex.getIconVMax();
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(r * brightness, g * brightness, b * brightness, alpha);
		if (LightmapHelper.isLightmapEnabled() && lightmapCoordinate != null) {
			tessellator.setLightmapCoord(lightmapCoordinate);
		}

		double xd = (double)0.0F;
		double yd = (double)0.0F;
		double zd = (double)0.0F;
		double minX = xd + (double)0.5F - 0.45;
		double maxX = xd + (double)0.5F + 0.45;
		double minZ = zd + (double)0.5F - 0.45;
		double maxZ = zd + (double)0.5F + 0.45;
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, maxZ, maxU, minV);
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
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		tessellator.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
		double xd = (double)x;
		double yd = (double)y;
		double zd = (double)z;
		if (this.block == Blocks.TALLGRASS || this.block == Blocks.TALLGRASS_FERN || this.block == Blocks.SPINIFEX) {
			long dRandom = (long)x * 3129871L ^ (long)z * 116129781L ^ (long)y;
			dRandom = dRandom * dRandom * 42317861L + dRandom * 11L;
			xd += ((double)((float)(dRandom >> 16 & 15L) / 15.0F) - (double)0.5F) * (double)0.5F;
			yd += ((double)((float)(dRandom >> 20 & 15L) / 15.0F) - (double)1.0F) * 0.2;
			zd += ((double)((float)(dRandom >> 24 & 15L) / 15.0F) - (double)0.5F) * (double)0.5F;
		}

		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
		if (renderBlocks.overrideBlockTexture != null) {
			texIndex = renderBlocks.overrideBlockTexture;
		}

		double minU = texIndex.getIconUMin();
		double maxU = texIndex.getIconUMax();
		double minV = texIndex.getIconVMin();
		double maxV = texIndex.getIconVMax();
		float yOffset = 0.0F;
		double minX = xd + (double)0.5F - 0.4;
		double maxX = xd + (double)0.5F + 0.4;
		double minZ = zd + (double)0.5F - 0.4;
		double maxZ = zd + (double)0.5F + 0.4;
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, maxZ, maxU, minV);
		return true;
	}

	public boolean shouldItemRender3d() {
		return false;
	}

	public float getItemRenderScale() {
		return 0.5F;
	}
}
