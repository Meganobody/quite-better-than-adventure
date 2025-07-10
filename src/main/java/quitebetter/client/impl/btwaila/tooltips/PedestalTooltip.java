package quitebetter.client.impl.btwaila.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import quitebetter.core.tileentity.TileEntityCrate;
import quitebetter.core.tileentity.TileEntityPedestal;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class PedestalTooltip extends TileTooltip<TileEntityPedestal> {
	@Override
	public void initTooltip() {
		addClass(TileEntityPedestal.class);
	}

	@Override
	public void drawAdvancedTooltip(TileEntityPedestal pedestal, AdvancedInfoComponent c) {
		if (pedestal.displayItem == null) return;
		int offY =  c.getOffY();
		c.addOffY(5);

		Lighting.enableInventoryLight();
		GL11.glEnable(32826);
		GL11.glEnable(2929);
		Tessellator t = Tessellator.instance;
		ItemModel model = ItemModelDispatcher.getInstance().getDispatch(pedestal.displayItem);
		c.drawStringWithShadow(""+pedestal.displayItem.getDisplayName(), 18);
		c.setOffY(offY);
		model.renderItemIntoGui(t, c.minecraft.font, c.minecraft.textureManager, pedestal.displayItem, c.getPosX(), 40, 9, 1.0F);
		c.addOffY(16);
		GL11.glDisable(2929);
		GL11.glDisable(2896);
		Lighting.disable();
	}
}
