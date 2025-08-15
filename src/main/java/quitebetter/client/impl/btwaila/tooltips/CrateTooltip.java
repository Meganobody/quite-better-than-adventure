package quitebetter.client.impl.btwaila.tooltips;

import net.minecraft.core.item.ItemStack;
import quitebetter.core.tileentity.TileEntityCrate;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.Colors;

public class CrateTooltip extends TileTooltip<TileEntityCrate> {
	@Override
	public void initTooltip() {
		addClass(TileEntityCrate.class);
	}

	@Override
	public void drawAdvancedTooltip(TileEntityCrate crate, AdvancedInfoComponent c) {
		if (crate.glued) { c.drawStringWithShadow("Glued", 0, Colors.GREEN); return; }
		if (crate.itemID == null) return;
		int maxitemcount = crate.getOverallMaxSpace();
		int itemcount = crate.getOverallFullness();
		float percentage = ((float)itemcount/maxitemcount)*100;
		int offY =  c.getOffY();
		c.addOffY(5);
		c.drawStringWithShadow(itemcount+"/"+maxitemcount + " ("+String.format("%.2f", percentage)+"% of "+crate.getCrateCount()+" )", 16);
		c.setOffY(offY);
		c.drawItemList(new ItemStack[]{new ItemStack(crate.itemID, 1, crate.itemMeta)}, 0);
	}
}
