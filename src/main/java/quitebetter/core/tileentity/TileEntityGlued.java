package quitebetter.core.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;

public class TileEntityGlued extends TileEntity {
	public Boolean Glued=false;

	public void readFromNBT(CompoundTag compoundTag) {
		super.readFromNBT(compoundTag);
		this.Glued = compoundTag.getBoolean("Glued");
	}

	public void writeToNBT(CompoundTag compoundTag) {
		super.writeToNBT(compoundTag);
		if (this.Glued != null) {
			compoundTag.putBoolean("Glued", this.Glued);
		}

	}
}
