package quitebetter.core.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;

public class BlockLogicMachine extends BlockLogicFullyRotatable {
	public static final int MASKFLAG_POWERED = 8;
	public BlockLogicMachine(Block<?> block) {
		super(block, Material.piston);
	}
	public int tickDelay() {
		return 4;
	}
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		boolean flag = world.hasNeighborSignal(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		if (flag && ((meta & MASKFLAG_POWERED) == 0)) {
			world.scheduleBlockUpdate(x, y, z, this.block.id(), this.tickDelay());
			world.setBlockMetadata(x, y, z, meta | MASKFLAG_POWERED);
		} else if (!flag) {
			world.setBlockMetadata(x, y, z, meta & ~MASKFLAG_POWERED);
		}
	}
}
