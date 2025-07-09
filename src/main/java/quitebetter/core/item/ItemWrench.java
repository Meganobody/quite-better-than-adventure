package quitebetter.core.item;


import net.minecraft.core.block.*;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemWrench extends Item {


	public ItemWrench(String name, String namespaceId, int id) {
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
		if (block != null) {

		}
		return false;
	}
}
