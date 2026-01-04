package quitebetter.client.itemmodel;

import com.formdev.flatlaf.util.AnimatedIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.*;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.font.Fonts;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

import static quitebetter.core.ModCore.MOD_ID;

public class ItemModelNumber extends ItemModelStandard {
	public IconCoordinate[] icons;
	public ItemModelNumber(Item item, String namespace) {
		super(item, namespace);
		icons = new IconCoordinate[10];

		for (int i = 0; i < 10; i++) {
			IconCoordinate coordinate = TextureRegistry.getTexture(MOD_ID+":item/number/number"+i);
			icons[i] = coordinate;
		}
	}
	public void renderAsItemEntity(Tessellator tessellator, @Nullable Entity entity, Random random, ItemStack itemstack, int renderCount, float yaw, float brightness, float partialTick) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.fullbright || this.itemfullBright) {
			brightness = 1.0F;
		}

		EntityRenderDispatcher renderDispatcher = EntityRenderDispatcher.instance;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		if (this.useColor) {
			int color = this.getColor(itemstack);
			float r = (float)(color >> 16 & 255) / 255.0F;
			float g = (float)(color >> 8 & 255) / 255.0F;
			float b = (float)(color & 255) / 255.0F;
			GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0F);
		} else {
			GL11.glColor4f(brightness, brightness, brightness, 1.0F);
		}

		if (LightmapHelper.isLightmapEnabled() && this.itemfullBright && entity != null) {
			int lmc = entity.getLightmapCoord(1.0F);
			lmc = LightmapHelper.setBlocklightValue(lmc, 15);
			LightmapHelper.setLightmapCoord(lmc);
		}

		if ((Boolean)mc.gameSettings.items3D.value) {
			GL11.glPushMatrix();
			GL11.glScaled((double)1.0F, (double)1.0F, (double)1.0F);
			GL11.glRotated((double)yaw, (double)0.0F, (double)1.0F, (double)0.0F);
			GL11.glTranslated((double)-0.5F, (double)0.0F, -0.05 * (double)(renderCount - 1));

			for(int i = 0; i < renderCount; ++i) {
				GL11.glPushMatrix();
				GL11.glTranslated((double)0.0F, (double)0.0F, 0.1 * (double)i);
				this.renderItem(tessellator, renderDispatcher.itemRenderer, itemstack, entity, brightness, false);
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
		} else {
			for(int i = 0; i < renderCount; ++i) {
				GL11.glPushMatrix();
				if (i > 0) {
					float rOffX = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float rOffY = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float rOffZ = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(rOffX, rOffY, rOffZ);
				}

				GL11.glRotatef(180.0F - renderDispatcher.viewLerpYaw, 0.0F, 1.0F, 0.0F);
				IconCoordinate tex;
				int amount = itemstack.stackSize;
				if (amount >= 10)
				{
					tex = this.getIcon(entity, amount % 10);
					tex.parentAtlas.bind();
					renderHalf(tessellator, tex, true);
					tex = this.getIcon(entity, amount / 10);
					tex.parentAtlas.bind();
					renderHalf(tessellator, tex, false);
				}
				else
				{
					tex = this.getIcon(entity, amount % 10);
					tex.parentAtlas.bind();
					renderFlat(tessellator, tex);
				}
				GL11.glPopMatrix();
			}
		}

	}
	public void renderHalf(Tessellator tessellator, IconCoordinate index, boolean second) {
		float xOff = 0.5F;
		float yOff = 0.25F;
		float z = 0.0F;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(second ? -0.0 : -0.5, (double)-0.25F, (double)0.0F, index.getIconUMin(), index.getIconVMax());
		tessellator.addVertexWithUV(second ? +0.5 : +0.0, (double)-0.25F, (double)0.0F, index.getIconUMax(), index.getIconVMax());
		tessellator.addVertexWithUV(second ? +0.5 : +0.0, (double)0.75F, (double)0.0F, index.getIconUMax(), index.getIconVMin());
		tessellator.addVertexWithUV(second ? -0.0 : -0.5, (double)0.75F, (double)0.0F, index.getIconUMin(), index.getIconVMin());
		tessellator.draw();
	}
	public @NotNull IconCoordinate getIcon(@Nullable Entity entity, int digit) {
		return icons[digit];
	}
	public @NotNull ItemModelStandard setIcon(@NotNull String texture, int i) {
		IconCoordinate coordinate = TextureRegistry.getTexture(texture);
		icons[i] = coordinate;
		return this;
	}
	public @NotNull ItemModelStandard setIcon(@NotNull NamespaceID texture, int i) {
		IconCoordinate coordinate = TextureRegistry.getTexture(texture);
		icons[i] = coordinate;
		return this;
	}
}
