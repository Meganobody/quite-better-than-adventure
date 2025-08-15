package quitebetter.mixin.core.feature;

import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.BlockLogicVeryRotatable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.entity.TileEntityMobSpawner;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quitebetter.core.block.ModBlocks;

import java.util.Random;
import java.util.logging.Logger;

@Mixin(
	value = {WorldFeatureLabyrinth.class},
	remap = false
)
public class WorldFeatureLabyrinthMixin {
	@Shadow
	int dungeonSize;
	@Shadow
	int dungeonLimit;
	@Shadow
	int dungeonCount;
	@Shadow
	boolean treasureGenerated;
	@Shadow
	boolean libraryGenerated;
	@Shadow
	boolean isCold;
	@Shadow
	int wallBlockA;
	@Shadow
	int wallBlockB;
	@Shadow
	int brickBlockA;
	@Shadow
	int brickBlockB;
	@Shadow
	int slabBlock;
	@Shadow
	public ItemStack treasureItem;
	@Shadow
	public WeightedRandomBag<WeightedRandomLootObject> chestLoot;
	@Shadow
	public WeightedRandomBag<WeightedRandomLootObject> dispenserLoot;
	@Shadow
	public WeightedRandomBag<String> spawnerMonsters;
	//METHOD COPIES
	@Unique
	private boolean canReplace(World world, int x, int y, int z) {
		if (y <= 11) {
			return false;
		} else if (world.getBlockId(x, y, z) != brickBlockA && world.getBlockId(x, y, z) != Blocks.PLANKS_OAK.id() && world.getBlockId(x, y, z) != Blocks.COBWEB.id() && world.getBlockId(x, y, z) != Blocks.BOOKSHELF_PLANKS_OAK.id() && world.getBlockId(x, y, z) != Blocks.MOBSPAWNER.id() && world.getBlockId(x, y, z) != this.brickBlockB) {
			if (world.getBlockId(x, y, z) != Blocks.MOTION_SENSOR_IDLE.id() && world.getBlockId(x, y, z) != Blocks.DISPENSER_COBBLE_STONE.id() && world.getBlockId(x, y, z) != Blocks.MOTION_SENSOR_ACTIVE.id()) {
				return BlockTags.CAVES_CUT_THROUGH.appliesTo(world.getBlock(x, y, z)) || world.getBlockMaterial(x, y, z) == Material.grass || world.getBlockMaterial(x, y, z) == Material.dirt || world.getBlockMaterial(x, y, z).isStone() || world.getBlockMaterial(x, y, z) == Material.sand || world.getBlockMaterial(x, y, z) == Material.moss;
			} else {
				world.removeBlockTileEntity(x, y, z);
				world.setBlockWithNotify(x, y, z, 0);
				return true;
			}
		} else {
			return false;
		}
	}
	@Unique
	private ItemStack pickDispenserLootItem(Random random) {
		return ((WeightedRandomLootObject)this.dispenserLoot.getRandom(random)).getItemStack(random);
	}
	@Unique
	private ItemStack pickCheckLootItem(Random random) {
		if (!this.treasureGenerated && this.dungeonSize > 7) {
			this.treasureGenerated = true;
			return this.treasureItem.copy();
		} else {
			return ((WeightedRandomLootObject)this.chestLoot.getRandom(random)).getItemStack(random);
		}
	}
	//UNIQUE
	@Unique
	private boolean isRoomDirectionEmpty(World world, int x, int y, int z, int roomsize, Direction dir) {
		return world.canPlaceInsideBlock(x+dir.getOffsetX()*roomsize,y+dir.getOffsetY()*roomsize,z+dir.getOffsetZ()*roomsize);
	}
	//INJECTIONS
	@Inject(
		method = {"place"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/WeightedRandomBag;addEntry(Ljava/lang/Object;D)V",
			shift = At.Shift.AFTER
		)
	)
	private void place(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if (x % 7 == 0 && !isCold) {
			this.wallBlockA = Blocks.BRICK_CLAY.id();
			this.wallBlockB = ModBlocks.BRICK_CLAY_MOSSY.id();
			this.brickBlockA = Blocks.BRICK_STONE_POLISHED.id();
			this.brickBlockB = Blocks.BRICK_STONE_POLISHED_MOSSY.id();
			this.slabBlock = Blocks.SLAB_STONE_POLISHED.id();
		}
	}

	@Inject(
		method = {"generateCorridor"},
		at = @At("TAIL")
	)
	private void generateCorridor(World world, Random random, int blockX, int blockY, int blockZ, int rot, int corridorIteration, CallbackInfo ci) {
		byte height = 2;
		int width = 2;
		int length = 2;

		for(int x = blockX - width; x <= blockX + width; ++x) {
			boolean xWallCheck = x == blockX - width || x == blockX + width;
			for (int y = blockY - height; y <= blockY + (height - 1); ++y) {
				boolean yWallCheck = y == blockY - height;

				for (int z = blockZ - length; z <= blockZ + length; ++z) {
					boolean zWallCheck = z == blockZ - length || z == blockZ + length;
					if (y>blockY-height && !world.canPlaceInsideBlock(x, y-1, z) && !zWallCheck && !xWallCheck && random.nextInt(7) == 0) {
						world.setBlockWithNotify(x, y, z, ModBlocks.BLOCK_BONE.id());
					}
				}
			}
		}
	}

	@Inject(
		method = {"generateDungeon"},
		at = @At("HEAD"),
		cancellable = true
	)
	private void generateDungeon(World world, Random random, int blockX, int blockY, int blockZ, boolean doSpawner, CallbackInfo ci) {
		switch (random.nextInt(3)) {
			case 0:
				generateCrypt(world, random, blockX, blockY, blockZ);
				ci.cancel();
				break;
		}
	}
	//CUSTOM
	@Unique
	private void generateCrypt(World world, Random random, int blockX, int blockY, int blockZ) {
		int size = 4;
		Axis roomAxis = null;
		if (
			!isRoomDirectionEmpty(world,blockX,blockY,blockZ,size,Direction.EAST) &&
				!isRoomDirectionEmpty(world,blockX,blockY,blockZ,size,Direction.WEST)
		) {
			roomAxis = Axis.Z;
		} else if (
			!isRoomDirectionEmpty(world,blockX,blockY,blockZ,size,Direction.NORTH) &&
				!isRoomDirectionEmpty(world,blockX,blockY,blockZ,size,Direction.SOUTH)
		) {
			roomAxis = Axis.X;
		}
		if (roomAxis==null) { return; }

		if (blockY >= 10) {
			//BASE ROOM
			for (int x = blockX - size; x <= blockX + size; ++x) {
				for (int y = blockY - 2; y <= blockY + 2; ++y) {
					for (int z = blockZ - size; z <= blockZ + size; ++z) {
						boolean xWallCheck = x == blockX - size || x == blockX + size;
						boolean zWallCheck = z == blockZ - size || z == blockZ + size;
						boolean yWallCheck = y == blockY - 2;
						if (this.canReplace(world, x, y, z)) {
							if (!xWallCheck && !zWallCheck) {
								if (yWallCheck) {
									if (random.nextInt(5) == 0) {
										world.setBlockWithNotify(x, y, z, this.wallBlockA);
									} else {
										world.setBlockWithNotify(x, y, z, this.wallBlockB);
									}
								} else {
									world.setBlockWithNotify(x, y, z, 0);
								}
							} else {
								world.setBlockWithNotify(x, y, z, this.wallBlockB);
							}
						}
					}
				}
			}

			int xo = roomAxis.equals(Axis.X) ? 1 : 0;
			int zo = roomAxis.equals(Axis.Z) ? 1 : 0;
			int cx;
			int cy;
			int cz;
			boolean flip;
			//CRYPT SIDES
			cx = blockX-size+1;
			cy = blockY-1;
			cz = blockZ-size+1;
			flip = false;
			generateCryptWall(world, random, xo, zo, cx, cy, cz, size, flip, roomAxis);
			cx = roomAxis.equals(Axis.X) ? blockX-size+1 : blockX+size-1;
			cy = blockY-1;
			cz = roomAxis.equals(Axis.Z) ? blockZ-size+1 : blockZ+size-1;
			flip = true;
			generateCryptWall(world, random, xo, zo, cx, cy, cz, size, flip, roomAxis);
		}
	}

	@Unique
	private void generateCryptWall(World world, Random random, int xo, int zo, int cx, int cy, int cz, int size, boolean flip, Axis roomAxis) {
		int bid;
		//LOWER LAYER
		for (int i=0;i<size*2-1;i++) {
			if (i % 2 == 0) {
				world.setBlockWithNotify(cx+xo*i,cy,cz+zo*i,this.slabBlock);
			} else {
				bid = random.nextInt(5)==0?this.wallBlockA:this.wallBlockB;
				world.setBlockWithNotify(cx+xo*i,cy,cz+zo*i,bid);
			}
		}
		cy+=1;
		//TOMBS
		for (int i=0;i<size*2-1;i++) {
			if (i % 2 == 0) {
				//CONTENTS
				int x = cx + xo * i - (flip?zo * -2:zo * 2);
				int z = cz + zo * i - (flip?xo * -2:xo * 2);

				switch (random.nextInt(6)) {
					case 0:
					case 1:
					case 2:
						world.setBlockWithNotify(x,cy, z,ModBlocks.BLOCK_BONE.id());
						break;
					case 3:
						world.setBlockWithNotify(x,cy, z,Blocks.BLOCK_LAPIS.id());
						break;
					case 4:
						world.setBlockAndMetadataWithNotify(x,cy, z,
							Blocks.CHEST_PLANKS_OAK.id(), roomAxis.equals(Axis.Z) ? (
								!flip ? 5 : 4
							) : (
								!flip ? 2 : 0
							));
						TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(x,cy, z);
						for(int k4 = 0; k4 < 10; ++k4) {
							ItemStack itemstack = this.pickCheckLootItem(random);
							if (itemstack != null) {
								tileentitychest.setItem(random.nextInt(tileentitychest.getContainerSize()), itemstack);
							}
						}
						break;
					case 5:
						world.setBlockAndMetadataWithNotify(x,cy, z,
							Blocks.MOTION_SENSOR_IDLE.id(), roomAxis.equals(Axis.Z) ? (
								!flip ? 5 : 4
							) : (
								!flip ? 3 : 2
							));
						break;
				}

				bid = random.nextInt(3)==0?Blocks.GRAVEL.id():ModBlocks.BLOCK_BONE.id();
				world.setBlockWithNotify(
					cx+xo*i-(flip?-zo:zo),
					cy,
					cz+zo*i-(flip?-xo:xo),
					bid);

				bid = this.slabBlock;
			} else {
				bid = random.nextInt(5)==0?this.wallBlockA:this.wallBlockB;
			}
			world.setBlockWithNotify(cx+xo*i,cy,cz+zo*i,bid);
		}
		cy+=1;
		//SHELF
		for (int i=0;i<size*2-1;i++) {
			bid = random.nextInt(5)==0?this.wallBlockA:this.wallBlockB;
			world.setBlockWithNotify(cx+xo*i,cy,cz+zo*i,bid);
		}

		cy+=1;
		for (int i=0;i<size*2-1;i++) {
			bid = 0;
			switch (random.nextInt(4)) {
				case 0:
				case 1:
					break;
				case 2:
					bid = Blocks.COBWEB.id();
					break;
				case 3:
					bid = ModBlocks.BLOCK_BONE.id();
					break;
			}
			world.setBlockWithNotify(cx+xo*i,cy,cz+zo*i,bid);
		}
	}
}
