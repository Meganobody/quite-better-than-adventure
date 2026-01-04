package quitebetter.mixin.core;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.world.data.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quitebetter.core.util.NBTUtil;

@Mixin(
	value = {EntityItem.class},
	remap = false
)
public abstract class EntityItemMixin {
	private static final int FLAG_CLAMP_DISABLED = 4;
	@Shadow protected int age;

	@Inject(
		method = {"tick"},
		at = @At("TAIL")
	)
	public void tick(CallbackInfo ci)
	{
		// Приведение для доступа к функционалу целевого класса
		if (age == 20) {
			Log.info(LogCategory.TEST, "Flag CLAMP_DISABLED should taken away");
			NBTUtil.setSharedFlag(((IEntityAccessor) this).getEntityData(), FLAG_CLAMP_DISABLED, false);
		}
	}
	@Inject(
		method = {"combineItems"},
		at = @At("HEAD"),
		cancellable = true
	)
	private static void onCombineItems(EntityItem entityItem1, EntityItem entityItem2, CallbackInfo ci)
	{
		if (NBTUtil.getSharedFlag(entityItem1, FLAG_CLAMP_DISABLED) || NBTUtil.getSharedFlag(entityItem2, FLAG_CLAMP_DISABLED))
			ci.cancel();
	}
}
