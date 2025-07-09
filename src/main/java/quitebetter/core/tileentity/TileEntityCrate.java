package quitebetter.core.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import quitebetter.core.block.BlockLogicCrate;
import quitebetter.core.item.ModItems;
import quitebetter.core.util.PlayerUtil;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TileEntityCrate extends TileEntity {
	public int maxItemCount = 64*30;
	public Integer ItemCount = 0;
	public Integer ItemId;
	public Integer ItemMeta;
	private Integer upperEnd;
	private Integer lowerEnd;

	public boolean canBeCarried(World world, Entity potentialHolder) {
		return true;
	}

	private boolean isCrateConnectable(int yo) {
		return (
			worldObj.getBlock(x,yo,z)!=null &&
			worldObj.getBlock(x,yo,z).id()==worldObj.getBlock(x,y,z).id() &&
			worldObj.getBlockMetadata(x,yo,z)==worldObj.getBlockMetadata(x,y,z)
		);
	}

	public void updateEnds() {
		upperEnd=0;
		lowerEnd=0;
		int curcount=7;
		int i;
		boolean iscrate;
		//UPPER
		i = 1;
		iscrate = isCrateConnectable(y+i);
		while (iscrate && curcount>0) {
			TileEntityCrate tile = (TileEntityCrate)worldObj.getTileEntity(x,y+i,z);
			upperEnd += 1;
			curcount -= 1;
			i += 1;
			if (tile!=null) {
				if (tile.ItemId == null) {
					this.passData(tile);
				}
				if (!tile.hasEndData()) {
					tile.updateEnds();
				}
			}
			iscrate = isCrateConnectable(y+i);
		}
		//LOWER
		i = 1;
		iscrate = isCrateConnectable(y-i);
		while (iscrate && curcount>0) {
			TileEntityCrate tile = (TileEntityCrate)worldObj.getTileEntity(x,y-i,z);
			lowerEnd += 1;
			curcount -= 1;
			i += 1;
			if (tile!=null) {
				if (tile.ItemId == null) {
					this.passData(tile);
				}
				if (!tile.hasEndData()) {
					tile.updateEnds();
				}
			}
			iscrate = isCrateConnectable(y-i);
		}
	}

	public boolean hasEndData() {
		return this.upperEnd!=null && this.lowerEnd!=null;
	}

	public void passData(TileEntityCrate passcrate) {
		passcrate.ItemMeta = this.ItemMeta;
		passcrate.ItemId = this.ItemId;
	}

	public void emitParticles(ItemStack stack) {
		Random rand = new Random();
		if (stack!=null) {
			for (int j = 0; j < 12; ++j) {
				worldObj.spawnParticle(((stack.getItem() instanceof ItemBlock) ? "block" : "item"), this.x + rand.nextFloat(), this.y + rand.nextFloat(), this.z + rand.nextFloat(), (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10, stack.getItem().id);
			}
		} else {
			for (int j = 0; j < 12; ++j) {
				worldObj.spawnParticle("smoke", this.x + rand.nextFloat(), this.y + rand.nextFloat(), this.z + rand.nextFloat(), (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10,0);
			}
		}
	}

	public int getFreeSpace() { return getFreeSpace(null); }

	public int getFreeSpace(@Nullable Integer reqAmount) {
		if (reqAmount==null) { return maxItemCount-ItemCount; }
		else { int a=maxItemCount-ItemCount-reqAmount; return (a>0?0:-a); }
	}

	public int getOverallFreeSpace() {
		int out = 0;
		updateEnds();
		for (int i=upperEnd;i>=-lowerEnd;i--) {
			if (worldObj.getBlock(x,y+i,z)!=null && isCrateConnectable(y+i)) {
				TileEntityCrate tile = (TileEntityCrate)worldObj.getTileEntity(x,y+i,z);
				out += tile.maxItemCount;
				out -= tile.ItemCount;
			}
		}
		return out;
	}

	public ArrayList<ItemStack> getBreakResult() {
		ArrayList<ItemStack> result = new ArrayList<>();
		if (ItemId==null) {return result;}
		int maxStackSize = (new ItemStack(ItemId, 1, ItemMeta)).getMaxStackSize();
		int curItemCount = ItemCount;
		for (int i = 1; Math.round((float) ItemCount/maxStackSize)>=i; i++) {
			result.add(new ItemStack(ItemId, (Math.min(curItemCount, maxStackSize)), ItemMeta));
			curItemCount -= (Math.min(curItemCount, maxStackSize));
		}
		return result;
	}

	public static boolean isStackPushable(ItemStack stack) {
		return stack==null || (stack.getItem().getItemStackLimit(stack)>1 && canUseToTake(stack));
	}

	public static boolean canUseToTake(ItemStack stack) {
		Integer[] itemids = {Items.BASKET.id, ModItems.PEDESTAL.id, Items.PAINTBRUSH.id};
		return stack==null || !Arrays.asList(itemids).contains(stack.itemID) && (!(stack.getItem() instanceof ItemBlock) || !(BlockLogicCrate.isCrate(((ItemBlock) stack.getItem()).getBlock())));
	}

	public ItemStack pushStack(ItemStack stack) {
		updateEnds();
		if (stack!=null && isStackPushable(stack) && (this.ItemId==null || this.ItemMeta==null)) {
			this.ItemId = stack.itemID;
			this.ItemMeta = stack.getMetadata();
		}
		if (this.ItemId==null || this.ItemMeta==null) {
			return null;
		}
		if (stack != null && stack.itemID==this.ItemId && stack.getMetadata()==this.ItemMeta) {
			loop:
			for (int i = upperEnd; i >= -lowerEnd; i--) {
				if (worldObj != null && isCrateConnectable(y+i) && (((TileEntityCrate) worldObj.getTileEntity(x, y + i, z)).getFreeSpace() > 0)) {
					TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, y + i, z);
					int countcorr = tile.getFreeSpace(stack.stackSize);
					tile.ItemId = stack.itemID;
					tile.ItemMeta = stack.getMetadata();
					tile.ItemCount += stack.stackSize - countcorr;
					stack.stackSize = Math.max(countcorr, 0);
					if (stack.stackSize > 0) {
						continue loop;
					}
				}
			}
			emitParticles(stack);
			worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.equip", 0.5F, 2F);
			worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "step.wood", 0.5F, 0.5F);
			return stack;
		}
		return null;
	}

	public boolean pushItems(Player player) {
		updateEnds();
		ItemStack heldstack = player.getHeldItem();
		//INIT
		if (!player.isSneaking() && heldstack!=null && isStackPushable(heldstack) && (this.ItemId==null || this.ItemMeta==null)) {
			this.ItemId = heldstack.itemID;
			this.ItemMeta = heldstack.getMetadata();
		}
		if (this.ItemId==null || this.ItemMeta==null) {
			return false;
		}
		if (!player.isSneaking()) {
			//SINGLE STACK
			Integer index = PlayerUtil.findSlotInInventory(player, Item.getItem(this.ItemId), this.ItemMeta);
			if (index != null) {
				ItemStack stack = player.inventory.getItem(index);
				ItemStack corr = pushStack(stack);
				if (corr!=null) {
					player.inventory.setItem(index, (ItemStack) (
						stack.stackSize > 0 ? stack : null
					));
				}
			}
		} else {
			Integer index;
			ItemStack stack;
			index = PlayerUtil.findSlotInInventory(player, Item.getItem(this.ItemId), this.ItemMeta);
			if (index!=null) {
				stack = player.inventory.getItem(index);
				boolean success;
				do {
					success = false;
					loop:
					for (int i = upperEnd; i >= -lowerEnd; i--) {
						if (worldObj != null && isCrateConnectable(y+i) && (((TileEntityCrate) worldObj.getTileEntity(x, y + i, z)).getFreeSpace() > 0)) {
							TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, y + i, z);
							int countcorr = tile.getFreeSpace(stack.stackSize);
							tile.ItemId = stack.itemID;
							tile.ItemMeta = stack.getMetadata();
							tile.ItemCount += stack.stackSize - countcorr;
							stack.stackSize = Math.max(countcorr, 0);
							if (stack.stackSize > 0) {
								continue loop;
							}
							tile.emitParticles(stack);
							success = true;
						}
					}
					player.inventory.setItem(index, (ItemStack) (
						stack.stackSize > 0 ? stack : null
					));
					index = PlayerUtil.findSlotInInventory(player, Item.getItem(this.ItemId), this.ItemMeta);
					if (index != null) {
						stack = player.inventory.getItem(index);
					} else {
						stack = null;
					}
				} while (stack != null && success);
				worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.equip", 0.5F, 2F);
				worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "step.wood", 0.5F, 0.5F);
				return true;
			}
		}
		return false;
	}

	public ItemStack takeStack() {
		updateEnds();
		for (int i=upperEnd;i>=-lowerEnd;i--) {
			if (isCrateConnectable(y+i) && worldObj.getTileEntity(x,y+i,z) instanceof TileEntityCrate && (((TileEntityCrate)worldObj.getTileEntity(x,y+i,z)).ItemCount>0) ) {
				TileEntityCrate tile = (TileEntityCrate)worldObj.getTileEntity(x,y+i,z);
				int maxStackSize = (new ItemStack(tile.ItemId, 1, tile.ItemMeta)).getMaxStackSize();
				int amount = Math.min(tile.ItemCount, maxStackSize);
				tile.ItemCount -= amount;
				ItemStack stack = new ItemStack(tile.ItemId,amount,tile.ItemMeta);
				emitParticles(stack);
				worldObj.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z,"random.equip", 0.5F, 2F);
				worldObj.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z,"step.wood", 0.5F, 0.5F);
				return stack;
			}
		}
		worldObj.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z,"step.wood", 0.5F, 2F);
		return null;
	}

	public void readFromNBT(CompoundTag compoundTag) {
		super.readFromNBT(compoundTag);
		this.ItemCount = compoundTag.getInteger("ItemCount");
		this.ItemId = compoundTag.getInteger("ItemId");
		this.ItemMeta = compoundTag.getInteger("ItemMeta");
		this.ItemId = Item.getItem(this.ItemId)==null?null:this.ItemId;
	}

	public void writeToNBT(CompoundTag compoundTag) {
		super.writeToNBT(compoundTag);
		if (this.ItemCount != null) { compoundTag.putInt("ItemCount", this.ItemCount);}
		if (this.ItemId != null) { compoundTag.putInt("ItemId", this.ItemId);}
		if (this.ItemMeta != null) { compoundTag.putInt("ItemMeta", this.ItemMeta);}
	}

	public boolean canTakeStack(ItemStack stack) {
		return this.ItemId==null || isStackPushable(stack) && stack.itemID == this.ItemId && stack.getMetadata() == this.ItemMeta;
	}

	public void takeOutOfBasket() {
		TileEntityCrate tile = (TileEntityCrate) this.worldObj.getTileEntity(x,y,z);
		if (this.worldObj.getTileEntity(x,y+1,z)!=null && this.worldObj.getTileEntity(x,y+1,z) instanceof TileEntityBasket) {
			List<TileEntityBasket.BasketEntry> toRemove = new ArrayList();
			TileEntityBasket basket = (TileEntityBasket) this.worldObj.getTileEntity(x,y+1,z);
			if (basket.contents!=null) {
				for (Map.Entry<TileEntityBasket.BasketEntry, Integer> entry : basket.contents.entrySet()) {
					if (entry!=null) {
						TileEntityBasket.BasketEntry basketEntry = (TileEntityBasket.BasketEntry) entry.getKey();
						ItemStack stack = new ItemStack(basketEntry.id, (Integer) entry.getValue(), basketEntry.metadata, basketEntry.tag);
						if (pushStack(stack)!=null) {
							basket.numUnitsInside -= entry.getValue();
							toRemove.add(basketEntry);
						}
					}
				}
				//REMOVAL
				for(TileEntityBasket.BasketEntry entry : toRemove) {
					basket.contents.remove(entry);
				}
			}
		}
	}
}
