package com.philipzhan.fasterminecarts.mixin;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.math.BlockPos;
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

		if (config.furnaceMinecartSlowDownOnHopper) {
			BlockPos blockPos = this.getBlockPos();
			BlockState state = this.world.getBlockState(blockPos);
			BlockEntity entity = world.getBlockEntity(getBlockPos().down());
			// Return if above hopper block entity
			if (!(state.getBlock() instanceof AbstractRailBlock) || entity instanceof HopperBlockEntity) {
				cir.setReturnValue(getDefaultSpeed());
				return;
			}
		}

		super.onGetMaxOffRailSpeed(cir);
	}
}
