package quitebetter.core;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

import java.awt.*;

public class CustomItemModel extends ItemModelStandard {
	public CustomItemModel(Item item, String namespace) {
		super(item, namespace);
	}
	@Override
	public int getColor(ItemStack stack) {
		// Slowly shift its color as time increases
		return Color.HSBtoRGB((System.currentTimeMillis()%10000)/10000f, 1f, 1f);
	}
}
