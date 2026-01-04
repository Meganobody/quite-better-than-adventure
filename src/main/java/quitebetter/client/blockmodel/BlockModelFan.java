package quitebetter.client.blockmodel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelFullyRotatable;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;

@Environment(EnvType.CLIENT)
public class BlockModelFan<T extends BlockLogic> extends BlockModelFullyRotatable<T> {
	public BlockModelFan(Block<T> block) {
		super(block);
	}
   @Override
   	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		Direction dir = BlockLogicFullyRotatable.metaToDirection(data);
		if (dir == Direction.NONE) {
			return this.blockTextures.get(Side.getSideById(data >> 3)); //For funny building
		}
		if (side != dir.getSide() && side != dir.getOpposite().getSide()) {
			switch (dir.getAxis()) {
				case X:
					if (side == Side.SOUTH || side == Side.TOP)
						return this.blockTextures.get(Side.SOUTH);
					break;
				case Z:
					if (side == Side.WEST || side == Side.TOP)
						return this.blockTextures.get(Side.SOUTH);
					break;
				default:
					break;
			}
		}
		return super.getBlockTextureFromSideAndMetadata(side, data);
    }
}
