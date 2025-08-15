package quitebetter.client.blockmodel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;

import static quitebetter.core.ModCore.MOD_ID;

public class BlockModelPedestal<T extends BlockLogic> extends BlockModelStandard<T> {
	private final @NotNull IconCoordinate topTexture;
	private final @NotNull IconCoordinate sideTexture;
	private final @NotNull IconCoordinate bottomTexture;

	public BlockModelPedestal(Block<T> block) {
		super(block);
		this.topTexture = TextureRegistry.getTexture(MOD_ID+":block/pedestal/top");
		this.sideTexture = TextureRegistry.getTexture(MOD_ID+":block/pedestal/side");
		this.bottomTexture = TextureRegistry.getTexture(MOD_ID+":block/pedestal/bottom");
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		return side == Side.TOP ? this.topTexture : this.bottomTexture;
	}

	public boolean shouldItemRender3d() { return false; }

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		boolean fullbright = Minecraft.getMinecraft().isFullbrightEnabled();
		WorldSource world = this.renderBlocks.blockAccess;
		float l;
		l = world.getLightBrightness(x,y,z); tessellator.setColorRGBA((int) ((255*l)*0.5),(int) ((255*l)*0.5),(int) ((255*l)*0.5),255);
		this.renderStandardBlock(tessellator, this.block.getBounds(), x, y, z);
		AABB sidebounds = AABB.getPermanentBB(0,0,0,1,1,1);

		double minU = this.sideTexture.getIconUMin();
		double maxU = this.sideTexture.getIconUMax();
		double minV = this.sideTexture.getIconVMin();
		double maxV = this.sideTexture.getIconVMax();
		double w = 0.1;
		//WEST
		l = (world.getLightBrightness(x-1,y-1,z)+world.getLightBrightness(x-1,y,z))/2;
		if (!fullbright) { tessellator.setColorRGBA((int) ((255*l)*l*0.6),(int) ((255*l)*0.6),(int) ((255*l)*0.6),255); }

		tessellator.addVertexWithUV(x, y+0.1, z, minU, minV);
		tessellator.addVertexWithUV(x-w, y-1+0.1, z, minU, maxV);
		tessellator.addVertexWithUV(x-w, y-1+0.1, z+1, maxU, maxV);
		tessellator.addVertexWithUV(x, y+0.1, z+1, maxU, minV);

		//EAST
		l = (world.getLightBrightness(x+1,y-1,z)+world.getLightBrightness(x+1,y,z))/2;
		if (!fullbright) { tessellator.setColorRGBA((int) ((255*l)*0.6),(int) ((255*l)*0.6),(int) ((255*l)*0.6),255); }

		tessellator.addVertexWithUV(x+1, y+0.1, z+1, minU, minV);
		tessellator.addVertexWithUV(x+1+w, y-1+0.1, z+1, minU, maxV);
		tessellator.addVertexWithUV(x+1+w, y-1+0.1, z, maxU, maxV);
		tessellator.addVertexWithUV(x+1, y+0.1, z, maxU, minV);

		//SOUTH
		l = (world.getLightBrightness(x,y-1,z+1)+world.getLightBrightness(x,y,z+1))/2;
		if (!fullbright) { tessellator.setColorRGBA((int) ((255*l)*0.8),(int) ((255*l)*0.8),(int) ((255*l)*0.8),255); }
		tessellator.addVertexWithUV(x, y+0.1, z+1, minU, minV);
		tessellator.addVertexWithUV(x, y -1+w, z+1 +w, minU, maxV);
		tessellator.addVertexWithUV(x+1, y -1+w, z+1 +w, maxU, maxV);
		tessellator.addVertexWithUV(x+1, y+0.1, z+1, maxU, minV);

		//NORTH
		l = (world.getLightBrightness(x,y-1,z-1)+world.getLightBrightness(x,y,z-1))/2;
		if (!fullbright) { tessellator.setColorRGBA((int) (255*l*0.8),(int) (255*l*0.8),(int) (255*l*0.8),255); }
		tessellator.addVertexWithUV(x+1, y+0.1, z, minU, minV);
		tessellator.addVertexWithUV(x+1, y -1+0.1, z -w, minU, maxV);
		tessellator.addVertexWithUV(x, y -1+0.1, z -w, maxU, maxV);
		tessellator.addVertexWithUV(x, y+0.1, z, maxU, minV);
		return true;
	}
}
