package quitebetter.mixin.core.packet;

import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.net.packet.PacketAddParticle;
import net.minecraft.server.net.handler.PacketHandlerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(
	value = {PacketAddParticle.class},
	remap = false
)
//Small fix.
public class PacketAddParticleMixin {
	@Inject(
		method = {"read"},
		at = @At("TAIL")
	)
	private void read(DataInputStream dis, CallbackInfo ci) throws IOException {
		PacketAddParticle packet = (PacketAddParticle)(Object)this;
		packet.data = dis.readInt();
	}

	@Inject(
		method = {"write"},
		at = @At("TAIL")
	)
	private void write(DataOutputStream dos, CallbackInfo ci) throws IOException {
		PacketAddParticle packet = (PacketAddParticle)(Object)this;
		dos.writeInt(packet.data);
	}
}
