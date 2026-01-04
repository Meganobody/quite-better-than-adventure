package quitebetter.core.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import org.jetbrains.annotations.Nullable;

public class TileEntityFetcher extends TileEntity implements Container {
	public @Nullable ItemStack fetchedItemStack;
	@Override
	public int getContainerSize() { return 1; }
	@Override
	public @Nullable ItemStack getItem(int index) { return index == 0 ? fetchedItemStack : null; }
	public @Nullable ItemStack takeItem()
	{
		@Nullable ItemStack itemStack = fetchedItemStack;
		fetchedItemStack = null;
		setChanged();
		return itemStack;
	}
	@Override
	public @Nullable ItemStack removeItem(int index, int takeAmount) {
		if (index != 0 || fetchedItemStack == null) return null;
		if (fetchedItemStack.stackSize <= takeAmount) {
			ItemStack itemStack = fetchedItemStack;
			fetchedItemStack = null;
			setChanged();
			return itemStack;
		} else {
			ItemStack itemStack1 = fetchedItemStack.splitStack(takeAmount);
			if (fetchedItemStack.stackSize <= 0) {
				fetchedItemStack = null;
			}
			setChanged();
			return itemStack1;
		}
	}

	@Override
	public void setItem(int index, @Nullable ItemStack itemStack) {
		if (index != 0) return;
		fetchedItemStack = itemStack;
		setChanged();
	}

	@Override
	public String getNameTranslationKey() { return "container.fetcher.name"; }
	@Override
	public int getMaxStackSize()  {return 64; }
	@Override
	public boolean stillValid(Player player) {
		return this.worldObj != null
			&& this.worldObj.getTileEntity(this.x, this.y, this.z) == this
			&& player.distanceToSqr(this.x + 0.5, this.y + 0.5, this.z + 0.5) <= 64.0;
	}

	@Override
	public void sortContainer() { }
	public void readFromNBT(CompoundTag nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		fetchedItemStack = ItemStack.readItemStackFromNbt(nbttagcompound.getCompound("Item"));


	}

	public void writeToNBT(CompoundTag nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (fetchedItemStack == null) return;

		CompoundTag nbttagcompound1 = new CompoundTag();
		fetchedItemStack.writeToNBT(nbttagcompound1);

		nbttagcompound.put("Item", nbttagcompound1);
	}
}
