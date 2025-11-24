package quitebetter.core;

import net.minecraft.client.gui.ScreenCreateWorld;
import quitebetter.core.achivement.ModAchievements;
import quitebetter.core.block.ModBlocks;
import quitebetter.core.crafting.ModRecipes;
import quitebetter.core.feature.ModFeatures;
import quitebetter.core.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quitebetter.core.tool.ModMiningLevels;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class ModCore implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
	public static final String MOD_ID = "quitebetter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String VERSION = "1.0.8re1";
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing "+MOD_ID+" "+VERSION);
	}

	@Override
	public void beforeGameStart() {
		ModConfig.Setup();
		ModBlocks.Setup();
		ModItems.Setup();
		ModMiningLevels.Setup();
		ModFeatures.Setup();
	}

	@Override
	public void afterGameStart() {
	}

	public static void onStatInitialize() {
		ModAchievements.Setup();
	}

	@Override
	public void onRecipesReady() {
		ModRecipes.SetupCrafts();
		ModRecipes.SetupCorrections();
	}

	@Override
	public void initNamespaces() {
		ModRecipes.SetupNamespace();
	}

}
