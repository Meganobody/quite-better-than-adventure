package quitebetter.core.feature;

import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.generate.feature.WorldFeature;
import quitebetter.core.block.ModBlocks;
import quitebetter.core.item.ModItems;
import quitebetter.core.util.WorldUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class WorldFeatureMaze extends WorldFeature {
	private int wallBlockA;
	private int wallBlockB;
	private int floorBlock;
	private int slabBlock;
	private int mazeDepth = 24;
	private int entranceHeight = 3;
	private int roomSize = 2;
	private int xSize = 5;
	private int zSize = 5;

	public WeightedRandomBag<WeightedRandomLootObject> chestLoot;

	public static final Biome[] allowedBiomes = {
		Biomes.OVERWORLD_SWAMPLAND,
		Biomes.OVERWORLD_CAATINGA,
		Biomes.OVERWORLD_CAATINGA_PLAINS,
		Biomes.OVERWORLD_OUTBACK
	};

	private void setBlock(World world, int x, int y, int z, int id) {
		world.setBlock(x, y, z, id);
	}

	@Override
	public boolean place(World world, Random random, int x, int y, int z) {
		this.wallBlockA = ModBlocks.BRICK_MUD_BAKED.id();
		this.wallBlockB = Blocks.MUD_BAKED.id();
		this.floorBlock = Blocks.COBBLE_STONE_MOSSY.id();
		this.slabBlock = ModBlocks.SLAB_BRICK_MUD_BAKED.id();

		Block<?> block = world.getBlock(x,y-1,z);
		if (block==null || !block.hasTag(BlockTags.IS_WATER)) {
			//CHEST LOOT
			this.chestLoot = new WeightedRandomBag();
			this.chestLoot.addEntry(new WeightedRandomLootObject((ItemStack)null), (double)892.0F);
			//BLOCKS
			this.chestLoot.addEntry(new WeightedRandomLootObject(Blocks.DIRT_SCORCHED.getDefaultStack(), 1, 3), (double)50.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Blocks.MUD.getDefaultStack(), 1, 3), (double)50.0F);
			//ARMOR
			//this.chestLoot.addEntry(new WeightedRandomLootObject(Items.ARMOR_HELMET_GOLD.getDefaultStack()), (double)15.0F);
			//this.chestLoot.addEntry(new WeightedRandomLootObject(Items.ARMOR_CHESTPLATE_GOLD.getDefaultStack()), (double)15.0F);
			//this.chestLoot.addEntry(new WeightedRandomLootObject(Items.ARMOR_LEGGINGS_GOLD.getDefaultStack()), (double)15.0F);
			//this.chestLoot.addEntry(new WeightedRandomLootObject(Items.ARMOR_BOOTS_GOLD.getDefaultStack()), (double)15.0F);
			//TOOLS
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.TOOL_SWORD_GOLD.getDefaultStack()), (double)15.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.TOOL_PICKAXE_GOLD.getDefaultStack()), (double)15.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.TOOL_AXE_GOLD.getDefaultStack()), (double)15.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.TOOL_SHOVEL_GOLD.getDefaultStack()), (double)15.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.TOOL_HOE_GOLD.getDefaultStack()), (double)15.0F);
			//VALUABLES
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.INGOT_GOLD.getDefaultStack(), 1, 5), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.ORE_RAW_GOLD.getDefaultStack(), 1, 5), (double)200.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.INGOT_IRON.getDefaultStack(), 1, 5), (double)200.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.ORE_RAW_IRON.getDefaultStack(), 1, 5), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.DIAMOND.getDefaultStack(), 1, 3), (double)5.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.DUST_REDSTONE.getDefaultStack(), 3, 6), (double)100.0F);

			//MISC
			for(int i = 0; i < 9; ++i) {
				this.chestLoot.addEntry(new WeightedRandomLootObject(new ItemStack(Item.itemsList[Items.RECORD_13.id + i])), (double)1.0F);
			}

			this.chestLoot.addEntry(new WeightedRandomLootObject(Blocks.SPONGE_DRY.getDefaultStack(), 1, 4), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.STRING.getDefaultStack(), 1, 6), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.SEEDS_PUMPKIN.getDefaultStack(), 1, 3), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.LABEL.getDefaultStack()), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.FEATHER_CHICKEN.getDefaultStack(), 2, 5), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(new ItemStack(Items.DYE,1,0), 2, 5), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(ModItems.SEASHELL.getDefaultStack(), 2, 8), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.FOOD_BREAD.getDefaultStack(), 1, 3), (double)100.0F);
			this.chestLoot.addEntry(new WeightedRandomLootObject(Items.FOOD_STEW_MUSHROOM.getDefaultStack()), (double)100.0F);

			generateEntrance(world, random, x, y, z);
		}
		return true;
	}

	public int getWallBlock(Random random) {
		return random.nextInt(5)==0 ? this.wallBlockB : this.wallBlockA;
	}

	public void generateEntrance(World world, Random random, int blockX, int blockY, int blockZ) {
		int size = this.roomSize;
		int depth = blockY-(WorldUtil.getOceanLevel(world)-this.mazeDepth);
		int height = this.entranceHeight;
		//ENTRANCE
		for (int x = blockX - size; x <= blockX + size; ++x) {
			for (int y = blockY; y <= blockY+height; ++y) {
				for (int z = blockZ - size; z <= blockZ + size; ++z) {
					boolean yWallCheck = y == blockY+height;
					if ((x==blockX-size || x==blockX+size) && (z==blockZ-size || z==blockZ+size)) {
						setBlock(world, x, y, z, this.getWallBlock(random));
					} else {
						if (yWallCheck) {
							setBlock(world, x, y, z, this.floorBlock);
						} else {
							setBlock(world, x, y, z, 0);
						}
					}
				}
			}
		}
		//PIT
		for (int x = blockX - size; x <= blockX + size; ++x) {
			for (int y = blockY - depth; y <= blockY-1; ++y) {
				for (int z = blockZ - size; z <= blockZ + size; ++z) {
					boolean xWallCheck = x == blockX - size || x == blockX + size;
					boolean zWallCheck = z == blockZ - size || z == blockZ + size;
					boolean yWallCheck = y == blockY - depth;
					if (!xWallCheck && !zWallCheck) {
						if (yWallCheck) {
							setBlock(world, x, y, z, this.floorBlock);
						} else {
							setBlock(world, x, y, z, 0);
						}
					} else {
						setBlock(world, x, y, z, this.getWallBlock(random));
					}
				}
			}
		}

		this.generateGrid(world, random, blockX, (WorldUtil.getOceanLevel(world)-this.mazeDepth)-1, blockZ, size, this.xSize, this.zSize);
	}

	public void generateGrid(World world, Random random, int blockX, int blockY, int blockZ, int size, int xSize, int zSize) {
		int wallh = 8;

		for (int xi = -(1+xSize*2); xi <= 1+xSize*2; ++xi) {
			if (xi % 2 != 0) {
				for (int z = -(1+zSize*2)*size; z <= (1+zSize*2)*size; z++) {
					for (int yi=1;yi<=wallh;yi++) {
						setBlock(world, blockX + xi * size, blockY+yi, blockZ+z, this.getWallBlock(random));
					}
				}
			}
		}
		for (int zi = -(1+zSize*2); zi <= 1+zSize*2; ++zi) {
			if (zi % 2 != 0) {
				for (int x = -(1+xSize*2)*size; x <= (1+xSize*2)*size; x++) {
					for (int yi=1;yi<=wallh;yi++) {
						setBlock(world, blockX+x, blockY+yi, blockZ + zi * size, this.getWallBlock(random));
					}
				}
			}
		}
		generateRooms(world, random, blockX, blockY, blockZ, size, xSize, zSize, wallh);
	}

	private void generateRooms(World world, Random random, int blockX, int blockY, int blockZ, int size, int xSize, int zSize, int height) {
		//DOORS
		for (int xi = -(1+xSize*2); xi <= 1+xSize*2; ++xi) {
			for (int zi = -(1+zSize*2); zi <= 1+zSize*2; ++zi) {
				if (zi % 2 == 0 && xi % 2 == 0) {
					this.setRandomRoomDoors(world,random,blockX+xi*size,blockY+2,blockZ+zi*size,size,xSize,zSize,xi/2,zi/2);
				}
			}
		}
		//ROOMS
		for (int xi = -(1+xSize*2); xi <= 1+xSize*2; ++xi) {
			for (int zi = -(1+zSize*2); zi <= 1+zSize*2; ++zi) {
				if (zi % 2 == 0 && xi % 2 == 0) {
					if (!(xi==0 && zi==0)) {
						this.generateRoom(world,random,blockX+xi*size,blockY+1,blockZ+zi*size,size,xSize,zSize,xi,zi,height);
					}
				}
			}
		}
	}

	private void generateRoom(World world, Random random, int blockX, int blockY, int blockZ, int size, int xSize, int zSize, int xGrid, int zGrid, int height) {
		this.clearRoom(world, blockX, blockY, blockZ, size, height-1);
		HashMap<Direction,Boolean> doors = this.getDoors(world, blockX, blockY, blockZ, size);

		switch (random.nextInt(4)) {
			case 0:
			case 1: //EMPTY
			case 2:
				this.generateEnvironment(world, random, blockX, blockY, blockZ, size, height, doors);
				this.generateFloor(world, random, blockX, blockY, blockZ, size, this.floorBlock);
				break;
			case 3: //TRAP
				//SPIKES
					int depth = 5;
					for (int x = blockX - size; x <= blockX + size; ++x) {
						for (int y = blockY - depth; y <= blockY-1; ++y) {
							for (int z = blockZ - size; z <= blockZ + size; ++z) {
								boolean xWallCheck = x == blockX - size || x == blockX + size;
								boolean zWallCheck = z == blockZ - size || z == blockZ + size;
								boolean yWallCheck = y == blockY - depth;
								if (!xWallCheck && !zWallCheck) {
									if (yWallCheck) {
										setBlock(world, x, y, z, this.floorBlock);
									} else {
										setBlock(world, x, y, z, 0);
									}
								} else {
									setBlock(world, x, y, z, this.getWallBlock(random));
								}
							}
						}
					}
					this.generateFloor(world, random, blockX, blockY-depth+1, blockZ, size, Blocks.SPIKES.id());
					if (random.nextInt(3)==0) {
						for (int y=blockY - depth; y <= blockY-1; ++y) {
							setBlock(world, blockX, y, blockZ, this.getWallBlock(random));
						}
					}
				break;
		}
		this.generateFloor(world, random, blockX, blockY+height-1, blockZ, size, this.floorBlock);
		this.generateDecor(world, random, blockX, blockY+height-2, blockZ, size, Blocks.COBWEB.id(), 5);
	}

	private void clearRoom(World world, int blockX, int blockY, int blockZ, int size, int height) {
		for (int y=0; y<=height; y++) {
			for (int x = -size+1; x<=size-1; x++) {
				for (int z = -size+1; z<=size-1; z++) {
					setBlock(world, blockX+x, blockY+y, blockZ+z, 0);
				}
			}
		}
	}

	private void generateFloor(World world, Random random, int blockX, int blockY, int blockZ, int size, int bid) {
		for (int x = -size+1; x<=size-1; x++) {
			for (int z = -size+1; z<=size-1; z++) {
				setBlock(world, blockX+x, blockY, blockZ+z, bid);
			}
		}
	}

	private void generateEnvironment(World world, Random random, int blockX, int blockY, int blockZ, int size, int height, HashMap<Direction, Boolean> doors) {
		Direction dir = getClosedDoor(doors);
		switch (random.nextInt(5)) {
			case 0: //SHELVES
				if (dir!=null) {
					Axis dax = dir.getAxis();
					int xo = dax.equals(Axis.Z) ? 1 : 0;
					int zo = dax.equals(Axis.X) ? 1 : 0;
					for (int i=-size;i<=size;i++) {
						setBlock(world, blockX+dir.getOffsetX()+xo*i,blockY+1,blockZ+dir.getOffsetZ()+zo*i,this.getWallBlock(random));
					}
					if (random.nextInt(3)==0) {
						for (int i=-size;i<=size;i++) {
							setBlock(world, blockX+dir.getOffsetX()+xo*i,blockY+3,blockZ+dir.getOffsetZ()+zo*i,this.getWallBlock(random));
						}
					}
					if (random.nextInt(3)==0) {
						this.generateChest(world,random,blockX+dir.getOffsetX(),blockY+1,blockZ+dir.getOffsetZ(),this.getCloseDoorsAmount(doors));
					}
				}
				break;
			case 1:
				//SHELF
				for (int x=-1;x<=1;x++) {
					world.setBlockAndMetadataWithNotify(blockX+x, blockY+height-3, blockZ, this.slabBlock, 2);
				}
				if (random.nextInt(2)==0) {
					this.generateChest(world, random, blockX, blockY+height-2, blockZ, this.getCloseDoorsAmount(doors));
				}
				break;
		}
	}

	private void generateDecor(World world, Random random, int blockX, int blockY, int blockZ, int size, int bid, int chance) {
		for (int x = -size+1; x<=size-1; x++) {
			for (int z = -size+1; z<=size-1; z++) {
				if (world.canPlaceInsideBlock(blockX+x, blockY, blockZ+z) && random.nextInt(chance)==0) {
					setBlock(world, blockX+x, blockY, blockZ+z, bid);
				}
			}
		}
	}

	//DOORS

	private int getCloseDoorsAmount(HashMap<Direction,Boolean> doors) {
		int out = 0;
		for (Direction dir : doors.keySet()) {
			if (!doors.get(dir)) {
				out += 1;
			}
		}
		return out;
	}

	private Direction getClosedDoor(HashMap<Direction,Boolean> doors) {
		for (Direction dir : doors.keySet()) {
			if (!doors.get(dir)) {
				return dir;
			}
		}
		return null;
	}

	private HashMap<Direction,Boolean> getDoors(World world, int blockX, int blockY, int blockZ, int size) {
		HashMap<Direction,Boolean> out = new HashMap<>();
		for (Direction dir : Direction.horizontalDirections) {
			int bid = world.getBlockId(blockX+dir.getOffsetX()*size,blockY+1,blockZ+dir.getOffsetZ()*size);
			out.put(dir,!(bid==this.wallBlockA || bid==this.wallBlockB));
		}
		return out;
	}

	private void setRandomRoomDoors(World world, Random random, int blockX, int blockY, int blockZ, int size, int xSize, int zSize, int xGrid, int zGrid) {
		int maxDoorValue = 4;
		if (zGrid!=-zSize) {
			setRoomDoors(world,random,blockX,blockY,blockZ,size,Direction.NORTH, random.nextInt(maxDoorValue));
		}
		if (xGrid!=xSize) {
			setRoomDoors(world, random, blockX, blockY, blockZ, size, Direction.EAST, random.nextInt(maxDoorValue));
		}
		if (zGrid!=zSize) {
			setRoomDoors(world, random, blockX, blockY, blockZ, size, Direction.SOUTH, random.nextInt(maxDoorValue));
		}
		if (xGrid!=-xSize) {
			setRoomDoors(world, random, blockX, blockY, blockZ, size, Direction.WEST, random.nextInt(maxDoorValue));
		}
	}

	private void setRoomDoors(World world, Random random, int blockX, int blockY, int blockZ, int size, Direction dir, int id) {
		int bid = 0;
		switch (id) {
			case 0: //OPEN
			case 1:
			case 2:
				break;
			case 3: //CLOSED
				bid = this.getWallBlock(random);
				break;
		}
		if (bid==0 && random.nextInt(2)==0) {
			bid = ModBlocks.BLOCK_BONE.id();
		}
		setBlock(world, blockX+dir.getOffsetX()*size, blockY, blockZ+dir.getOffsetZ()*size, bid);
		setBlock(world, blockX+dir.getOffsetX()*size, blockY+1, blockZ+dir.getOffsetZ()*size, bid);
	}

	//CHESTS

	private void generateChest(World world, Random random, int blockX, int blockY, int blockZ, int tier) {
		setBlock(world, blockX, blockY, blockZ, Blocks.CHEST_PLANKS_OAK.id());
		BlockLogicChest.setDefaultDirection(world, blockX, blockY, blockZ);
		TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(blockX, blockY, blockZ);
		if (tileentitychest==null) {
			world.setTileEntity(blockX,blockY,blockZ,new TileEntityChest());
		}

		int amount = 12+tier*4;
		for(int i = 0; i < amount; ++i) {
			ItemStack itemstack = ((WeightedRandomLootObject)this.chestLoot.getRandom(random)).getItemStack(random);
			if (itemstack != null && tileentitychest != null) {
				tileentitychest.setItem(random.nextInt(tileentitychest.getContainerSize()), itemstack);
			}
		}
	}
}
