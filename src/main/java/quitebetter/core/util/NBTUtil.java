package quitebetter.core.util;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.data.SynchedEntityData;
import org.jetbrains.annotations.NotNull;

public class NBTUtil {
	public static boolean isDefault(CompoundTag tag) {
		return tag.equals(new CompoundTag());
	}
	public static boolean getSharedFlag(@NotNull Entity entity, int i) {
		return getSharedFlag(entity.getEntityData(), i);
	}
	public static boolean getSharedFlag(@NotNull SynchedEntityData data, int i) {
		return (data.getByte(0) & 1 << i) != 0;
	}
	public static void setSharedFlag(@NotNull Entity entity, int id, boolean flag) {
		setSharedFlag(entity.getEntityData(), id, flag);
	}
	public static void setSharedFlag(@NotNull SynchedEntityData data, int id, boolean flag) {
		byte sharedFlags = data.getByte(0);
		if (flag) {
			data.set(0, (byte)(sharedFlags | 1 << id));
		} else {
			data.set(0, (byte)(sharedFlags & ~(1 << id)));
		}
	}
}
