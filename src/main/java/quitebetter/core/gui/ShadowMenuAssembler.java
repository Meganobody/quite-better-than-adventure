package quitebetter.core.gui;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerCrafting;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ShadowMenuAssembler extends MenuAbstract {
	public final ContainerCrafting containerCrafting;
	public ShadowMenuAssembler()
	{
		containerCrafting = new ContainerCrafting(this, 3, 3);
	}
	@Override
	public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player player) {
		return new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
	}

	@Override
	public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player player) {
		return new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
