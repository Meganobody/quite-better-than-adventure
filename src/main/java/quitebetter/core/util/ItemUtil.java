package quitebetter.core.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import java.util.Map;

public class ItemUtil {
	public static void fillItemStack(Container container, ItemStack filled, ItemStack with)
	{
		int remainder = filled.getMaxStackSize(container) - filled.stackSize;
		if (remainder <= 0) return;
		int added = Math.min(with.stackSize, remainder);
		filled.stackSize += added;
		with.stackSize -= added;
	}
	public static void putItemStack(World world, int x, int y, int z, ItemStack itemStack) {
		if (itemStack == null) return;
		Block<?> outBlock = world.getBlock(x, y, z);
		TileEntity outTileEntity = world.getTileEntity(x, y, z);
		if (outTileEntity instanceof TileEntityBasket)
		{
			TileEntityBasket basket = (TileEntityBasket) outTileEntity;
			basket.importItemStack(itemStack);
			basket.updateNumUnits();
			basket.setChanged();
			world.notifyBlockChange(x, y, z, outBlock.id());
		}
		else if (outTileEntity instanceof Container)
		{
			Container container = (Container) outTileEntity;
			for (int i = 0; i < container.getContainerSize(); i++){
				if (container.locked(i)) continue;
				ItemStack itemStackIter = container.getItem(i);
				if (itemStackIter != null) continue;
				container.setItem(i, itemStack);
				container.setChanged();
				world.notifyBlockChange(x, y, z, outBlock.id());
				return;
			}
			for (int i = 0; i < container.getContainerSize(); i++) {
				if (container.locked(i)) continue;
				ItemStack itemStackIter = container.getItem(i);
				if (!itemStackIter.canStackWith(itemStack)) continue; //can't be null because of previous check
				ItemUtil.fillItemStack(container, itemStackIter, itemStack);
				container.setChanged();
				world.notifyBlockChange(x, y, z, outBlock.id());
				if (itemStack.stackSize <= 0) return;
			}
		}
		world.dropItem(x, y, z, itemStack);
	}

	public static ItemStack takeItemStack(World world, int x, int y, int z) {
		Block<?> inBlock = world.getBlock(x, y, z);
		TileEntity inTileEntity = world.getTileEntity(x, y, z);
		ItemStack itemStack = null;
		if (inTileEntity instanceof TileEntityBasket)
		{
			TileEntityBasket basket = (TileEntityBasket) inTileEntity;
			if (basket.contents != null && basket.getNumUnitsInside() > 0)
			{
				Map.Entry<TileEntityBasket.BasketEntry,Integer> entry = basket.contents.entrySet().iterator().next();
				TileEntityBasket.BasketEntry be = entry.getKey();
				int numItems = entry.getValue();
				int maxStackSize = be.getItem().getItemStackLimit(null);
				int stackSize = maxStackSize;
				int remainingItems = numItems - maxStackSize;
				if (remainingItems < 0) {
					stackSize = numItems;
				}
				itemStack = new ItemStack(be.id, stackSize, be.metadata, be.tag);
				if (remainingItems <= 0)
				{
					basket.contents.remove(be);
				}
				else
				{
					basket.contents.put(be, remainingItems);
				}
				basket.updateNumUnits();
				basket.setChanged();
				world.notifyBlockChange(x, y, z, inBlock.id());
			}
		}
		else if (inTileEntity instanceof Container)
		{
			Container container = (Container) inTileEntity;
			for (int i = 0; i < container.getContainerSize(); i++){
				if (container.locked(i)) continue;
				ItemStack itemStackIter = container.getItem(i);
				if (itemStackIter == null) continue;
				container.setItem(i, null);
				container.setChanged();
				world.notifyBlockChange(x, y, z, inBlock.id());
				return itemStackIter;
			}
		}
		return itemStack;
	}
}
