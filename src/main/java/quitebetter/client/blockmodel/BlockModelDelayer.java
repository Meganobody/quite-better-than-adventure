package quitebetter.client.blockmodel;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.model.*;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRepeater;
import net.minecraft.core.block.BlockLogicTorchRedstone;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import quitebetter.core.block.BlockLogicDelayer;

public class BlockModelDelayer extends BlockModelStandard<BlockLogicDelayer> {

	public BlockModelDelayer(Block<BlockLogicDelayer> block) {
		super(block);
	}
	private static final BlockModelTorchRedstone<BlockLogicTorchRedstone> modelTorchActive;
	private static final BlockModelTorchRedstone<BlockLogicTorchRedstone> modelTorchIdle;

	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		int i1 = meta & BlockLogicDelayer.MASK_DIRECTION;
		switch (i1) {
			case 0:
				renderBlocks.uvRotateTop = 0;
				break;
			case 1:
				renderBlocks.uvRotateTop = 1;
				break;
			case 2:
				renderBlocks.uvRotateTop = 3;
				break;
			case 3:
				renderBlocks.uvRotateTop = 2;
		}

		int j1 = (meta & BlockLogicDelayer.MASK_TICK_DELAY) >> 2;
		this.renderStandardBlock(tessellator, this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z), x, y, z);
		this.resetRenderBlocks();
		float brightness = 1.0F;
		if (LightmapHelper.isLightmapEnabled()) {
			int lmc = this.block.getLightmapCoord(renderBlocks.blockAccess, x, y, z);
			if (this.block.emission > 0) {
				lmc = LightmapHelper.setBlocklightValue(lmc, 15);
			}

			tessellator.setLightmapCoord(lmc);
		} else {
			brightness = this.getBlockBrightness(renderBlocks.blockAccess, x, y, z);
			if (this.block.emission > 0 || Global.accessor.isFullbrightEnabled()) {
				brightness = 1.0F;
			}
		}

		tessellator.setColorOpaque_F(brightness, brightness, brightness);
		double d = -0.1875;
		double d1 = 0.0;
		double d2 = 0.0;
		switch (i1) {
			case 0:
				d2 = BlockLogicDelayer.torchPosOffset[j1];
				break;
			case 1:
				d1 = -BlockLogicDelayer.torchPosOffset[j1];
				break;
			case 2:
				d2 = -BlockLogicDelayer.torchPosOffset[j1];
				break;
			case 3:
				d1 = BlockLogicDelayer.torchPosOffset[j1];
		}

		BlockModelTorch<?> modelTorch = this.block.getLogic().isRepeaterPowered ? modelTorchActive : modelTorchIdle;
		if (renderBlocks.overbright && this.block.getLogic().isRepeaterPowered) {
			modelTorch.renderTorchAtAngle(tessellator, (double)x + d1, (double)y + d, (double)z + d2, 0.0F, 0.0F);
		} else if (!renderBlocks.overbright) {
			modelTorch.renderTorchAtAngle(tessellator, (double)x + d1, (double)y + d, (double)z + d2, 0.0F, 0.0F);
		}

		return true;
	}

	public boolean shouldItemRender3d() {
		return false;
	}

	public boolean shouldSideBeRendered(WorldSource blockAccess, AABB bounds, int x, int y, int z, int side) {
		return true;
	}

	static {
		modelTorchActive = (BlockModelTorchRedstone<BlockLogicTorchRedstone>) BlockModelDispatcher.getInstance().getDispatch(Blocks.TORCH_REDSTONE_ACTIVE);
		modelTorchIdle = (BlockModelTorchRedstone<BlockLogicTorchRedstone>) BlockModelDispatcher.getInstance().getDispatch(Blocks.TORCH_REDSTONE_IDLE);
	}
}
