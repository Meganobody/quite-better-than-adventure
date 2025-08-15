package quitebetter.core.util;

import net.minecraft.core.entity.EntityFallingBlock;

public interface IFalling {
	default boolean FellOnGround(EntityFallingBlock fallingBlock, int x, int y, int z) { return true; }
}
