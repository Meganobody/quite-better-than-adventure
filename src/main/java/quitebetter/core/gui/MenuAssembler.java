package quitebetter.core.gui;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerCrafting;
import net.minecraft.core.player.inventory.container.ContainerResult;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuActivator;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.NotNull;
import quitebetter.core.tileentity.TileEntityAssembler;

import java.util.List;

public class MenuAssembler extends MenuAbstract {
	public static final int ID_LOCKED_SLOTS = 1;
	public TileEntityAssembler assembler;
	private final int assemblerSlotsStart;
	private final int inventorySlotsStart;
	private final int hotbarSlotsStart;
	private int lockedSlots;
	public MenuAssembler(Container container, TileEntityAssembler assembler)
	{
		this.assembler = assembler;

		for(int l = 0; l < 3; ++l) {
			for(int k1 = 0; k1 < 3; ++k1) {
				this.addSlot(new Slot(this.assembler, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
			}
		}

		for(int j = 0; j < 3; ++j) {
			for(int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(container, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(container, k, 8 + k * 18, 142));
			//this.addSlotListener();
		}
		this.assemblerSlotsStart = 0;
		this.inventorySlotsStart = 9;
		this.hotbarSlotsStart = 36;
		this.lockedSlots = 0;
	}
	public void broadcastChanges() {
		super.broadcastChanges();

		for(ContainerListener crafter : this.containerListeners) {
			if (this.lockedSlots != this.assembler.lockedSlotBitSet) {
				Log.debug(LogCategory.LOG, "broadcast locked slot alter");
				crafter.updateCraftingInventoryInfo(this, ID_LOCKED_SLOTS, this.assembler.lockedSlotBitSet);
			}
		}
		this.lockedSlots = this.assembler.lockedSlotBitSet;
	}

	public void setData(int id, int value) {
		this.assembler.lockedSlotBitSet = value;
	}
	@Override
	public ItemStack clicked(@NotNull InventoryAction action, int[] args, Player player) {
		Log.debug(LogCategory.LOG, String.format("clicked on assembler button, action %s", action.toString()));
		if (action == InventoryAction.LOCK) {
			this.assembler.lockSlot(args[0], !this.assembler.locked(args[0]));
			Log.debug(LogCategory.LOG, "clicked on assembler lock");
			return null;
		} else {
			return super.clicked(action, args, player);
		}
	}

	public boolean stillValid(Player entityplayer) {
		return this.assembler.stillValid(entityplayer);
	}

	public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
		if (slot.index >= this.assemblerSlotsStart && slot.index < this.inventorySlotsStart) {
			return this.getSlots(this.assemblerSlotsStart, 9, false);
		} else {
			if (action == InventoryAction.MOVE_ALL) {
				if (slot.index >= this.inventorySlotsStart && slot.index < this.hotbarSlotsStart) {
					return this.getSlots(this.inventorySlotsStart, 27, false);
				}

				if (slot.index >= this.hotbarSlotsStart) {
					return this.getSlots(this.hotbarSlotsStart, 9, false);
				}
			}

			return action == InventoryAction.MOVE_SIMILAR && slot.index >= this.inventorySlotsStart
				? this.getSlots(this.inventorySlotsStart, 36, false)
				: null;
		}
	}

	public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
		return slot.index >= this.assemblerSlotsStart && slot.index < this.inventorySlotsStart
			? this.getSlots(this.inventorySlotsStart, 36, false)
			: this.getSlots(this.assemblerSlotsStart, 9, false);
	}
}
