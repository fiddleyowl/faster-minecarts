package com.philipzhan.fasterminecarts.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.world.World;

@Mixin(FurnaceMinecartEntity.class)
public abstract class FurnaceMinecartEntityMixin extends AbstractMinecartEntityMixin {

	public FurnaceMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "getMaxOffRailSpeed", at = @At("HEAD"), cancellable = true)
	protected void onGetMaxOffRailSpeed(CallbackInfoReturnable<Double> cir) {
		super.onGetMaxOffRailSpeed(cir);
	}
}
