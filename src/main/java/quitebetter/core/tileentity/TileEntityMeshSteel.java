package quitebetter.core.tileentity;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TileEntityMeshSteel extends TileEntityMeshSteelCrude {
	public @Nullable ItemStack filteredItems;
	public boolean setFilter(Player player, @Nullable ItemStack stack) {
		filteredItems = stack;
		return super.setFilter(player, stack);
	}

}
