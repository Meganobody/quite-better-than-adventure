package quitebetter.core.item;

import quitebetter.core.projectile.ProjectileArrowTorch;
import net.minecraft.core.item.IDispensable;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

import java.util.Random;

public class ItemArrowTorch extends Item implements IDispensable {
	public ItemArrowTorch(String translationKey, String namespaceId, int id) {
		super(translationKey, namespaceId, id);
	}

	@Override
	public void onDispensed(ItemStack itemStack, World world, double x, double y, double z, int xd, int yd, int zd, Random random) {
		ProjectileArrowTorch arrow = new ProjectileArrowTorch(world, x, y, z);
		arrow.xd = xd;
		arrow.yd = yd;
		arrow.zd = zd;
		arrow.setDoesArrowBelongToPlayer(true);
		world.entityJoinedWorld(arrow);
	}
}
