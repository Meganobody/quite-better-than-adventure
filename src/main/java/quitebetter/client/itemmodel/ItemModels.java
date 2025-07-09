package quitebetter.client.itemmodel;

import quitebetter.core.item.ModItems;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.ModelHelper;

import java.util.function.Supplier;

import static quitebetter.core.ModCore.MOD_ID;

public class ItemModels {
	enum ModelTypes {
		ITEM, TOOL, SACK
	}
	private static void Add(@NotNull Item item, String texture, @NotNull ItemModels.ModelTypes modeltype) {
		String prevkey = item.getKey();
		Supplier func;
		switch (modeltype) {
			case TOOL:
				func = () -> new ItemModelStandard(item, MOD_ID)
					//.setRotateWhenRendering()
					.setFull3D();
				break;
			case SACK:
				func = () -> new ItemModelSack(item, MOD_ID);
				break;
			case ITEM:
			default:
				func = () -> new ItemModelStandard(item, MOD_ID);
				break;
		}
		ModelHelper.setItemModel(item.setKey(texture), func);
		item.setKey(prevkey);
	}

	public static void Setup(ItemModelDispatcher d) {
		Add(ModItems.HOOK,"climbing_hook", ModelTypes.TOOL);
		Add(ModItems.WRENCH,"wrench", ModelTypes.TOOL);
		Add(ModItems.META_WRENCH,"meta_wrench", ModelTypes.TOOL);
		Add(ModItems.SEASHELL,"seashell", ModelTypes.ITEM);
		d.addDispatch((new ItemModelSteelFishingRod(ModItems.TOOL_STEEL_FISHINGROD, "minecraft")).setFull3D().setRotateWhenRendering());
		Add(ModItems.PEDESTAL,"pedestal", ModelTypes.ITEM);
		Add(ModItems.AMMO_ARROW_TORCH,"ammo_arrow_torch", ModelTypes.ITEM);
		Add(ModItems.SACK,"sack", ModelTypes.SACK);
	}
}
