package quitebetter.core.gui;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuActivator;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import quitebetter.core.tileentity.TileEntityAssembler;

import java.util.ArrayList;
import java.util.List;

public class ScreenAssembler extends ScreenContainerAbstract {
	protected final TileEntityAssembler tileEntityAssembler;

	public ScreenAssembler(ContainerInventory inventory, TileEntityAssembler tileEntityAssembler) {
		super(new MenuAssembler(inventory, tileEntityAssembler));
		this.tileEntityAssembler = tileEntityAssembler;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.textureManager.loadTexture("/assets/minecraft/textures/gui/container/crafting.png").bind();
		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
	}
	public void init() {
		Log.debug(LogCategory.LOG, "init!");
		super.init();
		int xLeft = (this.width - this.xSize) / 2;
		int yTop = (this.height - this.ySize) / 2;
		int startX = xLeft + 7;
		int startY = yTop + 34;
		this.buttons.clear();
		for(int l = 0; l < 3; ++l) {
			for(int k1 = 0; k1 < 3; ++k1) {
				ButtonElement button = new ButtonElement(k1 + l * 3, startX + 22 + k1 * 18, startY - 18 + l * 18, 18, 18, "");
				button.visible = false;
				add(button);
			}
		}
	}
	protected void buttonClicked(ButtonElement button) {
		Log.debug(LogCategory.LOG, String.format("button event click %d", button.id));
		tileEntityAssembler.lockSlot(button.id, !tileEntityAssembler.locked(button.id));
	}
}
