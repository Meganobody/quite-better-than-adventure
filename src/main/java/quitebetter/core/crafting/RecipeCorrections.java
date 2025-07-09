package quitebetter.core.crafting;

import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeCorrections {
	//BLACKLIST
	private static List RecipeBlacklist = new ArrayList<String>();
	public static void addToBlacklist(String recipekey) { RecipeBlacklist.add(recipekey); }
	public static boolean isRecipeKeyBlacklisted(String recipekey) { return RecipeBlacklist.contains(recipekey); }

	//////TROMMEL
	//MODIFIER
	public static class TrommelEntry {public String recipekey; public WeightedRandomLootObject bag; public Integer weight; }
	private static ArrayList TrommelRecipeAdditions = new ArrayList<TrommelEntry>();
	public static void addToTrommelRecipe(String recipeKey, Item item, Integer yieldMin, Integer yieldMax, Integer weight) {
		TrommelEntry entry = new TrommelEntry();
		entry.recipekey = recipeKey;
		entry.bag = new WeightedRandomLootObject(new ItemStack(item), yieldMin, yieldMax);
		entry.weight = weight;
		TrommelRecipeAdditions.add(entry);
	}
	public static ArrayList<TrommelEntry> getAllTrommelRecipeCorrections(String recipeKey) {
		ArrayList out = new ArrayList<TrommelEntry>();
		if (TrommelRecipeAdditions.size()<=0) { return out; }
		//
		for(int i = 0; i < TrommelRecipeAdditions.size(); i++) {
			TrommelEntry entry = (TrommelEntry)TrommelRecipeAdditions.get(i);
			if (Objects.equals(entry.recipekey, recipeKey)) {
				out.add(entry);
			}
		}
		return out;
	}
}
