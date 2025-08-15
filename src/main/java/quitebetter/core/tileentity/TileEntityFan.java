package quitebetter.core.tileentity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.packet.PacketEntityFling;
import net.minecraft.core.net.packet.PacketMovePlayer;
import net.minecraft.server.entity.player.PlayerServer;
import quitebetter.core.block.BlockLogicFan;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.List;

public class TileEntityFan extends TileEntity {
	private int getSightRange(World world, double x, double y, double z, Direction facing) {
		if (facing == Direction.NONE) {
			return 0;
		} else {
			int range = 4;
			for(int i = 1; i <= range; ++i) {
				int x1 = MathHelper.round(x + (double)(facing.getOffsetX() * i));
				int y1 = MathHelper.round(y + (double)(facing.getOffsetY() * i));
				int z1 = MathHelper.round(z + (double)(facing.getOffsetZ() * i));
				int id = world.getBlockId(x1, y1, z1);
				if (Blocks.solid[id]) {
					return i - 1;
				}
			}
			return range;
		}
	}

	private AABB getDetectionBox(double x, double y, double z, Direction facing, int range) {
		double x1 = x + (double)facing.getOffsetX();
		double y1 = y + (double)facing.getOffsetY();
		double z1 = z + (double)facing.getOffsetZ();
		double x2 = x + (double)(facing.getOffsetX() * range);
		double y2 = y + (double)(facing.getOffsetY() * range);
		double z2 = z + (double)(facing.getOffsetZ() * range);
		double minX = Math.min(x1, x2);
		double minY = Math.min(y1, y2);
		double minZ = Math.min(z1, z2);
		double maxX = Math.max(x1, x2) + (double)1.0F;
		double maxY = Math.max(y1, y2) + (double)1.0F;
		double maxZ = Math.max(z1, z2) + (double)1.0F;
		return AABB.getTemporaryBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public void tick() {
		if (this.worldObj != null && !this.worldObj.isClientSide && ((BlockLogicFan) this.getBlock().getLogic()).isActive() && !((BlockLogicFan) this.getBlock().getLogic()).isSignalOverriden(worldObj,x,y,z)) {
			int id = this.worldObj.getBlockId(this.x, this.y, this.z);
			Block block = this.worldObj.getBlock(this.x,this.y,this.z);
			int meta = this.worldObj.getBlockMetadata(this.x, this.y, this.z);
			Direction facing = BlockLogicFan.getDirection(meta);
			int effectiveRange = this.getSightRange(this.worldObj, (double)this.x, (double)this.y, (double)this.z, facing);
			if (effectiveRange > 0) {
				AABB detectionBox = this.getDetectionBox((double)this.x, (double)this.y, (double)this.z, facing, effectiveRange);
				List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, detectionBox);
				for(int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity)list.get(i);
					if (entity.canInteract()) {
						double xd = (double) facing.getOffsetX() /6.5 /(BlockLogicFan.isInverted(block) ? -1 : 1);
						double yd = (double) facing.getOffsetY() /6.5 /(BlockLogicFan.isInverted(block) ? -1 : 1);
						double zd = (double) facing.getOffsetZ() /6.5 /(BlockLogicFan.isInverted(block) ? -1 : 1);
						if (EnvironmentHelper.isServerEnvironment() && entity instanceof PlayerServer) {
							entity.xd += (double) facing.getOffsetX() /6.5 /(BlockLogicFan.isInverted(block) ? -1 : 1);
							entity.yd += (double) facing.getOffsetY() /6.5 /(BlockLogicFan.isInverted(block) ? -1 : 1);
							entity.zd += (double) facing.getOffsetZ() /6.5 /(BlockLogicFan.isInverted(block) ? -1 : 1);
							entity.fallDistance = 0.0F;
							PlayerServer player = (PlayerServer) entity;
							player.playerNetServerHandler.sendPacket(new PacketEntityFling(player.id, entity.xd, entity.yd, entity.zd, 0, 0));
						} else {
							entity.fling(xd,yd,zd,0);
						}
						//FIRE
						if ( ((BlockLogicFan) this.getBlock().getLogic()).isModifierFire(worldObj,x,y,z) ) {
							entity.remainingFireTicks += 300;
						}
					}
				}
			}
		}
	}
}
