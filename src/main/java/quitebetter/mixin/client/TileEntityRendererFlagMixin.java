package quitebetter.mixin.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FlagRenderer;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.model.ModelFlag;
import net.minecraft.client.render.tileentity.TileEntityRendererFlag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
	value = {TileEntityRendererFlag.class},
	remap = false
)
public class TileEntityRendererFlagMixin {
	@Shadow
	public Minecraft mc;
	@Shadow
	private ModelFlag modelFlag;
	@Shadow
	private ModelFlag modelFlagMirrored;
	@Shadow
	private static FlagRenderer flagRenderer;
	@Shadow
	private long lastTick;

	private static final float rotationValue = 15F;
	private static final float pushValue = 0.4F;

	private void loadTexture(String texturePath) {
		TileEntityRendererFlag terf = (TileEntityRendererFlag)(Object)this;
		TextureManager textureManager = terf.renderDispatcher.textureManager;
		textureManager.bindTexture(textureManager.loadTexture(texturePath));
	}

	private void setModelRotation(ModelFlag model, float x, float y, float z) {
		model.flagPole.setRotationPoint(0,0,0);
		model.flagPoleShort.setRotationPoint(0,0,0);
		model.flag.setRotationPoint(0,0,0);
		model.flagOverlay.setRotationPoint(0,0,0);
		model.flagBase.setRotationPoint(0,0,0);

		model.flagPole.setRotationAngle(x,y,z);
		model.flagPoleShort.setRotationAngle(x,y,z);
		model.flag.setRotationAngle(x,y,z);
		model.flagOverlay.setRotationAngle(x,y,z);
		model.flagBase.setRotationAngle(x,y,z);
	}

	@Inject(
		method = {"Lnet/minecraft/client/render/tileentity/TileEntityRendererFlag;doRender(Lnet/minecraft/core/block/entity/TileEntityFlag;DDDFZFF)V"},
		at = @At("HEAD"),
		cancellable = true
	)
	private void doRender(TileEntityFlag tileEntityFlag, double x, double y, double z, float partialTick, boolean shortPole, float windDirection, float windIntensity, CallbackInfo ci) {
		TileEntityRendererFlag terf = (TileEntityRendererFlag)(Object)this;
		World theWorld = Minecraft.getMinecraft().currentWorld;
		int meta = theWorld.getBlockMetadata(tileEntityFlag.x,tileEntityFlag.y,tileEntityFlag.z);
		int lowermeta = theWorld.getBlockMetadata(tileEntityFlag.x,tileEntityFlag.y-1,tileEntityFlag.z);
		Direction dir = Direction.getDirectionById(meta);
		boolean diagonal = dir.isHorizontal();

		if (flagRenderer == null) {
			flagRenderer = new FlagRenderer(terf.renderDispatcher.textureManager);
		}

		if (theWorld.getWorldTime() != lastTick) {
			lastTick = theWorld.getWorldTime();
			flagRenderer.tick();
		}

		float swayInterpolated = (float)tileEntityFlag.sway + partialTick;
		float sway = (float)((double)swayInterpolated / (double)1.5F % (Math.PI * 2D));
		sway = (float)(Math.sin((double)sway) * (double)windIntensity * (double)2.0F);
		GL11.glPushMatrix();
		float scale = 1.0F;

		if (diagonal) {
			GL11.glTranslatef(
				(float)x + 0.5F - dir.getOffsetX()*pushValue,
				(float)y + 0.75F,
				(float)z + 0.5F - dir.getOffsetZ()*pushValue);

			GL11.glRotatef(rotationValue, dir.getOffsetZ(), 0, -dir.getOffsetX());
		} else {
			GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
			setModelRotation(modelFlag,0.0F,0.0F,0.0F);
		}

		GL11.glScalef(1.0F, -1.0F, -1.0F);
		loadTexture("/assets/minecraft/textures/entity/flag.png");
		if (diagonal) {
			modelFlag.renderPole();
		} else {
			if (shortPole && lowermeta==0) {
				modelFlag.renderPoleShort();
			} else {
				modelFlag.renderPole();
				modelFlag.renderBase();
			}
		}

		GL11.glPushMatrix();
		if (diagonal) {
			int angle = 0;
			switch (dir) {
				case NORTH:
					angle = 3;
					break;
				case EAST:
					angle = 0;
					break;
				case SOUTH:
					angle = 1;
					break;
				case WEST:
					angle = 2;
					break;
			}
			GL11.glRotatef(angle*90 + sway, 0.0F, 1.0F, 0.0F);
		} else {
			GL11.glRotatef(windDirection + sway, 0.0F, 1.0F, 0.0F);
		}
		modelFlag.renderFlag();
		terf.renderDispatcher.textureManager.bindTexture(flagRenderer.getTexture(tileEntityFlag));
		GL11.glEnable(3042);
		GL11.glBlendFunc(769, 768);
		if (tileEntityFlag.getFlipped()) {
			modelFlagMirrored.renderFlagOverlay();
		} else {
			modelFlag.renderFlagOverlay();
		}

		GL11.glDisable(3042);
		GL11.glPopMatrix();
		float f4 = 0.01666667F;
		GL11.glTranslatef(0.0F, 0.5F, 0.07F);
		GL11.glScalef(0.01666667F, -0.01666667F, 0.01666667F);
		GL11.glNormal3f(0.0F, 0.0F, -0.01666667F);
		GL11.glPopMatrix();

		ci.cancel();
	}
}
