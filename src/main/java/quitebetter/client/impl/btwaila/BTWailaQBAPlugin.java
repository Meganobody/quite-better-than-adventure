package quitebetter.client.impl.btwaila;

import org.slf4j.Logger;
import quitebetter.client.impl.btwaila.tooltips.CrateTooltip;
import quitebetter.client.impl.btwaila.tooltips.FanTooltip;
import quitebetter.client.impl.btwaila.tooltips.GluedTooltip;
import quitebetter.client.impl.btwaila.tooltips.PedestalTooltip;
import toufoumaster.btwaila.entryplugins.waila.BTWailaCustomTooltipPlugin;
import toufoumaster.btwaila.tooltips.TooltipRegistry;

public class BTWailaQBAPlugin implements BTWailaCustomTooltipPlugin {
	@Override
	public void initializePlugin(TooltipRegistry tooltipRegistry, Logger logger) {
		tooltipRegistry.register(new CrateTooltip());
		tooltipRegistry.register(new PedestalTooltip());
		tooltipRegistry.register(new FanTooltip());
		tooltipRegistry.register(new GluedTooltip());
	}
}
