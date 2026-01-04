package quitebetter.core.item;


import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemMetaWrench extends Item {


	public ItemMetaWrench(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

	public boolean onUseItemOnBlock(ItemStack itemstack, @Nullable Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		Block<?> block = world.getBlock(blockX, blockY, blockZ);
		if (block != null) {
			if (true) {
				player.swingItem();
				world.playBlockEvent(player, 2001, blockX, blockY, blockZ, world.getBlockId(blockX, blockY, blockZ));
				int oldmeta = world.getBlockMetadata(blockX, blockY, blockZ);
				int change = player.isSneaking() ? -1 : 1;
				world.setBlockMetadataWithNotify(blockX, blockY, blockZ, oldmeta + change);
				player.sendMessage(I18n.getInstance().translateKey("item.quitebetter.meta.wrench.info").replace("*", ""+oldmeta) + " "+world.getBlockMetadata(blockX,blockY,blockZ));
				return true;
			}
		}
		return false;
	}
}
