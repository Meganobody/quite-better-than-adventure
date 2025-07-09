package quitebetter.client.itemmodel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static quitebetter.core.ModCore.MOD_ID;

@Environment(EnvType.CLIENT)
public class ItemModelSteelFishingRod extends ItemModelStandard {
	protected IconCoordinate rodCast = TextureRegistry.getTexture(MOD_ID+":item/tool_steel_fishingrod_cast");
	protected IconCoordinate rodDefault = TextureRegistry.getTexture(MOD_ID+":item/tool_steel_fishingrod");

	public ItemModelSteelFishingRod(Item item, String namespace) {
		super(item, namespace);
	}

	public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
		return entity instanceof Player && itemStack == ((Player)entity).getHeldItem() && ((Player)entity).bobberEntity != null ? this.rodCast : this.rodDefault;
	}
}
