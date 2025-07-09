package quitebetter.core.item;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.slot.Slot;

import static quitebetter.core.ModCore.LOGGER;

public class ItemSack extends Item {
	public ItemSack(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
		this.setMaxStackSize(1);
		this.setMaxDamage(63);
	}

	public ItemStack getItem(ItemStack sack, int slot) {
		CompoundTag tag = sack.getData().getCompound("sackitem"+slot);
		int id = tag.getShort("id");
		int size = tag.getByte("Count");
		int meta = tag.getShort("Damage");
		ItemStack stack = new ItemStack(id,size,meta,tag);
		stack.readFromNBT(tag);
		if (tag!=null && id>0) {
			return (stack);
		}
		return null;
	}

	public Boolean isEmpty(ItemStack sack) {
		return getItem(sack,0)==null;
	}

	public int getItemCount(ItemStack sack) {
		CompoundTag tag = sack.getData();
		return tag.getInteger("itemcount");
	}

	public ItemStack addItemStack(ItemStack sack, ItemStack addstack) {
		CompoundTag tag = sack.getData();
		Integer findslot = findItem(sack, addstack);
		if (findslot!=null) {
			ItemStack oldstack = getItem(sack,findslot);
			if (oldstack.getMaxStackSize()>=oldstack.stackSize+addstack.stackSize) {
				oldstack.stackSize = oldstack.stackSize + addstack.stackSize;
				//PRIORITIZATION
				for (int i=findslot;i<getItemCount(sack)-1;i++) {
					tag.putCompound("sackitem" + i, getItem(sack,i+1).writeToNBT(new CompoundTag()));
				}
				tag.putCompound("sackitem"+(getItemCount(sack)-1), oldstack.writeToNBT(new CompoundTag()));
			} else {
				tag.putCompound("sackitem"+getItemCount(sack), addstack.writeToNBT(new CompoundTag()));
				tag.putInt("itemcount", getItemCount(sack)+1);
				sack.setData(tag);
			}
		} else {
			tag.putCompound("sackitem"+getItemCount(sack), addstack.writeToNBT(new CompoundTag()));
			tag.putInt("itemcount", getItemCount(sack)+1);
			sack.setData(tag);
		}
		return sack;
	}

	public Integer findItem(ItemStack sack, ItemStack fndstack) {
		for (int i=getItemCount(sack); i>=0; i--) {
			ItemStack stack = getItem(sack,i);
			if (stack!=null) { LOGGER.info(stack.getMetadata()+" "+fndstack.getMetadata()); }
			if (stack!=null && Item.getItem(stack.itemID)!=null && Item.getItem(stack.itemID).equals(Item.getItem(fndstack.itemID)) && stack.getMetadata()==fndstack.getMetadata()) {
				return i;
			}
		}
		return null;
	}

	public ItemStack getItemStack(ItemStack sack, Item fnditem) {
		for (int i=1; i<=getItemCount(sack); i++) {
			ItemStack stack = getItem(sack,i);
			if (stack!=null && Item.getItem(stack.itemID)!=null && Item.getItem(stack.itemID).equals(fnditem)) {
				return stack;
			}
		}
		return null;
	}

	public void removeLastStack(ItemStack sack) {
		CompoundTag tag = sack.getData();
		ItemStack stack = getItem(sack, getItemCount(sack)-1);
		if (stack!=null) {
			tag.putCompound("sackitem"+(getItemCount(sack)-1),new CompoundTag());
			tag.putInt("itemcount", getItemCount(sack)-1);
		}
	}

	@Override
	public boolean hasInventoryInteraction() {
		return true;
	}

	public Integer GetStackSizeOverflow(ItemStack sack, int space) {
		int dur = sack.getItemDamageForDisplay();
		if (dur+space>64) {
			return (dur+space)-64;
		}
		return 0;
	}

	public ItemStack ConsumeDurability(ItemStack sack, int space) {
		int dur = sack.getMetadata();
		if (dur+space>64) {
			sack.setMetadata(0);
			return sack;
		}
		sack.setMetadata(dur+space);
		return sack;
	}

	@Override
	public ItemStack onInventoryInteract(Player player, Slot slot, ItemStack stackInSlot, boolean isItemGrabbed) {
		ItemStack sack;
		if (isItemGrabbed) {
			sack = player.inventory.getHeldItemStack();
		} else {
			sack = stackInSlot;
		}
		if (stackInSlot!=null) {
			if (isItemGrabbed) {
				int sizecor = GetStackSizeOverflow(sack,stackInSlot.stackSize);
				if (sizecor<stackInSlot.stackSize) {
					stackInSlot.stackSize -= sizecor;
					sack = addItemStack(sack, stackInSlot);
					player.inventory.setHeldItemStack(ConsumeDurability(sack, stackInSlot.stackSize));
					if (sizecor>0) {
						stackInSlot.stackSize = sizecor;
					} else {
						stackInSlot = null;
					}
				}
			} else if (player.inventory.getHeldItemStack()!=null) {
				ItemStack held = player.inventory.getHeldItemStack();
				int sizecor = GetStackSizeOverflow(sack,held.stackSize);
				if (sizecor<held.stackSize) {
					held.stackSize -= sizecor;
					addItemStack(sack, held);
					GetStackSizeOverflow(sack, held.stackSize);
					sack = ConsumeDurability(sack, held.stackSize);
					if (sizecor>0) {
						held.stackSize = sizecor;
						player.inventory.setHeldItemStack(held);
					} else {
						player.inventory.setHeldItemStack(null);
					}
					stackInSlot = sack;
				}
			} else {
				ItemStack laststack = getItem(sack,getItemCount(sack)-1);
				if (laststack!=null) {
					removeLastStack(sack);
					sack = ConsumeDurability(sack, -laststack.stackSize);
					player.inventory.setHeldItemStack(laststack);
				}
			}
		} else {
			ItemStack laststack = getItem(sack,getItemCount(sack)-1);
			if (laststack!=null) {
				removeLastStack(sack);
				sack = ConsumeDurability(sack, -laststack.stackSize);
				stackInSlot = laststack;
			}
		}

		return stackInSlot;
	}

	@Override
	public String getTranslatedDescription(ItemStack itemstack) {
		String desc = I18n.getInstance().translateKey(itemstack.getItemKey() + ".desc" + (isEmpty(itemstack) ? ".empty" : ".filled"));
		if (itemstack.hasCustomName() && itemstack.getCustomName().toLowerCase().contains("mystery")) {
			desc = I18n.getInstance().translateKey(itemstack.getItemKey() + ".desc.mystery");
			return desc;
		}
		if (!isEmpty(itemstack)) {
			for (int i=getItemCount(itemstack); i>=0; i--) {
				ItemStack stack = getItem(itemstack,i);
				if (stack!=null) {
					String name = (stack.hasCustomName()?"§o"+stack.getCustomName():stack.getItem().getTranslatedName(stack))+(i<=0?"":",");
					desc += (" x" + stack.stackSize + " " + name).replaceAll(" ", i==getItemCount(itemstack)-1?" §4":" §8");
				}
			}
		}
		return desc;
	}
}
