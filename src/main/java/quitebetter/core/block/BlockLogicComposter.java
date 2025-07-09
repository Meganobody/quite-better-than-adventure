package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.Arrays;
import java.util.Random;

public class BlockLogicComposter extends BlockLogic {
	public BlockLogicComposter(Block<?> block, Material material) {
		super(block, material);
		block.setTicking(true);
	}

	public static Item[] Compostables = new Item[] {
		Items.WHEAT,
		Items.SEEDS_WHEAT
	};

	public static boolean isCompostable(Item item) {
		return Arrays.asList(Compostables).contains(Item.getItem(item.id));
	}

	public int tickDelay() {
		return 0;
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {

	}

	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		if (BlockLogicComposter.isCompostable( player.getHeldItem().getItem() )) {
			world.spawnParticle("smoke", x+0.5, y+0.5, z+0.5, 0.0D, 0.1D, 0.0D, 0);
			player.getHeldItem().consumeItem(player);
			return true;
		}
		return false;
	}
}
