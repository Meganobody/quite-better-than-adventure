package quitebetter.client.blockmodel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import static quitebetter.core.ModCore.MOD_ID;

@Environment(EnvType.CLIENT)
public class BlockModelOverlaySeashell<T extends BlockLogic> extends BlockModelStandard<T> {
	protected IconCoordinate[] seashells = new IconCoordinate[]{
		TextureRegistry.getTexture(MOD_ID+":block/seashells/0"),
		TextureRegistry.getTexture(MOD_ID+":block/seashells/1"),
		TextureRegistry.getTexture(MOD_ID+":block/seashells/2")};

	public BlockModelOverlaySeashell(Block<T> block) {
		super(block);
	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		float brightness = 1.0F;
		if (LightmapHelper.isLightmapEnabled()) {
			tessellator.setLightmapCoord(LightmapHelper.max(this.block.getLightmapCoord(renderBlocks.blockAccess, x, y, z), this.block.getLightmapCoord(renderBlocks.blockAccess, x, y - 1, z)));
		} else {
			brightness = Math.max(this.getBlockBrightness(renderBlocks.blockAccess, x, y, z), this.getBlockBrightness(renderBlocks.blockAccess, x, y - 1, z));
		}

		tessellator.setColorOpaque_F(brightness, brightness, brightness);
		this.renderTopFace(tessellator, this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z), (double)x, (double)y, (double)z, this.getBlockTextureFromSideAndMetadata(Side.TOP, renderBlocks.blockAccess.getBlockMetadata(x, y, z)));
		return true;
	}

	public boolean shouldItemRender3d() {
		return false;
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		return this.seashells[MathHelper.clamp(data, 0, 2)];
	}
}
