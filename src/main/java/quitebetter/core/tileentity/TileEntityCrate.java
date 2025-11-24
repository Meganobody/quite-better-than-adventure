package quitebetter.core.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.render.block.model.BlockModelBasket;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.motion.CarriedBlock;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.net.packet.PacketBlockUpdate;
import net.minecraft.core.net.packet.PacketContainerSetSlot;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.BlockParticleHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.server.entity.player.PlayerServer;
import quitebetter.core.block.BlockLogicCrate;
import quitebetter.core.item.ModItems;
import quitebetter.core.util.BaseUtil;
import quitebetter.core.util.PlayerUtil;
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
import quitebetter.core.util.ServerUtil;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.*;

public class TileEntityCrate extends TileEntityGlued { //implements Container
	public final static int maxSlots = 48;
	public final static int maxCrates = 8;
	public Integer maxItemCount; //Max item amount in single instance
	public Integer itemCount = 0; //Current item amount in single instance
	public Integer itemID; //Item ID
	public Integer itemMeta; //Item Meta
	public Integer itemMaxStack; //Item stack max size
	public Integer upperHeight; //Y cord of upper crate
	public Integer lowerHeight; //Y cord of lower crate
	public boolean glued = false; //Is glued

	private boolean isCrateConnectable(int yo) {
		assert worldObj != null;
		return (
			worldObj.getBlock(x, yo, z) != null &&
				worldObj.getBlock(x, yo, z).id() == worldObj.getBlock(x, y, z).id() &&
				worldObj.getBlockMetadata(x, yo, z) == worldObj.getBlockMetadata(x, y, z) &&
				worldObj.getTileEntity(x, yo, z) != null &&
				!((TileEntityCrate) worldObj.getTileEntity(x, yo, z)).glued
		);
	}

	//DATA

	public boolean hasEqualDataTo(TileEntityCrate crate) {
		return this.hasItemData() &&
			this.itemID.equals(crate.itemID) &&
			this.itemMeta.equals(crate.itemMeta);
	}

	public boolean hasEqualItemDataTo(TileEntityCrate crate) {
		return this.hasItemData() &&
			this.itemID.equals(crate.itemID) &&
			this.itemMeta.equals(crate.itemMeta);
	}

	public boolean hasItemData() {
		return
			this.itemID != null &&
				this.itemMeta != null &&
				this.itemMaxStack != null;
	}

	public void passDataTo(TileEntityCrate passcrate) {
		passcrate.itemMeta = this.itemMeta;
		passcrate.itemID = this.itemID;
		passcrate.itemMaxStack = this.itemMaxStack;
		passcrate.maxItemCount = this.maxItemCount;
		passcrate.upperHeight = this.upperHeight;
		passcrate.lowerHeight = this.lowerHeight;
		this.sendPacketsForCrates();
	}

	//VISUAL

	public void emitParticles(ItemStack stack) {
		Random rand = new Random();
		if (stack != null) {
			if (stack.getItem() instanceof ItemBlock) {
				for (int j = 0; j < 5; ++j) {
					BaseUtil.spawnParticle(worldObj,
						"block",
						this.x + rand.nextFloat(),
						this.y + rand.nextFloat(),
						this.z + rand.nextFloat(),
						(double) (rand.nextInt(11) - 5) / 10,
						(double) (rand.nextInt(11) - 5) / 10,
						(double) (rand.nextInt(11) - 5) / 10,
						BlockParticleHelper.encodeBlockData(stack.itemID, stack.getMetadata(), Side.NORTH));
				}
			} else {
				for (int j = 0; j < 5; ++j) {
					BaseUtil.spawnParticle(worldObj,
						"item",
						this.x + rand.nextFloat(),
						this.y + rand.nextFloat(),
						this.z + rand.nextFloat(),
						(double) (rand.nextInt(11) - 5) / 10,
						(double) (rand.nextInt(11) - 5) / 10,
						(double) (rand.nextInt(11) - 5) / 10,
						stack.itemID);
				}
			}
		} else {
			for (int j = 0; j < 3; ++j) {
				BaseUtil.spawnParticle(worldObj, "smoke", this.x + rand.nextFloat(), this.y + rand.nextFloat(), this.z + rand.nextFloat(), (double) (rand.nextInt(11) - 5) / 100, (double) (rand.nextInt(11) - 5) / 100, (double) (rand.nextInt(11) - 5) / 100, 0);
			}
		}
	}

	//AURAL

	public void emitSound(boolean success) {
		assert worldObj != null;
		if (success) {
			worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.equip", 0.5F, 2F);
			worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "step.wood", 0.5F, 0.5F);
		} else {
			worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "step.wood", 0.5F, 2F);
		}
	}

	//STORAGE DATA

	public int getFreeSpace() {
		return getFreeSpace(null);
	}

	public int getFreeSpace(@Nullable Integer reqAmount) {
		if (reqAmount == null) {
			return this.maxItemCount - this.itemCount;
		} else {
			int a = this.maxItemCount - this.itemCount - reqAmount;
			return (a > 0 ? 0 : -a);
		}
	}

	public int getOverallFreeSpace() {
		assert worldObj != null;
		int out = 0;
		this.checkStorageHeights();
		for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
			if (worldObj.getBlock(x, yi, z) != null && isCrateConnectable(yi)) {
				TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
				if (tile.hasItemData()) {
					out += tile.maxItemCount;
					out -= tile.itemCount;
				}
			}
		}
		return out;
	}

	public int getOverallMaxSpace() {
		assert worldObj != null;
		int out = 0;
		this.checkStorageHeights();
		for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
			if (worldObj.getBlock(x, yi, z) != null && isCrateConnectable(yi)) {
				TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
				if (tile.maxItemCount != null) {
					out += tile.maxItemCount;
				}
			}
		}
		return out;
	}

	public int getOverallFullness() {
		assert worldObj != null;
		int out = 0;
		this.checkStorageHeights();
		for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
			if (worldObj.getBlock(x, yi, z) != null && isCrateConnectable(yi)) {
				TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
				if (tile.itemCount != null) {
					out += tile.itemCount;
				}
			}
		}
		return out;
	}

	public Integer getCrateCount() {
		this.checkStorageHeights();
		return this.upperHeight - this.lowerHeight + 1;
	}

	public TileEntityCrate getHighestCrate() {
		assert worldObj != null;
		return (TileEntityCrate) worldObj.getTileEntity(x,upperHeight,z);
	}

	//HEIGHTS

	public void checkStorageHeights() {
		if (this.upperHeight == null) {
			this.upperHeight = this.y;
		}
		if (this.lowerHeight == null) {
			this.lowerHeight = this.y;
		}
	}

	public void updateStorageHeights() {
		assert worldObj != null;
		checkStorageHeights();
		int yi;
		boolean success;
		//ADDING
		yi = this.y;
		this.upperHeight = yi;
		do {
			success = false;
			if (isCrateConnectable(yi)) {
				TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
				if (!tile.hasItemData() || tile.hasEqualDataTo(this)) {
					if (this.upperHeight - this.lowerHeight + 1 < maxCrates) {
						success = true;
					}
				}
			}
			if (success) {
				this.upperHeight = yi;
			}
			yi += 1;
		} while (success);
		yi = this.y;
		this.lowerHeight = yi;
		do {
			success = false;
			if (isCrateConnectable(yi)) {
				TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
				if (!tile.hasItemData() || tile.hasEqualDataTo(this)) {
					if (this.upperHeight - this.lowerHeight + 1 < maxCrates) {
						success = true;
					}
				}
			}
			if (success) {
				this.lowerHeight = yi;
			}
			yi -= 1;
		} while (success);
		this.notifyCrates();
	}

	public void notifyCrates() {
		for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
			if (worldObj != null && isCrateConnectable(yi)) {
				TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
				if (tile.hasEqualItemDataTo(this) || !tile.hasItemData()) {
					this.passDataTo(tile);
				}
			}
		}
		sendPacketsForCrates();
	}

	//BREAK RESULT

	public ArrayList<ItemStack> getBreakResult() {
		ArrayList<ItemStack> result = new ArrayList<>();
		if (itemID == null) {
			return result;
		}
		int maxStackSize = (new ItemStack(itemID, 1, itemMeta)).getMaxStackSize();
		int curItemCount = itemCount;
		for (int i = 1; Math.round((float) itemCount / maxStackSize) >= i; i++) {
			result.add(new ItemStack(itemID, (Math.min(curItemCount, maxStackSize)), itemMeta));
			curItemCount -= (Math.min(curItemCount, maxStackSize));
		}
		return result;
	}

	//CARRYING

	public boolean canBeCarried(World world, Entity potentialHolder) {
		return true;
	}

	public boolean tryPlace(World world, Entity holder, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		CarriedBlock carriedBlock = this.carriedBlock;
		this.x = blockX + side.getOffsetX();
		this.y = blockY + side.getOffsetY();
		this.z = blockZ + side.getOffsetZ();
		Block<?> currentBlock = world.getBlock(this.x, this.y, this.z);
		if (currentBlock != null && !currentBlock.hasTag(BlockTags.PLACE_OVERWRITES)) {
			return false;
		} else {
			assert carriedBlock != null;
			world.setBlockAndMetadata(this.x, this.y, this.z, carriedBlock.blockId, carriedBlock.metadata);
			this.upperHeight = null;
			this.lowerHeight = null;
			this.worldObj = world;
			this.validate();
			world.removeBlockTileEntity(this.x, this.y, this.z);
			world.setTileEntity(this.x, this.y, this.z, this);
			Block<?> b = world.getBlock(this.x, this.y, this.z);
			if (b != null && holder instanceof Mob) {
				b.onBlockPlacedByMob(world, this.x, this.y, this.z, side, (Mob) holder, xPlaced, yPlaced);
			}

			world.notifyBlockChange(this.x, this.y, this.z, carriedBlock.blockId);
			if (carriedBlock.blockId != 0) {
				if (Blocks.getBlock(carriedBlock.blockId).isSignalSource()) {
					for (Side s : Side.sides) {
						world.notifyBlocksOfNeighborChange(this.x + s.getOffsetX(), this.y + s.getOffsetY(), this.z + s.getOffsetZ(), this.getBlockId());
					}
				}
			}
			sendTilePacket(blockX, blockY, blockZ);

			return true;
		}
	}

	//INTERACTION

	public static boolean isStackPushable(ItemStack stack) {
		return stack == null || (stack.getItem().getItemStackLimit(stack) > 1 && canUseToTake(stack));
	}

	public static boolean canUseToTake(ItemStack stack) {
		Integer[] itemids = {Items.BASKET.id, ModItems.PEDESTAL.id, Items.PAINTBRUSH.id};
		return stack == null || !Arrays.asList(itemids).contains(stack.itemID) && (!(stack.getItem() instanceof ItemBlock) || !(BlockLogicCrate.isCrate(((ItemBlock) stack.getItem()).getBlock())));
	}

	public ItemStack pushStack(ItemStack stack) {
		if (stack != null && isStackPushable(stack) && (this.itemID == null || this.itemMeta == null)) {
			this.itemID = stack.itemID;
			this.itemMeta = stack.getMetadata();
			this.itemMaxStack = stack.getMaxStackSize();
			this.maxItemCount = maxSlots * this.itemMaxStack;
		}
		if (this.itemID == null || this.itemMeta == null) {
			return null;
		}
		if (stack != null && stack.itemID == this.itemID && stack.getMetadata() == this.itemMeta) {
			this.checkStorageHeights();
			boolean success = false;
			loop:
			for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
				if (worldObj != null && isCrateConnectable(yi)) {
					TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
					if (!tile.hasItemData() || tile.getFreeSpace() > 0) {
						tile.itemID = stack.itemID;
						tile.itemMeta = stack.getMetadata();
						tile.itemMaxStack = Item.getItem(tile.itemID).getDefaultStack().getMaxStackSize();
						tile.maxItemCount = maxSlots * tile.itemMaxStack;
						int countcorr = tile.getFreeSpace(stack.stackSize);
						tile.itemCount += stack.stackSize - countcorr;
						stack.stackSize = Math.max(countcorr, 0);
						success = true;
						sendTilePacket(x, yi, z);
						if (stack.stackSize > 0) {
							continue loop;
						}
					}
				}
			}
			if (success) {
				emitParticles(stack);
				emitSound(true);
			} else {
				emitParticles(null);
				emitSound(false);
			}
			sendPacketsForCrates();
			return stack;
		}
		return null;
	}

	public void pushItems(Player player) {
		ItemStack heldstack = player.getHeldItem();
		//INTERACTION
		if (this.glued) {
			return;
		}
		if (!this.hasItemData() && heldstack != null && heldstack.getItem().equals(Items.SLIMEBALL) && player.isSneaking()) {
			Random rand = new Random();
			for (int j = 0; j < 4; ++j) {
				BaseUtil.spawnParticle(worldObj, "item", x + rand.nextFloat(), y + rand.nextFloat(), z + rand.nextFloat(), (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10, Items.SLIMEBALL.id);
			}
			worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "mob.slimeattack", 0.25F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
			this.glued = true;
			sendTilePacket(x, y, z);
			return;
		}
		//INIT
		if (!player.isSneaking() && heldstack != null && isStackPushable(heldstack) && (this.itemID == null || this.itemMeta == null)) {
			this.itemID = heldstack.itemID;
			this.itemMeta = heldstack.getMetadata();
			this.itemMaxStack = heldstack.getMaxStackSize();
			this.maxItemCount = maxSlots * this.itemMaxStack;
		}
		if (this.itemID == null || this.itemMeta == null) {
			return;
		}
		player.swingItem();
		if (!player.isSneaking()) {
			//SINGLE STACK
			Integer index = PlayerUtil.findSlotInInventory(player, Item.getItem(this.itemID), this.itemMeta);
			if (index != null) {
				ItemStack stack = player.inventory.getItem(index);
				pushStack(player.getGamemode().getId() == 1 ? stack.copy() : stack);
				if (player.getGamemode().getId() != 1) {
					PlayerUtil.inventorySetSlot(player, index, (ItemStack) (
						stack.stackSize > 0 ? stack : null
					));
				}
			}
		} else {
			Integer index;
			ItemStack stack;
			index = PlayerUtil.findSlotInInventory(player, Item.getItem(this.itemID), this.itemMeta);
			if (index != null) {
				stack = player.inventory.getItem(index);
				boolean success;
				this.checkStorageHeights();
				do {
					success = false;
					loop:
					for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
						if (worldObj != null && isCrateConnectable(yi)) {
							TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
							if (!tile.hasItemData() || tile.getFreeSpace() > 0) {
								tile.itemID = stack.itemID;
								tile.itemMeta = stack.getMetadata();
								tile.itemMaxStack = Item.getItem(tile.itemID).getDefaultStack().getMaxStackSize();
								tile.maxItemCount = maxSlots * tile.itemMaxStack;
								int countcorr = tile.getFreeSpace(stack.stackSize);
								tile.itemCount += stack.stackSize - countcorr;
								stack.stackSize = Math.max(countcorr, 0);
								if (stack.stackSize > 0) {
									continue loop;
								}
								sendTilePacket(x, yi, z);
								tile.emitParticles(stack);
								success = true;
							}
						}
					}
					PlayerUtil.inventorySetSlot(player, index, (ItemStack) (
						stack.stackSize > 0 ? stack : null
					));
					index = PlayerUtil.findSlotInInventory(player, Item.getItem(this.itemID), this.itemMeta);
					if (index != null) {
						stack = player.inventory.getItem(index);
					} else {
						stack = null;
					}
				} while (stack != null && success);
				sendPacketsForCrates();
				emitSound(true);
				player.inventory.setChanged();
				return;
			}
		}
		player.inventory.setChanged();
		sendPacketsForCrates();
		emitSound(false);
	}

	public boolean canTakeStack(ItemStack stack) {
		return this.itemID == null || this.getFreeSpace()>0 && isStackPushable(stack) && stack != null && stack.itemID == this.itemID && stack.getMetadata() == this.itemMeta;
	}
	//public ItemStack takeStack(int amount)
	//{
	//	this.checkStorageHeights();
	//	if (this.glued) {
	//		return null;
	//	}
	//	for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
	//		if (isCrateConnectable(yi) && worldObj.getTileEntity(x, yi, z) instanceof TileEntityCrate && (((TileEntityCrate) worldObj.getTileEntity(x, yi, z)).itemCount > 0)) {
	//			TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
	//			int maxStackSize = (new ItemStack(tile.itemID, 1, tile.itemMeta)).getMaxStackSize();
	//			int amount = Math.min(tile.itemCount, maxStackSize);
	//			tile.itemCount -= amount;
	//			ItemStack stack = new ItemStack(tile.itemID, amount, tile.itemMeta);
	//			emitParticles(stack);
	//			sendTilePacket(x, yi, z);
	//			emitSound(true);
	//			takeOutOfBasket();
	//			return stack;
	//		}
	//	}
	//	sendPacketsForCrates();
	//	emitSound(false);
	//	return null;
	//}

	public ItemStack takeMaxStack() {
		this.checkStorageHeights();
		if (this.glued) {
			return null;
		}
		for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
			if (isCrateConnectable(yi) && worldObj.getTileEntity(x, yi, z) instanceof TileEntityCrate && (((TileEntityCrate) worldObj.getTileEntity(x, yi, z)).itemCount > 0)) {
				TileEntityCrate tile = (TileEntityCrate) worldObj.getTileEntity(x, yi, z);
				int maxStackSize = (new ItemStack(tile.itemID, 1, tile.itemMeta)).getMaxStackSize();
				int amount = Math.min(tile.itemCount, maxStackSize);
				tile.itemCount -= amount;
				ItemStack stack = new ItemStack(tile.itemID, amount, tile.itemMeta);
				emitParticles(stack);
				sendTilePacket(x, yi, z);
				emitSound(true);
				takeOutOfBasket();
				return stack;
			}
		}
		sendPacketsForCrates();
		emitSound(false);
		return null;
	}

	//NBT

	@Override
	public void readFromNBT(CompoundTag compoundTag) {
		super.readFromNBT(compoundTag);
		if (Item.getItem(compoundTag.getInteger("ItemId")) != null) {
			this.itemCount = compoundTag.getInteger("ItemCount");
			this.itemID = compoundTag.getInteger("ItemId");
			this.itemMeta = compoundTag.getInteger("ItemMeta");
			this.itemMaxStack = Item.getItem(this.itemID).getDefaultStack().getMaxStackSize();
			this.maxItemCount = maxSlots * this.itemMaxStack;
			this.upperHeight = compoundTag.getInteger("UpperHeight");
			this.lowerHeight = compoundTag.getInteger("LowerHeight");
			this.glued = compoundTag.getBoolean("Glued");
		}
	}

	@Override
	public void writeToNBT(CompoundTag compoundTag) {
		super.writeToNBT(compoundTag);
		if (this.itemCount != null) {
			compoundTag.putInt("ItemCount", this.itemCount);
		}
		if (this.itemID != null) {
			compoundTag.putInt("ItemId", this.itemID);
		}
		if (this.itemMeta != null) {
			compoundTag.putInt("ItemMeta", this.itemMeta);
		}
		if (this.upperHeight != null) {
			compoundTag.putInt("UpperHeight", this.upperHeight);
		}
		if (this.lowerHeight != null) {
			compoundTag.putInt("LowerHeight", this.lowerHeight);
		}
		compoundTag.putBoolean("Glued", this.glued);
	}

	//BASKET INTERACTION

	public void takeOutOfBasket() {
		TileEntityCrate tile = this.getHighestCrate();
		if (tile.itemID!=null && tile.getFreeSpace()<=0) {
			return;
		}
		if (this.worldObj.getTileEntity(tile.x, tile.y + 1, tile.z) != null && this.worldObj.getTileEntity(x, y + 1, z) instanceof TileEntityBasket) {
			List<TileEntityBasket.BasketEntry> toRemove = new ArrayList();
			TileEntityBasket basket = (TileEntityBasket) this.worldObj.getTileEntity(tile.x, tile.y + 1, tile.z);
			if (basket.contents != null) {
				for (Map.Entry<TileEntityBasket.BasketEntry, Integer> entry : basket.contents.entrySet()) {
					if (entry != null) {
						TileEntityBasket.BasketEntry basketEntry = (TileEntityBasket.BasketEntry) entry.getKey();
						ItemStack stack = new ItemStack(basketEntry.id, (Integer) entry.getValue(), basketEntry.metadata, basketEntry.tag);
						//PUSHING STACK
						ItemStack stackcorr = tile.pushStack(stack);
						if (stackcorr!=null) { //SUCCESS
							if (stackcorr.stackSize>0) {
								basket.numUnitsInside -= entry.getValue();
								basket.numUnitsInside += stackcorr.stackSize;
								entry.setValue(stackcorr.stackSize);
							} else {
								toRemove.add(basketEntry);
								basket.numUnitsInside -= entry.getValue();
							}
							tile.updateStorageHeights();
						}
					}
				}
				//REMOVAL
				for (TileEntityBasket.BasketEntry entry : toRemove) {
					basket.contents.remove(entry);
				}
				tile.sendTilePacket(tile.x, tile.y + 1, tile.z);
				tile.sendPacketsForCrates();
			}
		}
	}

	//GLUABLE

	@Override
	public void applyGlue() {
		Random rand = new Random();
		assert worldObj != null;
		if (!this.glued) {
			this.glued = true;

			for (int j = 0; j < 4; ++j) {
				worldObj.spawnParticle("item", x + rand.nextFloat(), y + rand.nextFloat(), z + rand.nextFloat(), (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10, (double) (rand.nextInt(11) - 5) / 10, Items.SLIMEBALL.id);
			}
			worldObj.playSoundEffect((Entity) null, SoundCategory.WORLD_SOUNDS, x, y, z, "mob.slimeattack", 0.25F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
		}
	}

	//SERVER-SIDE

	public void sendTilePacket(int x, int y, int z) {
		assert worldObj != null;
		if (EnvironmentHelper.isServerEnvironment() && worldObj.getTileEntity(x, y, z) != null) {
			ServerUtil.sendToWorldPlayers(worldObj, new PacketTileEntityData(worldObj.getTileEntity(x, y, z)));
			ServerUtil.sendToWorldPlayers(worldObj, new PacketBlockUpdate(x, y, z, worldObj));
		}
	}

	public void sendPacketsForCrates() {
		if (EnvironmentHelper.isServerEnvironment() && this.upperHeight!=null && this.lowerHeight!=null) {
			assert worldObj != null;
			for (int yi = this.upperHeight; yi >= this.lowerHeight; yi--) {
				sendTilePacket(x, yi, z);
			}
		}
	}

	//@Override
	//public int getContainerSize() {
	//	return getOverallFullness();
	//}
//
	//@Override
	//public @Nullable ItemStack getItem(int arg0) {
	//	// TODO Auto-generated method stub
	//	throw new UnsupportedOperationException("Unimplemented method 'getItem'");
	//}
//
	//@Override
	//public int getMaxStackSize() {
	//	return getOverallMaxSpace();
	//}
//
	//@Override
	//public String getNameTranslationKey() {
	//	// TODO Auto-generated method stub
	//	throw new UnsupportedOperationException("Unimplemented method 'getNameTranslationKey'");
	//}
//
	//@Override
	//public @Nullable ItemStack removeItem(int arg0, int arg1) {
	//	// TODO Auto-generated method stub
	//	throw new UnsupportedOperationException("Unimplemented method 'removeItem'");
	//}
//
	//@Override
	//public void setItem(int arg0, @Nullable ItemStack arg1) {
	//	// TODO Auto-generated method stub
	//	throw new UnsupportedOperationException("Unimplemented method 'setItem'");
	//}
//
	//@Override
	//public void sortContainer() {
	//	// TODO Auto-generated method stub
	//	throw new UnsupportedOperationException("Unimplemented method 'sortContainer'");
	//}
//
	//@Override
	//public boolean stillValid(Player player) {
	//	// TODO Auto-generated method stub
	//	throw new UnsupportedOperationException("Unimplemented method 'stillValid'");
	//}
}
