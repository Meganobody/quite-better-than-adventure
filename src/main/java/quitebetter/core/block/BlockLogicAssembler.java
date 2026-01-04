package quitebetter.core.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.entity.projectile.ProjectileArrowGolden;
import net.minecraft.core.entity.projectile.ProjectileCannonball;
import net.minecraft.core.item.IDispensable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import quitebetter.core.gui.ScreenAssembler;
import quitebetter.core.tileentity.TileEntityAssembler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public class BlockLogicAssembler extends BlockLogicDispenser {
	public BlockLogicAssembler(Block<?> block) {
		super(block);
		block.withEntity(TileEntityAssembler::new);
	}
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		if (world.isClientSide) return true;
		TileEntityAssembler tileEntityAssembler = (TileEntityAssembler) world.getTileEntity(x, y, z);
		ScreenAssembler screen = new ScreenAssembler(player.inventory, tileEntityAssembler);
		Minecraft minecraft = Minecraft.getMinecraft();
		minecraft.displayScreen(screen);
		return true;
	}
	public void dispenseItem(World world, int x, int y, int z, Random random)
	{
		Direction direction = BlockLogicVeryRotatable.metaToDirection(world.getBlockMetadata(x, y, z));
		int xOffset = direction.getOffsetX();
		int yOffset = direction.getOffsetY();
		int zOffset = direction.getOffsetZ();
		TileEntityAssembler tileEntity = (TileEntityAssembler)world.getTileEntity(x, y, z);
		@Nullable ArrayList<ItemStack> stacks = tileEntity.craft();
		double px = x + xOffset * 0.6 + 0.5;
		double py = y + yOffset * 0.6 + 0.5;
		double pz = z + zOffset * 0.6 + 0.5;
		if (stacks.size() <= 0) {
			world.playBlockEvent(1001, x, y, z, 0);
		} else {
			for (ItemStack itemStack : stacks)
			{
				EntityItem item = new EntityItem(world, px, py - 0.3, pz, itemStack);
				double randOffset = random.nextDouble() * 0.1 + 0.2;
				item.xd = xOffset * randOffset;
				item.yd = yOffset + 0.2;
				item.zd = zOffset * randOffset;
				item.xd += random.nextGaussian() * 0.0075 * 6.0;
				item.yd += random.nextGaussian() * 0.0075 * 6.0;
				item.zd += random.nextGaussian() * 0.0075 * 6.0;
				world.entityJoinedWorld(item);
				world.playBlockEvent(1000, x, y, z, 0);
				world.playBlockEvent(2000, x, y, z, direction.getId());
			}
		}
	}
}
