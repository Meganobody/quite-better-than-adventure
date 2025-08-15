package quitebetter.client;

import net.minecraft.client.Minecraft;
import quitebetter.client.blockmodel.BlockModels;
import quitebetter.client.itemmodel.ItemModels;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import quitebetter.core.util.PopUpUtil;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;

@Environment(EnvType.CLIENT)
public class ModClient implements ModInitializer, ModelEntrypoint, ClientStartEntrypoint {
	@Override
	public void onInitialize() {}

	@Override
	public void initBlockModels(BlockModelDispatcher d) { BlockModels.Setup(d); }

	@Override
	public void initItemModels(ItemModelDispatcher d) { ItemModels.Setup(d); }

	@Override
	public void initEntityModels(EntityRenderDispatcher d) {}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher d) {}

	@Override
	public void initBlockColors(BlockColorDispatcher d) {}

	@Override
	public void beforeClientStart() {}

	@Override
	public void afterClientStart() {

	}
}
