package quitebetter.core.mixin;

import quitebetter.core.achivement.ModAchievements;
import quitebetter.core.item.ItemFishingRodLoot;
import quitebetter.core.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityFishingBobber;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.MobPathfinder;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mixin(
	value = {EntityFishingBobber.class},
	remap = false
)
public class FishingBobberMixin {
	@Shadow
	private int ticksCatchable;
	@Shadow
	private int xTile;
	@Shadow
	private int yTile;
	@Shadow
	private int zTile;
	@Shadow
	public Player owner;
	@Shadow
	private int ticksInAir;
	@Shadow
	public Entity hookedEntity;
	@Shadow
	private int lerpSteps;
	@Shadow
	private double lerpX;
	@Shadow
	private double lerpY;
	@Shadow
	private double lerpZ;
	@Shadow
	private double lerpYRot;
	@Shadow
	private double lerpXRot;
	@Shadow
	private double velocityX;
	@Shadow
	private double velocityY;
	@Shadow
	private double velocityZ;
	private boolean lavafishing=false;

	private static boolean isItemFishingRod(Item item) {
		Item[] rods = {Items.TOOL_FISHINGROD, ModItems.TOOL_STEEL_FISHINGROD};
		return item != null && Arrays.asList(rods).contains(item);
	}

	@Inject(
		method = {"tick()V"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Entity;tick()V", shift = At.Shift.AFTER),
		cancellable = true
	)
	private void tickFishing(CallbackInfo ci) {
		Random random = new Random();
		EntityFishingBobber bob = (EntityFishingBobber)(Object)this;
		if (lerpSteps > 0) {
			double d = bob.x + (lerpX - bob.x) / (double)lerpSteps;
			double d1 = bob.y + (lerpY - bob.y) / (double)lerpSteps;
			double d2 = bob.z + (lerpZ - bob.z) / (double)lerpSteps;

			double d4;
			for(d4 = lerpYRot - (double)bob.yRot; d4 < (double)-180.0F; d4 += (double)360.0F) {
			}

			while(d4 >= (double)180.0F) {
				d4 -= (double)360.0F;
			}

			bob.yRot = (float)((double)bob.yRot + d4 / (double)lerpSteps);
			bob.xRot = (float)((double)bob.xRot + (lerpXRot - (double)bob.xRot) / (double)lerpSteps);
			--lerpSteps;
			bob.setPos(d, d1, d2);
			bob.setRot(bob.yRot, bob.xRot);
		} else {
			if (!bob.world.isClientSide) {
				ItemStack heldPlayerItem = bob.owner.getCurrentEquippedItem();
				if (bob.owner.removed || !bob.owner.isAlive() || heldPlayerItem == null || !isItemFishingRod(heldPlayerItem.getItem()) || bob.distanceToSqr(bob.owner) > (double)1024.0F) {
					bob.remove();
					bob.owner.bobberEntity = null;
					ci.cancel();
					return;
				}

				if (bob.hookedEntity != null) {
					if (!bob.hookedEntity.removed) {
						bob.x = bob.hookedEntity.x;
						bob.y = bob.hookedEntity.bb.minY + (double)bob.hookedEntity.bbHeight * 0.8;
						bob.z = bob.hookedEntity.z;
						if (bob.hookedEntity instanceof MobPathfinder) {
						((MobPathfinder) bob.hookedEntity).setTarget(bob.owner);
						}

						double dx = bob.owner.x - bob.x;
						double dy = bob.owner.y - bob.y;
						double dz = bob.owner.z - bob.z;
						double distance = (double)MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
						if (distance > (double)10.0F) {
							double scale = 0.01;
							Entity var10000 = bob.hookedEntity;
							var10000.xd += dx * scale;
							var10000 = bob.hookedEntity;
							var10000.yd += dy * scale;
							var10000 = bob.hookedEntity;
							var10000.zd += dz * scale;
						}

						ci.cancel();
						return;
					}

					bob.hookedEntity = null;
				}
			}

			if (bob.isInGround()) {
				if (bob.world.getBlockId(xTile, yTile, zTile) == Blocks.ROPE.id()) {
					bob.x = (double)xTile + (double)0.5F;
					bob.y = (double)yTile + (double)0.5F;
					bob.z = (double)zTile + (double)0.5F;
					ci.cancel();
					return;
				}

				bob.setInGround(false);
				bob.xd *= (double)(random.nextFloat() * 0.2F);
				bob.yd *= (double)(random.nextFloat() * 0.2F);
				bob.zd *= (double)(random.nextFloat() * 0.2F);
				ticksInAir = 0;
				ticksCatchable = 0;
			}

			++ticksInAir;
			Vec3 currentPos = Vec3.getTempVec3(bob.x, bob.y, bob.z);
			Vec3 nextPos = Vec3.getTempVec3(bob.x + bob.xd, bob.y + bob.yd, bob.z + bob.zd);
			HitResult clip = bob.world.checkBlockCollisionBetweenPoints(currentPos, nextPos);
			currentPos = Vec3.getTempVec3(bob.x, bob.y, bob.z);
			nextPos = Vec3.getTempVec3(bob.x + bob.xd, bob.y + bob.yd, bob.z + bob.zd);
			if (clip != null) {
				nextPos = Vec3.getTempVec3(clip.location.x, clip.location.y, clip.location.z);
				if (clip.hitType == HitResult.HitType.TILE && bob.world.getBlockId(clip.x, clip.y, clip.z) == Blocks.ROPE.id()) {
					bob.setInGround(true);
					xTile = clip.x;
					yTile = clip.y;
					zTile = clip.z;
				}
			}

			Entity entity = null;
			List<Entity> list = bob.world.getEntitiesWithinAABBExcludingEntity(bob, bob.bb.expand(bob.xd, bob.yd, bob.zd).grow((double)1.0F, (double)1.0F, (double)1.0F));
			double d3 = (double)0.0F;

			for(Entity e : list) {
				if (e.isPickable() && (e != bob.owner || ticksInAir >= 5)) {
					float f2 = 0.3F;
					AABB aabb = e.bb.grow((double)f2, (double)f2, (double)f2);
					HitResult newHitResult = aabb.clip(currentPos, nextPos);
					if (newHitResult != null) {
						double d6 = currentPos.distanceTo(newHitResult.location);
						if (d6 < d3 || d3 == (double)0.0F) {
							entity = e;
							d3 = d6;
						}
					}
				}
			}

			if (entity != null) {
				clip = new HitResult(entity);
			}

			if (clip != null && clip.entity != null && clip.entity.hurt(bob.owner, 0, DamageType.COMBAT)) {
				bob.hookedEntity = clip.entity;
			}

			bob.move(bob.xd, bob.yd, bob.zd);
			float f = MathHelper.sqrt(bob.xd * bob.xd + bob.zd * bob.zd);
			bob.yRot = (float)(Math.atan2(bob.xd, bob.zd) * (double)180.0F / Math.PI);

			for(bob.xRot = (float)(Math.atan2(bob.yd, (double)f) * (double)180.0F / Math.PI); bob.xRot - bob.xRotO < -180.0F; bob.xRotO -= 360.0F) {
			}

			while(bob.xRot - bob.xRotO >= 180.0F) {
				bob.xRotO += 360.0F;
			}

			while(bob.yRot - bob.yRotO < -180.0F) {
				bob.yRotO -= 360.0F;
			}

			while(bob.yRot - bob.yRotO >= 180.0F) {
				bob.yRotO += 360.0F;
			}

			bob.xRot = bob.xRotO + (bob.xRot - bob.xRotO) * 0.2F;
			bob.yRot = bob.yRotO + (bob.yRot - bob.yRotO) * 0.2F;
			float movementScale = 0.92F;
			if (bob.onGround || bob.horizontalCollision) {
				movementScale = 0.5F;
			}

			int k = 5;
			double d5 = (double)0.0F;

			for(int l = 0; l < k; ++l) {
				double d8 = bob.bb.minY + (bob.bb.maxY - bob.bb.minY) * (double)l / (double)k - (double)0.125F + (double)0.125F;
				double d9 = bob.bb.minY + (bob.bb.maxY - bob.bb.minY) * (double)(l + 1) / (double)k - (double)0.125F + (double)0.125F;
				AABB axisalignedbb1 = AABB.getTemporaryBB(bob.bb.minX, d8, bob.bb.minZ, bob.bb.maxX, d9, bob.bb.maxZ);
				if (bob.world.isAABBInMaterial(axisalignedbb1, Material.water)) {
					d5 += (double)1.0F / (double)k;
				}
			}

			if (d5 > (double)0.0F) {
				if (ticksCatchable > 0) {
					--ticksCatchable;
				} else {
					int catchRate = 3000;
					int rainRate = 0;
					int algaeRate = 0;
					int steelRate = 0;
					if (bob.world.canBlockBeRainedOn(MathHelper.floor(bob.x), MathHelper.floor(bob.y) + 1, MathHelper.floor(bob.z))) {
						rainRate = 200;
					}

					if (bob.world.getBlockId(MathHelper.floor(bob.x), MathHelper.floor(bob.y) + 1, MathHelper.floor(bob.z)) == Blocks.ALGAE.id()) {
						algaeRate = 100;
					}

					if (bob.owner.getHeldItem() != null && bob.owner.getHeldItem().equals(ModItems.TOOL_STEEL_FISHINGROD)) {
						steelRate = 100;
					}

					catchRate = catchRate - rainRate - algaeRate - steelRate;
					if (random.nextInt(catchRate) == 0) {
						ticksCatchable = random.nextInt(30) + 10;
						bob.yd -= 0.2;
						bob.world.playSoundAtEntity((Entity)null, bob, "random.splash", 0.25F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
						float f3 = (float)MathHelper.floor(bob.bb.minY);

						for(int i1 = 0; (float)i1 < 1.0F + bob.bbWidth * 20.0F; ++i1) {
							double xOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
							double zOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
							bob.world.spawnParticle("bubble", bob.x + xOff, (double)(f3 + 1.0F), bob.z + zOff, bob.xd, bob.yd - (double)(random.nextFloat() * 0.2F), bob.zd, 0);
						}

						for(int j1 = 0; (float)j1 < 1.0F + bob.bbWidth * 20.0F; ++j1) {
							double xOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
							double zOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
							bob.world.spawnParticle("splash", bob.x + xOff, (double)(f3 + 1.0F), bob.z + zOff, bob.xd, bob.yd, bob.zd, 0);
						}
					}
				}
			}

			if (ticksCatchable > 0) {
				bob.yd -= (double)(random.nextFloat() * random.nextFloat() * random.nextFloat()) * 0.2;
			}

			double d7 = d5 * (double)2.0F - (double)1.0F;
			bob.yd += 0.04 * d7;
			if (d5 > (double)0.0F) {
				movementScale = (float)((double)movementScale * 0.9);
				bob.yd *= 0.8;
			}

			bob.xd *= (double)movementScale;
			bob.yd *= (double)movementScale;
			bob.zd *= (double)movementScale;
			bob.setPos(bob.x, bob.y, bob.z);
		}

		int k = 5;
		double d5 = (double)0.0F;
		lavafishing = false;

		if (bob.world.dimension.equals(Dimension.NETHER)) {
			for(int l = 0; l < k; ++l) {
				double d8 = bob.bb.minY + (bob.bb.maxY - bob.bb.minY) * (double)l / (double)k - (double)0.125F + (double)0.125F;
				double d9 = bob.bb.minY + (bob.bb.maxY - bob.bb.minY) * (double)(l + 1) / (double)k - (double)0.125F + (double)0.125F;
				AABB axisalignedbb1 = AABB.getTemporaryBB(bob.bb.minX, d8, bob.bb.minZ, bob.bb.maxX, d9, bob.bb.maxZ);
				if (bob.world.isAABBInMaterial(axisalignedbb1, Material.lava)) {
					d5 += (double)1.0F / (double)k;
					if (owner.getHeldItem().getItem() == ModItems.TOOL_STEEL_FISHINGROD) {
						lavafishing = true;
					} else {
						bob.remove();
						bob.owner.bobberEntity = null;

						ci.cancel();
						return;
					}
				}
			}

			if (d5 > (double)0.0F) {
				if (ticksCatchable > 0) {
					--ticksCatchable;
				} else {
					int catchRate = 600;

					if (random.nextInt(catchRate) == 0) {
						ticksCatchable = random.nextInt(30) + 10;
						bob.yd -= 0.2;
						bob.world.playSoundAtEntity((Entity)null, bob, "liquid.lavapop", 0.5F, 0.3F + (random.nextFloat() - random.nextFloat()) * 0.4F);
						double xOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
						double zOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
						bob.world.spawnParticle("lava", bob.x + xOff, bob.y, bob.z + zOff,xOff, 1, zOff, 0);

					} else {
						if (random.nextInt(20) == 0) {
							double xOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
							double zOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
							bob.world.spawnParticle("smoke", bob.x + xOff, bob.y, bob.z + zOff,xOff, 0.3, zOff, 0);
						}
					}
				}
			}

			if (lavafishing) {
				if (ticksCatchable > 0) {
					bob.yd -= (double) (random.nextFloat() * random.nextFloat() * random.nextFloat()) * 0.2;
				}

				double d7 = d5 * (double) 2.0F - (double) 1.0F;
				bob.yd += (d5 > 0 ? 0.08 : 0.04) * d7;
				if (d5 > (double) 0.0F) {
					bob.yd *= 0.8;
				}
			}
		}
		ci.cancel();
	}

	@Inject(
		method = {"yoink()I"},
		at = @At("HEAD"),
		cancellable = true
	)
	private void yoink(CallbackInfoReturnable<Integer> cir) {
		int damage = 0;
		EntityFishingBobber bob = (EntityFishingBobber)(Object)this;
		Random random = new Random();
		if (ticksCatchable > 0) {
			if (lavafishing) {
				for (int i=1;i<10;i++) {
					double xOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
					double zOff = (double)((random.nextFloat() * 2.0F - 1.0F) * bob.bbWidth);
					bob.world.spawnParticle("lava", bob.x + xOff, bob.y, bob.z + zOff,xOff, 1, zOff, 0);
				}
				bob.world.playSoundAtEntity((Entity)null, bob, "random.splash", 0.25F, 0.3F + (random.nextFloat() - random.nextFloat()) * 0.4F);
				//
				EntityItem entityitem = new EntityItem(bob.world, bob.x, bob.y+1.5F, bob.z, new ItemStack(ItemFishingRodLoot.GetLavaLoot()));
				double dx = bob.owner.x - bob.x;
				double dy = bob.owner.y - bob.y;
				double dz = bob.owner.z - bob.z;
				double distance = (double)MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
				double scale = 0.1;
				entityitem.xd = dx * scale;
				entityitem.yd = (dy-2) * scale + (double)MathHelper.sqrt(distance) * 0.08;
				entityitem.zd = dz * scale;
				bob.world.entityJoinedWorld(entityitem);
				bob.owner.addStat(StatList.fishCaughtStat, 1);
				damage = 1;
				bob.remove();
				bob.owner.bobberEntity = null;
				cir.setReturnValue(damage);
				bob.owner.triggerAchievement(ModAchievements.LAVA_FISHING);
				Minecraft.getMinecraft().statsCounter.add(ModAchievements.LAVA_FISHING,1);
			} else {
				EntityItem entityitem = new EntityItem(bob.world, bob.x, bob.y, bob.z, new ItemStack(ItemFishingRodLoot.GetWaterLoot()));
				double dx = bob.owner.x - bob.x;
				double dy = bob.owner.y - bob.y;
				double dz = bob.owner.z - bob.z;
				double distance = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
				double scale = 0.1;
				entityitem.xd = dx * scale;
				entityitem.yd = dy * scale + (double) MathHelper.sqrt(distance) * 0.08;
				entityitem.zd = dz * scale;
				bob.world.entityJoinedWorld(entityitem);
				bob.owner.addStat(StatList.fishCaughtStat, 1);
				damage = 1;
				bob.remove();
				bob.owner.bobberEntity = null;
				cir.setReturnValue(damage);
			}
		}
	}
}
