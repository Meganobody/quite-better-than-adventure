package quitebetter.mixin.core;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.data.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface IEntityAccessor {
	@Accessor("entityData")
	SynchedEntityData getEntityData();
}
