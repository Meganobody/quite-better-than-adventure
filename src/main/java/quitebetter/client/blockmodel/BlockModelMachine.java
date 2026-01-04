package quitebetter.client.blockmodel;

import net.minecraft.client.render.block.model.BlockModelFullyRotatable;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import quitebetter.core.block.BlockLogicMachine;

public class BlockModelMachine<T extends BlockLogic> extends BlockModelFullyRotatable<T>  {
	public IconCoordinate topActive;
	public IconCoordinate sideActive;
	public IconCoordinate bottomActive;
	public BlockModelMachine(Block<T> block) {
		super(block);
	}
	public BlockModelMachine<T> setTopActive(IconCoordinate topActive)
	{
		this.topActive = topActive;
		return this;
	}
	public BlockModelMachine<T> setSideActive(IconCoordinate sideActive)
	{
		this.sideActive = sideActive;
		return this;
	}
	public BlockModelMachine<T> setBottomActive(IconCoordinate bottomActive)
	{
		this.bottomActive = bottomActive;
		return this;
	}
	@Override
	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		Direction dir = BlockLogicFullyRotatable.metaToDirection(data);
		boolean active = (data & BlockLogicMachine.MASKFLAG_POWERED) != 0;
		if (side == dir.getSide()) {
			return active && (topActive != null)
				? topActive
				: this.blockTextures.get(Side.TOP);
		} else {
			return side.getDirection() != dir.getOpposite()
				? (active && (sideActive != null)
					? sideActive
					: this.blockTextures.get(Side.NORTH))
				: (active && (bottomActive != null)
					? bottomActive
					: this.blockTextures.get(Side.BOTTOM));
		}
	}
}
