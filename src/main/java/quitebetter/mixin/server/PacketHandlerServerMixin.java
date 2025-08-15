package quitebetter.mixin.server;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityFishingBobber;
import net.minecraft.core.net.packet.PacketBlockUpdate;
import net.minecraft.core.net.packet.PacketPlayerAction;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.net.packet.PacketUseOrPlaceItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quitebetter.core.util.ILeftClickable;
import quitebetter.core.util.IRightClickable;

import static quitebetter.core.ModCore.LOGGER;

@Mixin(
	value = {PacketHandlerServer.class},
	remap = false
)
public class PacketHandlerServerMixin {
	@Shadow
	PlayerServer playerEntity;
	@Shadow
	MinecraftServer mcServer;

	@Inject(
		method = {"handleBlockDig"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void handleBlockDig(PacketPlayerAction packet, CallbackInfo ci) {
		PacketHandlerServer handler = (PacketHandlerServer)(Object)this;

		int action = packet.action;
		int x = packet.xPosition;
		int y = packet.yPosition;
		int z = packet.zPosition;
		Side side = packet.side;
		WorldServer world = mcServer.getDimensionWorld(this.playerEntity.dimension);

		if (action == PacketPlayerAction.ACTION_DIG_START ||
			action == PacketPlayerAction.ACTION_DIG_CONTINUED ||
			action == PacketPlayerAction.ACTION_DIG_COMPLETE) {
			//LOGGER.info("Captured {} with action {}. Checking ILeftClickable.", packet.getClass().getName(),action);
			Block block = world.getBlock(x,y,z);
			if (block!=null && (block.getLogic() instanceof ILeftClickable)) {
				ILeftClickable logic = (ILeftClickable) block.getLogic();
				if (logic.preventsBreaking(world,x,y,z,playerEntity,side)) {
					logic.onBlockLeftClicked(world,x,y,z,playerEntity,side);
					playerEntity.playerNetServerHandler.sendPacket(new PacketTileEntityData(world.getTileEntity(x,y,z)));
					playerEntity.playerNetServerHandler.sendPacket(new PacketBlockUpdate(x, y, z, world));
					ci.cancel();
				}
			}
		}
	}

	@Inject(
		method = {"handlePlace"},
		at = @At("HEAD"),
		cancellable = true
	)
	public void handlePlace(PacketUseOrPlaceItemStack packet, CallbackInfo ci) {
		PacketHandlerServer handler = (PacketHandlerServer)(Object)this;

		int type = packet.type;
		int x = packet.xPosition;
		int y = packet.yPosition;
		int z = packet.zPosition;
		Direction direction = packet.direction;
		WorldServer world = mcServer.getDimensionWorld(this.playerEntity.dimension);

		//LOGGER.info("Captured {} with type {}. Checking IRightClickable.", packet.getClass().getName(),type);
		Block block = world.getBlock(x,y,z);
		if (block!=null && (block.getLogic() instanceof IRightClickable)) {
			IRightClickable logic = (IRightClickable) block.getLogic();
			if (logic.preventsInteraction(world,x,y,z,playerEntity,direction.getSide())) {
				logic.onBlockRightClicked(world,x,y,z,playerEntity,direction.getSide());
				playerEntity.playerNetServerHandler.sendPacket(new PacketTileEntityData(world.getTileEntity(x,y,z)));
				playerEntity.playerNetServerHandler.sendPacket(new PacketBlockUpdate(x, y, z, world));
				ci.cancel();
			}
		}
	}
}
