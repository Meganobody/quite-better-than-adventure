package quitebetter.client.tileentityrender;

import quitebetter.core.tileentity.TileEntityPedestal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class TileEntityRendererPedestal extends TileEntityRenderer<TileEntityPedestal> {
	public EntityItem renderEntity = new EntityItem((World)null);
	private Random rand = new Random();

	@Override
	public void doRender(Tessellator tessellator, TileEntityPedestal tileEntity, double x, double y, double z, float renderPartialTicks) {
		if (tileEntity.displayItem != null) {
			GL11.glPushMatrix();
			float bobbingOffset = MathHelper.sin(((float)tileEntity.ticksRan + renderPartialTicks) / 10.0F) * 0.1F + 0.1F;
			float f3 = (float)Math.toDegrees((double)(((float)tileEntity.ticksRan + renderPartialTicks) / 20.0F));
			GL11.glTranslatef((float)x + 0.5F, (float)y + bobbingOffset + 0.25F, (float)z + 0.5F);
			this.renderEntity.world = tileEntity.worldObj;
			this.renderEntity.x = this.renderEntity.xo = (double)tileEntity.x + (double)0.5F;
			this.renderEntity.y = this.renderEntity.yo = (double)tileEntity.y + (double)0.5F;
			this.renderEntity.z = this.renderEntity.zo = (double)tileEntity.z + (double)0.5F;
			float lightLevel = Minecraft.getMinecraft().currentWorld.getLightBrightness(tileEntity.x, tileEntity.y, tileEntity.z);
			if (Global.accessor.isFullbrightEnabled()) {
				lightLevel = 1.0F;
			}

			BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
			GL11.glEnable(32826);
			GL11.glEnable(3042);
			ItemModelDispatcher.getInstance().getDispatch(tileEntity.displayItem).renderAsItemEntity(Tessellator.instance, this.renderEntity, this.rand, tileEntity.displayItem, 1, f3, lightLevel, renderPartialTicks);
			GL11.glDisable(3042);
			GL11.glDisable(32826);
			GL11.glPopMatrix();
			this.renderEntity.world = null;
		}
	}
}
