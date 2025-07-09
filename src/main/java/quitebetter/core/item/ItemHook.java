package quitebetter.core.item;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.block.Block;

public class ItemHook extends Item {


	public ItemHook(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

//	@Override
//	public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
//		if (!(entity instanceof Player)) return;
//		if (((Player) entity).getHeldItem() == null) return;
//		if (((Player) entity).getHeldItem().getItem() != this) return;
//		if (!(entity.vehicle instanceof MobPig)) return;
//		MobPig pig = (MobPig) entity.vehicle;
//		Vec3 looking = entity.getLookAngle();
//		int x = (int) (entity.x + looking.x * 5);
//		int y = (int) (entity.y + looking.y * 5);
//		int z = (int) (entity.z + looking.z * 5);
//		pig.setPathToEntity(world.getEntityPathToXYZ(pig, x, y, z, 20));
//		pig.yRot = entity.yRot;
//	}
//
//	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
//		//world.playSoundAtEntity((Entity)entityplayer, (Entity)entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
//		//entityplayer.swingItem();
//		return itemstack;
//	}

	public boolean onUseItemOnBlock(ItemStack itemstack, @Nullable Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		Block<?> block = world.getBlock(blockX, blockY, blockZ);
		//check if block can be climbed
		double distance = player.distanceTo(blockX+0.5, blockY+0.5, blockZ+0.5);
		if (block != null && block.isCubeShaped() && side != Side.BOTTOM && side != Side.TOP &&
			blockY < player.y && player.yd < 0.1
			&& distance <= 1.8
		) {
			if (block.id() == 110) { //WOOL
				player.swingItem();
				world.playSoundAtEntity((Entity)player, (Entity)player, "step.cloth", 0.1F, 2F / (itemRand.nextFloat() * 0.4F + 0.8F));
				return true;
			} else {
				world.playBlockEvent(player, 2001, blockX, blockY, blockZ, world.getBlockId(blockX, blockY, blockZ));
				player.yd = 0.5;
				player.fallDistance = 0;
				player.swingItem();
				itemstack.damageItem(1, (Entity)player);
				return true;
			}
		}
		return false;
	}
}
