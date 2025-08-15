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

	public static final Toml properties = new Toml(
	"Quite Better Configs.toml"
			+"\n[!] Be careful with IDs. Changes can affect your existing worlds."
			+"\n[!] With updates config may need corrections if you did any changes before."
	);
	public static TomlConfigHandler cfg;

	public static int startingItemId = 20000;
	public static int startingBlockId = 4000;

	public static final String BlockIDs = "Block IDs";
	public static final String ItemIDs = "Item IDs";

	public static void Setup() {

		LOGGER.info("Initializing config..");
		//STRUCTURE
		properties.addCategory("General")
			.addEntry("version", 1);

		//BLOCK ID
		properties.addCategory(BlockIDs);
		properties.addEntry(BlockIDs+".startingFrom", startingBlockId);
		List<Field> blockFields = Arrays.stream(ModBlocks.class.getDeclaredFields()).filter((F)-> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field blockField : blockFields) {
			properties.addEntry(BlockIDs + "." + blockField.getName(), ModItems.id(blockField.getName())!=null ? ModBlocks.id(blockField.getName()) : startingBlockId++);
		}
		//ITEM ID
		properties.addCategory(ItemIDs);
		properties.addEntry(ItemIDs+".startingFrom", startingItemId);
		List<Field> itemFields = Arrays.stream(ModItems.class.getDeclaredFields()).filter((F)-> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field itemField : itemFields) {
			properties.addEntry(ItemIDs+ "." + itemField.getName(), ModItems.id(itemField.getName())!=null ? ModItems.id(itemField.getName()) : startingItemId++);
		}
		//CONFIG
		cfg = new TomlConfigHandler(MOD_ID, properties);

		if (cfg.getConfigFile().exists()) {
			cfg.loadConfig();
			cfg.setDefaults(cfg.getRawParsed());
			Toml raw = cfg.getRawParsed();
			boolean valid = true;
			//CATEGORIES
			//BLOCKS
			Toml BlockIDs = (Toml) raw.get("."+ModConfig.BlockIDs);
			if (BlockIDs!=null) {
				int newBlockIDs=ModConfig.startingBlockId+BlockIDs.getOrderedKeys().size();
				for (Field F : blockFields) {
					if (!raw.contains(ModConfig.BlockIDs+"."+F.getName())) {
						raw.addEntry(ModConfig.BlockIDs+"."+F.getName(), newBlockIDs++);
						valid = false;
					}
				}
			} else { raw.addCategory(ModConfig.BlockIDs); valid = false; }
			//ITEMS
			Toml ItemIDs = (Toml) raw.get("."+ModConfig.ItemIDs);
			if (ItemIDs!=null) {
				int newItemIDs=ModConfig.startingItemId+ItemIDs.getOrderedKeys().size();
				for (Field F : itemFields) {
					if (!raw.contains(ModConfig.ItemIDs+"."+F.getName())) {
						raw.addEntry(ModConfig.ItemIDs+"."+F.getName(), newItemIDs++);
						valid = false;
					}
				}
			} else { raw.addCategory(ModConfig.ItemIDs); valid = false; }
			//RESULT
			if (!valid) {
				cfg.getConfigFile().delete();
				cfg.setDefaults(raw);
				cfg.writeConfig();
				cfg.loadConfig();
			}
		} else {
			try {cfg.getConfigFile().createNewFile();} catch (IOException e) {throw new RuntimeException(e);}
			cfg.writeConfig();
		}
	}
}
