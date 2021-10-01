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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.philipzhan.fasterminecarts.config.FasterMinecartsConfig;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

	@Shadow public abstract AbstractMinecartEntity.Type getMinecartType();

	private boolean shouldAccelerate = false;
	private double speedFactor = 1.0;

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

		if (state.getBlock() instanceof AccelerationRailBlock) {
			if (state.get(AccelerationRailBlock.POWERED)) {
				shouldAccelerate = true;
				speedFactor = -1;
			}
		}

		if (state.getBlock() instanceof DecelerationRailBlock) {
			if (state.get(DecelerationRailBlock.POWERED)) {
				shouldAccelerate = false;
				speedFactor = 1;
			}
		}

		// Return if above soul sand blocks or slime blocks
		if (config.manualMinecartSlowDown) {
			if (!(state.getBlock() instanceof AbstractRailBlock) || under instanceof SoulSandBlock) {
				cir.setReturnValue(getDefaultSpeed());
				return;
			}

			if (!(state.getBlock() instanceof AbstractRailBlock) || under instanceof SlimeBlock) {
				cir.setReturnValue(getDefaultSpeed());
				return;
			}
		}

		// Return if storage minecarts are on hopper
		if (config.storageMinecartSlowDown) {
			if (this.getMinecartType().equals(AbstractMinecartEntity.Type.CHEST) || this.getMinecartType().equals(AbstractMinecartEntity.Type.HOPPER)) {
				BlockEntity entity = world.getBlockEntity(getBlockPos().down());
				// Return if above hopper block entity
				if (!(state.getBlock() instanceof AbstractRailBlock) || entity instanceof HopperBlockEntity) {
					cir.setReturnValue(getDefaultSpeed());
					return;
				}
			}
		}

		if (config.automaticMinecartSlowDown) {
			Vec3d v = this.getVelocity();

			if (Math.abs(v.getX()) < 0.5 && Math.abs(v.getZ()) < 0.5) {
				// Return early if at a speed where we can't possibly derail.
				cir.setReturnValue(getHighSpeed());
				return;
			}

			final int additionalOffset = 1;
			final int offset = (int) getHighSpeed() + additionalOffset;

			if (state.getBlock() instanceof AbstractRailBlock abstractRailBlock) {

				RailShape railShape = state.get(abstractRailBlock.getShapeProperty());
				Vec3i nextRailOffset = MinecartUtility.getNextRailOffsetByVelocity(railShape, v);

				if (nextRailOffset == null) {
					// it's a curved rail
					cir.setReturnValue(getSlowSpeed());
					return;
				}

				for (int i = 0; i < offset; i++) {
					RailShape railShapeAtOffset = null;

					railShapeAtOffset = MinecartUtility.getRailShapeAtOffset(
							new Vec3i(nextRailOffset.getX() * i, 0, nextRailOffset.getZ() * i), blockPos, this.world);

					if (railShapeAtOffset == null) {
						cir.setReturnValue(getSlowSpeed());
						return;
					}

					switch (railShapeAtOffset) {
						case SOUTH_EAST:
						case SOUTH_WEST:
						case NORTH_WEST:
						case NORTH_EAST:
							cir.setReturnValue(getSlowSpeed());
							return;
						default:
					}
				}
			}

		}

		// Fallback, accelerate
		if (shouldAccelerate) {
			cir.setReturnValue(getHighSpeed());
		} else {
			cir.setReturnValue(getDefaultSpeed());
		}

	}

	public double getDefaultSpeed() {
		if (this.getMinecartType().equals(AbstractMinecartEntity.Type.FURNACE)) {
			return (this.isTouchingWater() ? 3.0D : 4.0D) / 20.0D;
		} else {
			return (this.isTouchingWater() ? 4.0D : 8.0D) / 20.0D;
		}
	}

	public double getHighSpeed() {
		return config.maxSpeed / 20.0D;
	}

	public double getSlowSpeed() {
		return 0.3;
	}
}
