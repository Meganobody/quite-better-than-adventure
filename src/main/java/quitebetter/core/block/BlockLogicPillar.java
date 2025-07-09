package quitebetter.core.block;

import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;

public class BlockLogicPillar extends BlockLogicAxisAligned {

	public BlockLogicPillar(Block<?> block, Material material) {
		super(block, material);
	}

	public boolean isSolidRender() { return false; }
}
