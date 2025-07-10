package quitebetter.core;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import quitebetter.core.block.ModBlocks;
import quitebetter.core.item.ModItems;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static quitebetter.core.ModCore.LOGGER;
import static quitebetter.core.ModCore.MOD_ID;

public class ModConfig {
	public static final Toml properties = new Toml("Quite Better Configs.toml \n[!] Be careful with IDs. Changes can affect your existing worlds.");
	public static TomlConfigHandler cfg;

	public static int startingItemId = 20000;
	public static int startingBlockId = 4000;

	public static String BlockIDs = "Block IDs";
	public static String ItemIDs = "Item IDs";

	static void Setup() {
		LOGGER.info("Initializing config..");

		properties.addCategory("General")
			.addEntry("cfgVersion", 6);

		//BLOCK ID
		properties.addCategory(BlockIDs);
		properties.addEntry(BlockIDs+".startingFrom", startingBlockId);
		List<Field> blockFields = Arrays.stream(ModBlocks.class.getDeclaredFields()).filter((F)-> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field blockField : blockFields) {
			properties.addEntry(BlockIDs + "." + blockField.getName(), startingBlockId++);
		}
		//ITEM ID
		properties.addCategory(ItemIDs);
		properties.addEntry(ItemIDs+".startingFrom", startingItemId);
		List<Field> itemFields = Arrays.stream(ModItems.class.getDeclaredFields()).filter((F)-> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field itemField : itemFields) {
			properties.addEntry(ItemIDs+ "." + itemField.getName(), startingItemId++);
		}

		cfg = new TomlConfigHandler(MOD_ID, properties);

		if (cfg.getConfigFile().exists()) {
			cfg.loadConfig();
		} else {
			try {cfg.getConfigFile().createNewFile();} catch (IOException e) {throw new RuntimeException(e);}
			cfg.writeConfig();
		}
	}
}
