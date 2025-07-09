package quitebetter.client.blockmodel;

import quitebetter.core.block.BlockLogicConveyor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelRotatable;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;

import static quitebetter.core.ModCore.MOD_ID;

@Environment(EnvType.CLIENT)
public class BlockModelConveyor<T extends BlockLogic> extends BlockModelRotatable<T> {
	private final @NotNull IconCoordinate topTexture;
	private final @NotNull IconCoordinate sideTexture;
	private final @NotNull IconCoordinate frontTexture;
	private final @NotNull IconCoordinate bottomTexture;

	public BlockModelConveyor(Block<T> block, Boolean isActive) {
		super(block);
		if (isActive) {
			this.topTexture = TextureRegistry.getTexture(MOD_ID+":block/conveyor/active_top");
		} else {
			this.topTexture = TextureRegistry.getTexture(MOD_ID+":block/conveyor/top");
		}
		this.sideTexture = TextureRegistry.getTexture(MOD_ID+":block/conveyor/side");
		this.frontTexture = TextureRegistry.getTexture(MOD_ID+":block/conveyor/front");
		this.bottomTexture = TextureRegistry.getTexture(MOD_ID+":block/conveyor/bottom");
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		Direction direction = this.getDirection(data);
		if (direction == Direction.NONE) {
			return this.bottomTexture;
		} else if (side.getDirection() == Direction.DOWN ) {
			return this.bottomTexture;
		} else if (side.getDirection() == Direction.UP ) {
			return this.topTexture;
		} else {
			IconCoordinate t = this.bottomTexture;
			switch (direction) {
				case SOUTH:
				case NORTH:
					switch (side.getDirection()) {
						case NORTH:
						case SOUTH:
							t = this.frontTexture;
							break;
						case EAST:
						case WEST:
							t = this.sideTexture;
							break;
					}
					break;
				case EAST:
				case WEST:
					switch (side.getDirection()) {
						case NORTH:
						case SOUTH:
							t = this.sideTexture;
							break;
						case EAST:
						case WEST:
							t = this.frontTexture;
							break;
					}
					break;
			}
			return t;
		}
	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Direction direction = this.getDirection(meta);
		switch (direction) {
			case NORTH:
				break;
			case SOUTH:
				renderBlocks.uvRotateTop = 3;
				break;
			case WEST:
				renderBlocks.uvRotateTop = 2;
				break;
			case EAST:
				renderBlocks.uvRotateTop = 1;
				break;
			default:
				break;
		}
		this.renderStandardBlock(tessellator, this.block.getBounds(), x, y, z);
		this.resetRenderBlocks();
		return true;
	}

	public Direction getDirection(int data) {
		return Direction.getDirectionById(data & BlockLogicConveyor.MASK_DIRECTION);
	}
}
