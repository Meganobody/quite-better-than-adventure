package quitebetter.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.client.world.WorldClientMP;
import net.minecraft.core.net.packet.PacketAddParticle;
import net.minecraft.core.net.packet.PacketContainerSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static quitebetter.core.ModCore.LOGGER;

@Mixin(
	value = {PacketHandlerClient.class},
	remap = false
)
public class PacketHandlerClientMixin {
	@Shadow
	WorldClientMP worldClientMP;
	@Shadow
	Random rand;
	@Shadow
	Minecraft mc;

	@Inject(
		method = {"handleSpawnParticle"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void handleSpawnParticle(PacketAddParticle addParticlePacket, CallbackInfo ci) {
//		if (addParticlePacket.isGroup) {
//			for(int i = 0; i < addParticlePacket.amount; ++i) {
//				worldClientMP.spawnParticle(addParticlePacket.particleKey, addParticlePacket.x + rand.nextDouble() * (double)addParticlePacket.randOffX, addParticlePacket.y + this.rand.nextDouble() * (double)addParticlePacket.randOffY, addParticlePacket.z + rand.nextDouble() * (double)addParticlePacket.randOffZ, addParticlePacket.motionX + rand.nextGaussian() * (double)addParticlePacket.randMotionX, addParticlePacket.motionY + rand.nextGaussian() * (double)addParticlePacket.randMotionY, addParticlePacket.motionZ + rand.nextGaussian() * (double)addParticlePacket.randMotionZ, addParticlePacket.data, addParticlePacket.maxDistance);
//			}
//		} else {
//			worldClientMP.spawnParticle(addParticlePacket.particleKey, addParticlePacket.x, addParticlePacket.y, addParticlePacket.z, addParticlePacket.motionX, addParticlePacket.motionY, addParticlePacket.motionZ, addParticlePacket.data, addParticlePacket.maxDistance);
//		}
//		ci.cancel();
	}

	@Inject(
		method = {"handleSetSlot"},
		at = @At("HEAD")
	)
	public void handleSetSlot(PacketContainerSetSlot containerSetslotPacket, CallbackInfo ci) {
		if (containerSetslotPacket.windowId == 0 && containerSetslotPacket.itemSlot<9) {
			containerSetslotPacket.itemSlot += 36;
			LOGGER.warn("PacketContainerSetSlot was modified!");
		}
	}
}
