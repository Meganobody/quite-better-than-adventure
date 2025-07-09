package quitebetter.core.mixin;

import quitebetter.core.crafting.RecipeCorrections;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(
	value = {RecipeRegistry.class},
	remap = false
)
public class RecipeRegisterMixin {

	//RECIPE REGISTER CANCEL
	@Inject(method = "addCustomRecipe", at = @At("HEAD"), cancellable = true)
	private void RecipeDenierInject(String recipeKey, RecipeEntryBase<?, ?, ?> recipe, CallbackInfo ci) {
		//BLACKLIST
		if (RecipeCorrections.isRecipeKeyBlacklisted(recipeKey)) {
			ci.cancel();
		}
		//CORRECTIONS
		if (recipeKey.contains(":trommel")) {
			ArrayList corrs = RecipeCorrections.getAllTrommelRecipeCorrections(recipeKey);
			if (corrs.size()>0) {
				for(int i = 0; i < corrs.size(); i++) {
					RecipeCorrections.TrommelEntry entry = (RecipeCorrections.TrommelEntry) corrs.get(i);
					((WeightedRandomBag) recipe.getOutput()).addEntry(entry.bag,entry.weight);
				}
			}
		}
	}
}
