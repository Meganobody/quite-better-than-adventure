package quitebetter.client.impl.btwaila.tooltips;

import quitebetter.core.tileentity.TileEntityGlued;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.Colors;

public class GluedTooltip extends TileTooltip<TileEntityGlued> {
	@Override
	public void initTooltip() {
		addClass(TileEntityGlued.class);
	}

	@Override
	public void drawAdvancedTooltip(TileEntityGlued tile, AdvancedInfoComponent c) {
		if (tile.isGlued())
			c.drawStringWithShadow("Glued", 0, Colors.GREEN);
	}
}
