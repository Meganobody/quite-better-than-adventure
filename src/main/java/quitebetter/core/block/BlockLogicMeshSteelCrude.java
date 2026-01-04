package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicMesh;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import quitebetter.core.tileentity.TileEntityMeshSteelCrude;

public class BlockLogicMeshSteelCrude extends BlockLogicMesh {
	public BlockLogicMeshSteelCrude(Block<?> block) {
		super(block);
		block.withEntity(TileEntityMeshSteelCrude::new);
	}
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		TileEntityMeshSteelCrude meshSteel = (TileEntityMeshSteelCrude)world.getTileEntity(x, y, z);
		ItemStack heldItem = player.getHeldItem();
		boolean flag = meshSteel.setFilter(player, heldItem);
		if (heldItem != null && heldItem.stackSize <= 0) {
			player.inventory.setItem(player.inventory.getCurrentItemIndex(), null);
		}

		return flag;
	}

	public boolean collidesWithEntity(Entity entity, World world, int x, int y, int z) {
		if (entity instanceof EntityItem) {
			EntityItem item = (EntityItem) entity;
			TileEntityMeshSteelCrude mesh = (TileEntityMeshSteelCrude)world.getTileEntity(x, y, z);
			return item.item.stackSize != mesh.stackSize;
		} else {
			return true;
		}
	}

}
