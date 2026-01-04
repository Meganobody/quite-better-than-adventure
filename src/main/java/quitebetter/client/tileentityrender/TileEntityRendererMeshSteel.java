package quitebetter.client.tileentityrender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import quitebetter.core.item.ModItems;
import quitebetter.core.tileentity.TileEntityMeshSteel;

import java.util.Random;

public class TileEntityRendererMeshSteel extends TileEntityRenderer<TileEntityMeshSteel> {
	public EntityItem renderEntity = new EntityItem(null);
	private Random rand = new Random();

	public void doRender(Tessellator tessellator, TileEntityMeshSteel tileEntity, double x, double y, double z, float renderPartialTicks) {
		if (tileEntity.stackSize > 0) {
			GL11.glPushMatrix();
			float f3 = (float)Math.toDegrees((double)(((float)0 + renderPartialTicks) / 20.0F));
			GL11.glTranslatef((float)x + 0.5F, (float)y + 0.25F, (float)z + 0.5F);
			this.renderEntity.world = tileEntity.worldObj;
			this.renderEntity.x = this.renderEntity.xo = tileEntity.x + 0.5;
			this.renderEntity.y = this.renderEntity.yo = tileEntity.y + 0.5;
			this.renderEntity.z = this.renderEntity.zo = tileEntity.z + 0.5;
			float lightLevel = Minecraft.getMinecraft().currentWorld.getLightBrightness(tileEntity.x, tileEntity.y, tileEntity.z);
			if (Global.accessor.isFullbrightEnabled()) {
				lightLevel = 1.0F;
			}

			BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
			GL11.glEnable(32826);
			GL11.glEnable(3042);
			ItemModelDispatcher.getInstance().getDispatch(ModItems.NUMBER)
				.renderAsItemEntity(Tessellator.instance, this.renderEntity, this.rand, new ItemStack(ModItems.NUMBER, tileEntity.stackSize, tileEntity.stackSize), 1, f3, lightLevel, renderPartialTicks);
			GL11.glDisable(3042);
			GL11.glDisable(32826);
			GL11.glPopMatrix();
			this.renderEntity.world = null;
		}
	}
}
