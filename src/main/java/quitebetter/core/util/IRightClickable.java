package quitebetter.core.util;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public interface IRightClickable {

	default boolean preventsInteraction(World world, int x, int y, int z, Player player, Side side) { return !player.isSneaking(); }

	void onBlockRightClicked(World world, int x, int y, int z, Player player, Side side);
}
