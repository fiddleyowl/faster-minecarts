package com.philipzhan.fasterminecarts.mixin;

import com.philipzhan.fasterminecarts.Blocks.AccelerationRailBlock;
import com.philipzhan.fasterminecarts.Blocks.DecelerationRailBlock;
import com.philipzhan.fasterminecarts.util.MinecartUtility;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.philipzhan.fasterminecarts.config.FasterMinecartsConfig;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

	private boolean shouldAccelerate = false;

	FasterMinecartsConfig config = AutoConfig.getConfigHolder(FasterMinecartsConfig.class).getConfig();

	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "getMaxOffRailSpeed", at = @At("HEAD"), cancellable = true)
	protected void onGetMaxOffRailSpeed(CallbackInfoReturnable<Double> cir) {

		if(!config.enableMod) {
			cir.setReturnValue(getDefaultSpeed());
			return;
		}

		BlockPos blockPos = this.getBlockPos();
		BlockState state = this.world.getBlockState(blockPos);
		Block under = world.getBlockState(getBlockPos().down()).getBlock();

		// Return if above soul sand block
		if (config.manualMinecartSlowDownOnSoulSand) {
			if (!(state.getBlock() instanceof AbstractRailBlock) || under instanceof SoulSandBlock) {
				cir.setReturnValue(getDefaultSpeed());
				return;
			}
		}

		if (state.getBlock() instanceof DecelerationRailBlock) {
			shouldAccelerate = false;
		}

		if (state.getBlock() instanceof AccelerationRailBlock) {
			shouldAccelerate = true;
		}

		if (config.automaticMinecartSlowDownBeforeCurvedRail) {
			Vec3d v = this.getVelocity();

			if (Math.abs(v.getX()) < 0.5 && Math.abs(v.getZ()) < 0.5) {
				// Return early if at a speed where we can't possibly derail.
				cir.setReturnValue(getHighSpeed());
				return;
			}

			final int additionalOffset = 1;
			final int offset = (int) getHighSpeed() + additionalOffset;

			AbstractRailBlock abstractRailBlock = (AbstractRailBlock) state.getBlock();
			RailShape railShape = state.get(abstractRailBlock.getShapeProperty());
			Vec3i nextRailOffset = MinecartUtility.getNextRailOffsetByVelocity(railShape, v);

			if (nextRailOffset == null) {
				// it's a curved rail
				cir.setReturnValue(getDefaultSpeed());
				return;
			}

			for (int i = 0; i < offset; i++) {
				RailShape railShapeAtOffset = null;

				railShapeAtOffset = MinecartUtility.getRailShapeAtOffset(
						new Vec3i(nextRailOffset.getX() * i, 0, nextRailOffset.getZ() * i), blockPos, this.world);

				if (railShapeAtOffset == null) {
					cir.setReturnValue(getDefaultSpeed());
					return;
				}

				switch (railShapeAtOffset) {
					case SOUTH_EAST:
					case SOUTH_WEST:
					case NORTH_WEST:
					case NORTH_EAST:
						cir.setReturnValue(getDefaultSpeed());
						return;
					default:
				}
			}
		}

		// If all above fails, accelerate.
		if (shouldAccelerate) {
			cir.setReturnValue(getHighSpeed());
		} else {
			cir.setReturnValue(getDefaultSpeed());
		}
	}

	public double getDefaultSpeed() {
		return (this.isTouchingWater() ? 4.0D : 8.0D) / 20.0D;
	}

	public double getHighSpeed() {
		return config.maxSpeed / 20.0D;
	}
}
