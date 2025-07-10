package quitebetter.core.achivement;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import quitebetter.core.item.ModItems;
import net.minecraft.client.gui.achievements.data.AchievementPages;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.Achievements;
import net.minecraft.core.util.collection.NamespaceID;

import static quitebetter.core.ModCore.LOGGER;
import static quitebetter.core.ModCore.MOD_ID;

public class ModAchievements {
	public static Achievement LAVA_FISHING;
	public static void Setup() {
		LOGGER.info("Initializing achievements..");

		LAVA_FISHING = (new Achievement(NamespaceID.getPermanent(MOD_ID, "lava_fishing"), "lavaFishing", ModItems.TOOL_STEEL_FISHINGROD, Achievements.ENTER_NETHER)).setType(Achievement.TYPE_NORMAL).registerAchievement();

		if (!FabricLoaderImpl.INSTANCE.isModLoaded("stardew")) {
			AchievementPages.netherPage.addAchievement(LAVA_FISHING,-1,0);
		}
	}
}
