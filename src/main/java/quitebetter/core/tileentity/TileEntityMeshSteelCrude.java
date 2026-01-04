package quitebetter.core.tileentity;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import org.jetbrains.annotations.Nullable;

public class TileEntityMeshSteelCrude extends TileEntity {
	public int stackSize;
	public int ticksRan = 0;

	public void tick() {
		if (this.ticksRan == 0) {
			this.ticksRan = this.worldObj.rand.nextInt(360);
		}
		++this.ticksRan;
	}

	public boolean setFilter(Player player, @Nullable ItemStack stack) {
		if (stack != null) {
			this.stackSize = stack.stackSize;
			this.setChanged();
			return true;
		} else {
			boolean hasFilter = stackSize <= 0;
			this.stackSize = 0;
			this.setChanged();
			return stackSize <= 0 || !hasFilter;
		}
	}

	public void readFromNBT(CompoundTag compoundTag) {
		super.readFromNBT(compoundTag);
		this.stackSize = compoundTag.getIntegerOrDefault("stackSize", 0);
	}

	public void writeToNBT(CompoundTag compoundTag) {
		super.writeToNBT(compoundTag);
		compoundTag.putInt("stackSize", this.stackSize);

	}

	public Packet getDescriptionPacket() {
		return new PacketTileEntityData(this);
	}
}
