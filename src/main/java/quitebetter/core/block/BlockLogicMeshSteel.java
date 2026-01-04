package quitebetter.core.block;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.world.World;
import quitebetter.core.tileentity.TileEntityMeshSteelCrude;
import quitebetter.core.util.NBTUtil;

public class BlockLogicMeshSteel extends BlockLogicMeshSteelCrude {
	public BlockLogicMeshSteel(Block<?> block) {
		super(block);
	}
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (entity instanceof EntityItem) {
			TileEntityMeshSteelCrude mesh = (TileEntityMeshSteelCrude)world.getTileEntity(x, y, z);
			if (mesh.stackSize <= 0) return;
			EntityItem item = (EntityItem) entity;
			if (item.item.stackSize < mesh.stackSize) return;
			int remainder = item.item.stackSize - mesh.stackSize;
			if ((mesh.ticksRan % 20) != 0)
				return;
			if (remainder > 0)
			{
				Log.info(LogCategory.TEST, "Split moment");
				EntityItem remainedItem = item.dropItem(item.item.splitStack(remainder),0);
				NBTUtil.setSharedFlag(entity, 4, true);
				//remainedItem.xd = -item.xd;
				//remainedItem.yd = -item.yd;
				//remainedItem.zd = -item.zd;
				world.entityJoinedWorld(remainedItem);
			}
		}
	}
	public boolean collidesWithEntity(Entity entity, World world, int x, int y, int z) {
		if (entity instanceof EntityItem) {
			TileEntityMeshSteelCrude mesh = (TileEntityMeshSteelCrude)world.getTileEntity(x, y, z);
			if (mesh.stackSize <= 0) return false;
			EntityItem item = (EntityItem) entity;
			return item.item.stackSize < mesh.stackSize || NBTUtil.getSharedFlag(item, 4) || (mesh.ticksRan % 20) != 0;
		} else {
			return true;
		}
	}
}
