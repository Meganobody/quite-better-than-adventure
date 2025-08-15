package quitebetter.core.projectile;

import net.minecraft.client.gui.modelviewer.categories.entries.entity.EntityEntryCreeper;
import net.minecraft.core.block.BlockLogicTNT;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.EntityPrimedTNT;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.entity.projectile.ProjectileArrowPurple;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3;
import quitebetter.core.block.ModBlocks;
import quitebetter.core.item.ModItems;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import quitebetter.core.util.BaseUtil;

import static quitebetter.core.ModCore.LOGGER;

public class ProjectileArrowTorch extends ProjectileArrow {

	public ProjectileArrowTorch(World world, Mob entityliving, boolean doesArrowBelongToPlayer) {
		super(world, entityliving, doesArrowBelongToPlayer, 0);
		this.mobsHit = 0;
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.inTile = 0;
		this.shake = 0;
		this.inData = 0;
		this.stack = new ItemStack(Items.AMMO_ARROW);
		this.inGround = false;
		this.doesArrowBelongToPlayer = false;
		this.setDoesArrowBelongToPlayer(doesArrowBelongToPlayer);
		this.arrowType = 0;
	}

	public ProjectileArrowTorch(World world, double x, double y, double z) {
		super(world, x, y, z, 0);
		this.mobsHit = 0;
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.inTile = 0;
		this.shake = 0;
		this.inData = 0;
		this.stack = new ItemStack(Items.AMMO_ARROW);
		this.inGround = false;
		this.doesArrowBelongToPlayer = false;
		this.setDoesArrowBelongToPlayer(doesArrowBelongToPlayer);
		this.arrowType = 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.tickCount > 1 && this.tickCount % 3 == 0) {
			world.spawnParticle("flame", this.x, this.y, this.z, 0, 0, 0, 0);
		}
	}

	@Override
	public void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		if (hitResult.entity != null) {
			hitResult.entity.remainingFireTicks = 25;
			world.playSoundAtEntity((Entity)null, this, "fire.ignite", 1F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
			if (hitResult.entity instanceof EntityPrimedTNT) {
				((EntityPrimedTNT) hitResult.entity).fuse = 0;
			}
		}
	}

	protected void inGroundAction() {
		this.remove();
		boolean spawned = false;
		int x;
		int y;
		int z;
		//CENTERED
		x = this.xTile;
		y = this.yTile;
		z = this.zTile;
		//TNT BLOCK
		if (world.getBlock(x,y,z)!=null && world.getBlock(x,y,z).equals(Blocks.TNT)) {
			((BlockLogicTNT) world.getBlock(x,y,z).getLogic()).ignite(world,x,y,z,true);
			return;
		}
		if (this.yTile<(int) this.y) {
			y=(int)this.y;
		} else if (this.xTile<(int) this.x) {
			x=(int)this.x;
		} else if (this.xTile>(int) this.x) {
			x=(int)this.x;
		} else if (this.zTile<(int) this.z) {
			z=this.zTile+1;
		} else if (this.zTile>=(int) this.z) {
			z=this.zTile-1;
		}

		if (world.getBlock(x, y, z) == null || (world.getBlock(x, y, z).hasTag(BlockTags.PLACE_OVERWRITES) && !(world.getBlock(x, y, z).hasTag(BlockTags.IS_LAVA)) && !(world.getBlock(x, y, z).hasTag(BlockTags.IS_WATER)) )) {
			if (ModBlocks.ARROW_TORCH.canPlaceBlockAt(world,x,y,z)) {
				world.setBlock(x, y, z, ModBlocks.ARROW_TORCH.id());
				world.playSoundAtEntity((Entity)null, this, "fire.ignite", 1F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
				for (int i=1; i < 5; i++) {
					world.spawnParticle("smoke", x+0.5, y+0.5, z+0.5, random.nextFloat()*(random.nextInt(2)==0 ? -0.3 : 0.3), random.nextFloat()*(random.nextInt(2)==0 ? -0.3 : 0.3), random.nextFloat()*(random.nextInt(2)==0 ? -0.3 : 0.3), 0);
				}
				spawned = true;
			}
		}
		if (!spawned) {
			out:
			for (int xi=-1; xi < 1; xi++) {
				for (int yi = -1; yi < 1; yi++) {
					for (int zi = -1; zi < 1; zi++) {
						x = (int) (this.x + xi);
						y = (int) (this.y + yi);
						z = (int) (this.z + zi);
						if (world.getBlock(x, y, z) == null || (world.getBlock(x, y, z).hasTag(BlockTags.PLACE_OVERWRITES) && !(world.getBlock(x, y, z).hasTag(BlockTags.IS_LAVA)) && !(world.getBlock(x, y, z).hasTag(BlockTags.IS_WATER)) )) {
							if (ModBlocks.ARROW_TORCH.canPlaceBlockAt(world, x, y, z)) {
								world.setBlock(x, y, z, ModBlocks.ARROW_TORCH.id());
								world.playSoundAtEntity((Entity) null, this, "fire.ignite", 1F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
								for (int i = 1; i < 5; i++) {
									world.spawnParticle("smoke", x + 0.5, y + 0.5, z + 0.5, random.nextFloat() * (random.nextInt(2) == 0 ? -0.3 : 0.3), random.nextFloat() * (random.nextInt(2) == 0 ? -0.3 : 0.3), random.nextFloat() * (random.nextInt(2) == 0 ? -0.3 : 0.3), 0);
								}
								spawned = true;
								break out;
							}
						}
					}
				}
			}
		}

		if (!spawned) {
			world.entityJoinedWorld(
				new EntityItem(world, this.x, this.y, this.z, new ItemStack(ModItems.AMMO_ARROW_TORCH))
			);
		}
	}
}
