package quitebetter.client.blockmodel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelStandard;
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
public class BlockModelFan<T extends BlockLogic> extends BlockModelStandard<T> {
	public static final int MASK_DIRECTION = 0b00000111;

	private final @NotNull IconCoordinate topTexture;
	private final @NotNull IconCoordinate sideTexture;
	private final @NotNull IconCoordinate sideInvTexture;
	private final @NotNull IconCoordinate bottomTexture;

	public BlockModelFan(Block<T> block, Boolean isActive, Boolean isIn) {
		super(block);
		if (isActive) {
			this.topTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/active_top");
			this.sideTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/active_side"+(isIn ? "_in" : ""));
			this.sideInvTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/active_side_inverted"+(isIn ? "_in" : ""));
			this.bottomTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/bottom");
		} else {
			this.topTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/top");
			this.sideTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/side"+(isIn ? "_in" : ""));
			this.sideInvTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/side"+(isIn ? "_in" : ""));
			this.bottomTexture = TextureRegistry.getTexture(MOD_ID+":block/fan/bottom");
		}
	}

	public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
		Direction direction = this.getDirection(data);
		if (direction == Direction.NONE) {
			return data==255 ? this.topTexture : this.bottomTexture; //For funny building
		} else {
			IconCoordinate t;
			switch (direction) {
				case DOWN:
					switch (side.getDirection()) {case DOWN:t=this.bottomTexture; break;  case UP:t=this.topTexture; break; default:t=this.sideInvTexture; break; }
					return t;
				case UP:
					switch (side.getDirection()) {case DOWN:t=this.topTexture; break;  case UP:t=this.bottomTexture; break; default:t=this.sideTexture; break; }
					return t;
				case NORTH:
					switch (side.getDirection()) {case NORTH:t=this.topTexture; break;  case SOUTH:t=this.bottomTexture; break; case EAST:case DOWN:t=this.sideTexture; break; default:t=this.sideInvTexture; break; }
					return t;
				case EAST:
					switch (side.getDirection()) {case EAST:t=this.topTexture; break;  case WEST:t=this.bottomTexture; break; case DOWN:case NORTH:t=this.sideTexture; break; default:t=this.sideInvTexture; break; }
					return t;
				case SOUTH:
					switch (side.getDirection()) {case SOUTH:t=this.topTexture; break;  case NORTH:t=this.bottomTexture; break; case EAST:case DOWN:t=this.sideTexture; break; default:t=this.sideInvTexture; break; }
					return t;
				case WEST:
					switch (side.getDirection()) {case WEST:t=this.topTexture; break;  case EAST:t=this.bottomTexture; break; case DOWN:case NORTH:t=this.sideTexture; break; default:t=this.sideInvTexture; break; }
					return t;
				default: return this.bottomTexture;
			}
		}
	}

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Direction direction = this.getDirection(meta);
		switch (direction) {
			case DOWN:
				renderBlocks.uvRotateEast = 0;
				renderBlocks.uvRotateWest = 0;
				renderBlocks.uvRotateSouth = 0;
				renderBlocks.uvRotateNorth = 0;
				break;
			case UP:
				renderBlocks.uvRotateEast = 3;
				renderBlocks.uvRotateWest = 3;
				renderBlocks.uvRotateSouth = 3;
				renderBlocks.uvRotateNorth = 3;
				break;
			case NORTH:
				renderBlocks.uvRotateSouth = 1;
				renderBlocks.uvRotateNorth = 2;
				break;
			case SOUTH:
				renderBlocks.uvRotateSouth = 2;
				renderBlocks.uvRotateNorth = 1;
				renderBlocks.uvRotateTop = 3;
				renderBlocks.uvRotateBottom = 3;
				break;
			case WEST:
				renderBlocks.uvRotateEast = 1;
				renderBlocks.uvRotateWest = 2;
				renderBlocks.uvRotateTop = 2;
				renderBlocks.uvRotateBottom = 1;
				break;
			case EAST:
				renderBlocks.uvRotateEast = 2;
				renderBlocks.uvRotateWest = 1;
				renderBlocks.uvRotateTop = 1;
				renderBlocks.uvRotateBottom = 2;
				break;
			default:
				break;
		}
		this.renderStandardBlock(tessellator, this.block.getBounds(), x, y, z);
		this.resetRenderBlocks();
		return true;
	}

	public Direction getDirection(int data) {
		return Direction.getDirectionById(data & MASK_DIRECTION);
	}
}
