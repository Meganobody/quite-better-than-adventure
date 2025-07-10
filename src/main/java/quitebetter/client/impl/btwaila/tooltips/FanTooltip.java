package quitebetter.client.impl.btwaila.tooltips;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import quitebetter.core.block.BlockLogicFan;
import quitebetter.core.tileentity.TileEntityCrate;
import quitebetter.core.tileentity.TileEntityFan;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.Colors;

import java.awt.*;

public class FanTooltip extends TileTooltip<TileEntityFan> {
	@Override
	public void initTooltip() {
		addClass(TileEntityFan.class);
	}

	@Override
	public void drawAdvancedTooltip(TileEntityFan fan, AdvancedInfoComponent c) {
		World worldObj = fan.worldObj;
		if (((BlockLogicFan) fan.getBlock().getLogic()).isActive() && !((BlockLogicFan) fan.getBlock().getLogic()).isSignalOverriden(worldObj,fan.x,fan.y,fan.z)) {
			c.drawStringWithShadow("On", 0, Colors.GREEN);
		} else {
			c.drawStringWithShadow("Off", 0, Colors.RED);
		}
	}
}
