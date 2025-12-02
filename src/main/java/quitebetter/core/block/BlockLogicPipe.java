package quitebetter.core.block;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

public class BlockLogicPipe extends BlockLogicFullyRotatable {

	public BlockLogicPipe(Block<?> block) {
		super(block, Material.steel);
	}
	public AABB getCollisionBoundingBoxFromPool(WorldSource world, int x, int y, int z) {
		float f = 0.125F;
		Axis axis = metaToDirection(world.getBlockMetadata(x, y, z)).getAxis();
		switch (axis)
		{
			case X:
				return AABB.getTemporaryBB(x + f, y, z, (x + 1) - f, (y + 1), (z + 1));
			case Y:
				return AABB.getTemporaryBB(x, y + f, z, (x + 1), (y + 1) - f, (z + 1));
			case Z:
				return AABB.getTemporaryBB(x, y, z + f, (x + 1), (y + 1), (z + 1) - f);
			default:
				return super.getCollisionBoundingBoxFromPool(world, x, y, z);
		}
	}
	public static Direction getEntityDirection(Entity entity)
	{
		boolean xGreaterThanY = Math.abs(entity.xd) > Math.abs(entity.yd);
		boolean xGreaterThanZ = Math.abs(entity.xd) > Math.abs(entity.zd);
		boolean zGreaterThanY = Math.abs(entity.zd) > Math.abs(entity.yd);
		if (xGreaterThanY)
		{
			if (xGreaterThanZ)
				return entity.xd < 0 ? Direction.WEST : Direction.EAST; // x > y > z or x > z > y
			if (zGreaterThanY)
				return entity.zd < 0 ? Direction.NORTH : Direction.SOUTH; // z > x > y
			return Direction.NONE; //transitive property
		}
		if (zGreaterThanY)
			return entity.zd < 0 ? Direction.NORTH : Direction.SOUTH; // z > y > x
		return entity.yd < 0 ? Direction.DOWN : Direction.UP; // y > z > x or y > x > z
	}
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		Direction blockDirection = metaToDirection(world.getBlockMetadata(x, y, z));
		Direction entityDirection = getEntityDirection(entity);
		//if (blockDirection.getAxis() != entityDirection.getAxis())
		//	return;
		entity.moveTo(entity.x + entityDirection.getOffsetX(), entity.y + entityDirection.getOffsetY(), entity.z + entityDirection.getOffsetZ(), entity.yRot, entity.xRot);
		world.sendGlobalMessage(String.format("block %s | entity %s", blockDirection.toString(), entityDirection.toString()));
		//entity.push(entityDirection.getOffsetX(), entityDirection.getOffsetY(), entityDirection.getOffsetZ());
	}
}
