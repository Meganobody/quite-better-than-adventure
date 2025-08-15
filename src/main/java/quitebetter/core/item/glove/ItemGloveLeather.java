package quitebetter.core.item.glove;

import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ArmorMaterial;
import org.jetbrains.annotations.Nullable;

public class ItemGloveLeather extends Item implements IArmorItem {
	public ItemGloveLeather(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

	@Override
	public @Nullable ArmorMaterial getArmorMaterial() {
		return null;
	}

	public int getArmorPiece() {
		return 2;
	}
}
