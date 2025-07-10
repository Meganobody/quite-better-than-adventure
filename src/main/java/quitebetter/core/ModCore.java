package quitebetter.core;

import quitebetter.core.achivement.ModAchievements;
import quitebetter.core.block.ModBlocks;
import quitebetter.core.crafting.ModCrafts;
import quitebetter.core.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class ModCore implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
	public static final String MOD_ID = "quitebetter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing "+MOD_ID);
	}

	@Override
	public void beforeGameStart() {
		ModConfig.Setup();
		ModBlocks.Setup();
		ModItems.Setup();
	}

	@Override
	public void afterGameStart() {
	}

	public static void beforeRecipesReady() {
		ModCrafts.SetupCorrections();
	}

	public static void onStatInitialize() {
		ModAchievements.Setup();
	}

	@Override
	public void onRecipesReady() {
		ModCrafts.SetupCrafts();
	}

	@Override
	public void initNamespaces() {

	}

}
