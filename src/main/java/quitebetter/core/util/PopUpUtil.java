package quitebetter.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.popup.PopupBuilder;
import net.minecraft.client.gui.popup.PopupScreen;

public class PopUpUtil {
	public static PopupScreen buildConfigInfo() {
		return (new PopupBuilder(Minecraft.getMinecraft().currentScreen, 350))
			.closeOnEsc(0)
			.closeOnClickOut(0)
			.closeOnEnter(1)
			.withMessageBox("message",200,"Quite Better Than Adventure System Message:\n( ! ) Current Config file is outdated.\n( ! ) Unknown ID: HOOK",100)
			.build();
	}

}
