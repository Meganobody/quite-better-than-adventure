package quitebetter.client.impl.btwaila.tooltips;

import net.minecraft.core.item.ItemStack;
import quitebetter.core.tileentity.TileEntityCrate;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class CrateTooltip extends TileTooltip<TileEntityCrate> {
	@Override
	public void initTooltip() {
		addClass(TileEntityCrate.class);
	}

	@Override
	public void drawAdvancedTooltip(TileEntityCrate crate, AdvancedInfoComponent c) {
		if (crate.ItemId == null) return;
		float percentage = ((float)crate.ItemCount/crate.maxItemCount)*100;
		int offY =  c.getOffY();
		c.addOffY(9);
		c.drawStringWithShadow("/"+crate.maxItemCount + " ("+String.format("%.2f", percentage)+"% full)", 18);
		c.setOffY(offY);
		c.drawItemList(new ItemStack[]{new ItemStack(crate.ItemId, crate.ItemCount, crate.ItemMeta)}, 0);
	}
}
