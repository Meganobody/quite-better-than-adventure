package quitebetter.client.impl.btwaila.tooltips;

import quitebetter.core.tileentity.TileEntityGlued;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.Colors;

import java.awt.*;

public class GluedTooltip extends TileTooltip<TileEntityGlued> {
	@Override
	public void initTooltip() {
		addClass(TileEntityGlued.class);
	}

	@Override
	public void drawAdvancedTooltip(TileEntityGlued glued, AdvancedInfoComponent c) {
		if (glued.Glued)
			c.drawStringWithShadow("Glued", 0, Colors.GREEN);
	}
}
