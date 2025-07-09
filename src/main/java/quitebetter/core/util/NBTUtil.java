package quitebetter.core.util;

import com.mojang.nbt.tags.CompoundTag;

public class NBTUtil {
	public static boolean isDefault(CompoundTag tag) {
		return tag.equals(new CompoundTag());
	}
}
