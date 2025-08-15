package quitebetter.core.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicDoor;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;

import java.util.Random;

public class TileEntityGlued extends TileEntity {
	private Boolean glued = false;

	public boolean isGlued() {return this.glued;}

	public void applyGlue() {
		Random rand = new Random();
		assert worldObj != null;
		Block<?> block = worldObj.getBlock(x,y,z);
		assert block != null;
		if (!this.isGlued()) {
			this.glued = true;

			for(int j = 0; j < 4; ++j) {
				worldObj.spawnParticle("item", x+rand.nextFloat(), y+rand.nextFloat(), z+rand.nextFloat(), (double)(rand.nextInt(11)-5)/10, (double)(rand.nextInt(11)-5)/10, (double)(rand.nextInt(11)-5)/10, Items.SLIMEBALL.id);
			}
			worldObj.playSoundEffect((Entity)null, SoundCategory.WORLD_SOUNDS, x, y, z, "mob.slimeattack", 0.25F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);

			if (block.getLogic() instanceof BlockLogicDoor) {
				int off = ((BlockLogicDoor) block.getLogic()).isTop ? -1 : 1;
				TileEntityGlued tile = (TileEntityGlued) worldObj.getTileEntity(x,y+off,z);
				if (tile!=null) { tile.applyGlue(); }
			}
		}
	}

	public void readFromNBT(CompoundTag compoundTag) {
		super.readFromNBT(compoundTag);
		this.glued = compoundTag.getBoolean("Glued");
	}

	public void writeToNBT(CompoundTag compoundTag) {
		super.writeToNBT(compoundTag);
		if (this.glued != null) {
			compoundTag.putBoolean("Glued", this.glued);
		}

	}
}
