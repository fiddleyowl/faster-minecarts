package com.philipzhan.fasterminecarts.mixin;

import com.philipzhan.fasterminecarts.Blocks.AccelerationRailBlock;
import com.philipzhan.fasterminecarts.Blocks.CustomSpeedRailOneBlock;
import com.philipzhan.fasterminecarts.Blocks.CustomSpeedRailTwoBlock;
import com.philipzhan.fasterminecarts.Blocks.DecelerationRailBlock;
import static com.philipzhan.fasterminecarts.util.MinecartUtility.*;
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

	private double shouldAccelerateTo = getDefaultSpeed();
	private boolean shouldAccelerate = false;

	FasterMinecartsConfig config = AutoConfig.getConfigHolder(FasterMinecartsConfig.class).getConfig();

	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "getMaxOffRailSpeed", at = @At("RETURN"), cancellable = true)
	protected void onGetMaxOffRailSpeed(CallbackInfoReturnable<Double> cir) {

		if(!config.enableMod) {
//			System.out.println("Not Enabled.");
//			System.out.println("49 Return value: " + cir.getReturnValue());
			return;
		}

		BlockPos blockPosition = this.getBlockPos();
		BlockState blockCurrentState = this.world.getBlockState(blockPosition);
		Block blockCurrent = blockCurrentState.getBlock();
		BlockState blockBelowState = this.world.getBlockState(blockPosition.down());
		Block blockBelow = blockBelowState.getBlock();

		// Check 1: Deceleration Rail has the highest priority. This resets all custom settings.
		if (blockCurrent instanceof DecelerationRailBlock) {
			if (blockCurrentState.get(DecelerationRailBlock.POWERED)) {
//				System.out.println("Deceleration Rail");
//				System.out.println("63 Return value: " + cir.getReturnValue());
				shouldAccelerate = false;
				return;
			}
		}

		// Check 2: Manual slowdown has the second priority.
		if (config.manualMinecartSlowDown) {
			if (blockBelow instanceof SoulSandBlock || blockBelow instanceof SlimeBlock || blockBelow instanceof HoneyBlock) {
//				System.out.println("Soul Sand");
//				System.out.println("73 Return value: " + cir.getReturnValue());
				return;
			}
		}

		// Also Check 2.
		if (config.storageMinecartSlowDown) {
			if (this.getMinecartType().equals(AbstractMinecartEntity.Type.CHEST) || this.getMinecartType().equals(AbstractMinecartEntity.Type.HOPPER)) {
				BlockEntity entity = world.getBlockEntity(getBlockPos().down());
				// Return if above hopper block entity
				if (!(blockCurrentState.getBlock() instanceof AbstractRailBlock) || entity instanceof HopperBlockEntity) {
//					System.out.println("Hopper");
//					System.out.println("85 Return value: " + cir.getReturnValue());
					return;
				}
			}
		}

		// Check 3: Prevent derailing has the third priority.
		derail_check:
		if (config.automaticMinecartSlowDown) {
			Vec3d minecartVelocity = this.getVelocity();

			if (Math.abs(minecartVelocity.getX()) < 0.5 && Math.abs(minecartVelocity.getZ()) < 0.5) {
				// Speed is too low, no need to check for derailing.
				break derail_check;
			}

			final int checkDistance = (int) getMaximumSpeed() + config.slowdownCheckDistance;

			if (blockCurrent instanceof AirBlock && blockBelow instanceof AbstractRailBlock abstractRailBlock) {
				// Current is air, block below is rail, we are moving, this probably means we are going up or down.
				// getVelocity() returned velocity y values are always 0.2 or something, regardless of the true minecart moving status.
				RailShape currentRailShape = blockBelowState.get(abstractRailBlock.getShapeProperty());
				switch (currentRailShape) {
					case ASCENDING_EAST, ASCENDING_NORTH, ASCENDING_SOUTH, ASCENDING_WEST -> {
//						System.out.println("Ascending Rail");
//						System.out.println("109 Return value: " + cir.getReturnValue());
						if (config.ascendingSlowdown) {
							return;
						}
					}
				}
			}

			if (blockCurrent instanceof AbstractRailBlock abstractRailBlock) {
				RailShape currentRailShape = blockCurrentState.get(abstractRailBlock.getShapeProperty());
				switch (currentRailShape) {
					case SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST -> {
						// Current rail is curved.
						cir.setReturnValue(getCornerSpeed());
//						System.out.println("Curved Rail");
//						System.out.println("121 Return value: " + cir.getReturnValue());
						return;
					}
					case ASCENDING_EAST, ASCENDING_NORTH, ASCENDING_SOUTH, ASCENDING_WEST -> {
						// This never calls... Just keep it here.
//						System.out.println("Ascending Rail");
//						System.out.println("132 Return value: " + cir.getReturnValue());
						if (config.ascendingSlowdown) {
							return;
						}
					}
				}

				// Current rail is not curved.

				Vec3i runningDirection = getMinecartRunningDirection(currentRailShape, minecartVelocity);

				for (int i = 0; i < checkDistance; i++) {
					RailShape railShapeAtOffset;

					if (runningDirection == null) {
						return;
					}

					railShapeAtOffset = getRailShapeAtOffset(
							new Vec3i(runningDirection.getX() * i, 0, runningDirection.getZ() * i), blockPosition, this.world);

//					System.out.println("Offset: " + i + ", railShapeAtOffset: " + railShapeAtOffset);

					if (railShapeAtOffset == null) {
						// Sure to derail.
//						System.out.println("Not Rail");
//						System.out.println("167 Return value: " + cir.getReturnValue());
						break derail_check;
					}

					switch (railShapeAtOffset) {
						case SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST -> {
//							System.out.println("Curved Rail");
//							System.out.println("171 Return value: " + cir.getReturnValue());
							return;
						}
						case ASCENDING_EAST, ASCENDING_NORTH, ASCENDING_SOUTH, ASCENDING_WEST -> {
//							System.out.println("Ascending Rail");
//							System.out.println("177 Return value: " + cir.getReturnValue());
							if (config.ascendingSlowdown) {
								return;
							}
						}
					}
				}
			}
			// This is the end of derail_check if block.
		}

		if (blockCurrentState.getBlock() instanceof AccelerationRailBlock) {
			if (blockCurrentState.get(AccelerationRailBlock.POWERED)) {
				shouldAccelerate = true;
				shouldAccelerateTo = getMaximumSpeed();
			}
		}

		if (blockCurrentState.getBlock() instanceof CustomSpeedRailOneBlock) {
			if (blockCurrentState.get(CustomSpeedRailOneBlock.POWERED)) {
				shouldAccelerate = true;
				shouldAccelerateTo = getCustomSpeedOne();
			}
		}

		if (blockCurrentState.getBlock() instanceof CustomSpeedRailTwoBlock) {
			if (blockCurrentState.get(CustomSpeedRailTwoBlock.POWERED)) {
				shouldAccelerate = true;
				shouldAccelerateTo = getCustomSpeedTwo();
			}
		}

		if (shouldAccelerate) {
			cir.setReturnValue(shouldAccelerateTo);
		}

//		System.out.println("Final Return value: " + cir.getReturnValue());
	}

	public double getDefaultSpeed() {
		if (this.getMinecartType().equals(AbstractMinecartEntity.Type.FURNACE)) {
			return (this.isTouchingWater() ? 3.0D : 4.0D) / 20.0D;
		} else {
			return (this.isTouchingWater() ? 4.0D : 8.0D) / 20.0D;
		}
	}

	public double getMaximumSpeed() {
		return config.maxSpeed / 20.0D;
	}

	public double getCustomSpeedOne() {
		return config.customSpeedOne / 20.0D;
	}

	public double getCustomSpeedTwo() {
		return config.customSpeedTwo / 20.0D;
	}

	public double getCornerSpeed() {
		return 0.3D;
	}

}
