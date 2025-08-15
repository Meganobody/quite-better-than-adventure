package quitebetter.core.util;

import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.client.player.controller.PlayerControllerMP;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.packet.PacketContainerSetSlot;
import net.minecraft.core.net.packet.PacketEntityFling;
import net.minecraft.core.net.packet.PacketPlayerAction;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.server.entity.player.PlayerServer;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.EnvironmentHelper;
import static quitebetter.core.ModCore.LOGGER;

public class PlayerUtil {
	public static boolean hasItemInHotbar(Player player, Item item) {
		for (int i=0; i<8; i++) {
			if (player.inventory.getItem(i)!=null && player.inventory.getItem(i).getItem().equals(item)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasItemOutOfHotbar(Player player, Item item) {
		for (int i=8; i<36; i++) {
			if (player.inventory.getItem(i)!=null && player.inventory.getItem(i).getItem().equals(item)) {
				return true;
			}
		}
		return false;
	}

	public static ItemStack getQuiver(Player player) {
		ItemStack quiverSlot = player.inventory.armorItemInSlot(2);
		if (quiverSlot != null && quiverSlot.itemID == Items.ARMOR_QUIVER_GOLD.id && quiverSlot.getMetadata() < quiverSlot.getMaxDamage()) {
			return quiverSlot;
		} else if (quiverSlot != null && quiverSlot.itemID == Items.ARMOR_QUIVER.id && quiverSlot.getMetadata() < quiverSlot.getMaxDamage()) {
			return quiverSlot;
		}
		return null;
	}

	public static boolean consumeQuiverAmmo(Player player) {
		ItemStack quiver = getQuiver(player);
		if (quiver!=null) {
			if (quiver.getItem().equals(Items.ARMOR_QUIVER)) { quiver.damageItem(1, player); }
			return true;
		}
		return false;
	}

	public static void consumeBowAmmo(Player player) {
		Item item = player.getNextArrow();
		if (item!=null) {
			if (!consumeQuiverAmmo(player)) {
				player.inventory.consumeInventoryItem(item.id);
			}
		}
	}

	public static ItemStack findStackInInventory(Player player, Item item) {
		for (int i=0; i<36; i++) {
			if (player.inventory.getItem(i)!=null && player.inventory.getItem(i).getItem().equals(item)) {
				return player.inventory.getItem(i);
			}
		}
		return null;
	}

	public static Integer findSlotInInventory(Player player, Item item, int meta) {
		for (int i=0; i<36; i++) {
			ItemStack slot = player.inventory.getItem(i);
			if (slot!=null && slot.getItem().equals(item) && (!slot.getHasSubtypes() || slot.getMetadata() == meta)) {
				return i;
			}
		}
		return null;
	}

	public static boolean stackToInventory(Player player, int index, ItemStack itemstack) {
		ContainerInventory inv = player.inventory;
		ItemStack slot = inv.getItem(index);
		if (slot==null || (slot.itemID==itemstack.itemID && (!slot.getHasSubtypes() || slot.getMetadata() == itemstack.getMetadata()) && slot.getData() == itemstack.getData() && ( slot.isStackable() && slot.getMaxStackSize()-itemstack.stackSize-slot.stackSize>=0 )) ) {
			if (slot!=null) { itemstack.stackSize += slot.stackSize; }
			inv.setItem(index,itemstack);
			return true;
		}
		return false;
	}

	public static int storeItemStack(Player player, ItemStack itemstack) {
		ContainerInventory inv = player.inventory;
		for(int i = 0; i < inv.mainInventory.length; ++i) {
			if (stackToInventory(player,i,itemstack)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isArmorItemThis(Player player, int slot, Item item) {
		ItemStack stack = player.inventory.armorItemInSlot(slot);
		return stack!=null && stack.getItem().equals(item);
	}
}
