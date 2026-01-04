package quitebetter.core.tileentity;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.ItemStack;
import quitebetter.core.gui.ShadowMenuAssembler;

import javax.annotation.Nullable;
import java.util.ArrayList;

import static net.minecraft.core.data.registry.Registries.RECIPES;

public class TileEntityAssembler extends TileEntityDispenser {
	public int lockedSlotBitSet;
	public ShadowMenuAssembler shadowMenuAssembler;

	public TileEntityAssembler()
	{
		super();
		shadowMenuAssembler = new ShadowMenuAssembler();
	}
	public boolean locked(int index)
	{
		return (lockedSlotBitSet & (1 << index)) > 0;
	}
	public void lockSlot(int index, boolean value)
	{
		Log.debug(LogCategory.LOG, String.format("lock slot, action %d %b", index, value));
		if (value)
		{
			lockedSlotBitSet |= 1 << index;
		}
		else
		{
			lockedSlotBitSet &= ~(1 << index);
		}
	}
	public @Nullable ArrayList<ItemStack> craft()
	{
		for(int i = 0; i < this.dispenserContents.length; ++i) {
			shadowMenuAssembler.containerCrafting.setItem(i, removeItem(i, 1));
		}
		ArrayList<ItemStack> stacks = new ArrayList<>();
		ItemStack firstStack = RECIPES.findMatchingRecipe(shadowMenuAssembler.containerCrafting);
		if (firstStack == null) {
			for(int i = 0; i < this.dispenserContents.length; ++i) {
				if (shadowMenuAssembler.containerCrafting.getItem(i) == null) continue;
				stacks.add(shadowMenuAssembler.containerCrafting.removeItem(i, 1));
			}
		}
		else
		{
			stacks.add(firstStack);
			for (ItemStack itemStack : Registries.RECIPES.onCraftResult(shadowMenuAssembler.containerCrafting)) {
				if (itemStack == null) continue;
				stacks.add(itemStack);
			}
		}
		return stacks;
	}
}
