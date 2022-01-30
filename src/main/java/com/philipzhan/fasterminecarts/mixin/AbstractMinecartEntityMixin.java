package com.philipzhan.fasterminecarts.mixin;

import com.philipzhan.fasterminecarts.Blocks.AccelerationRailBlock;
import com.philipzhan.fasterminecarts.Blocks.CustomSpeedRailOneBlock;
import com.philipzhan.fasterminecarts.Blocks.CustomSpeedRailTwoBlock;
import com.philipzhan.fasterminecarts.Blocks.DecelerationRailBlock;
import com.philipzhan.fasterminecarts.util.MinecartUtility;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.MinecraftClient;
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
			System.out.println("Not Enabled.");
			System.out.println("49 Return value: " + cir.getReturnValue());
			return;
		}

		BlockPos blockPosition = this.getBlockPos();
		BlockState blockCurrentState = this.world.getBlockState(blockPosition);
		Block blockCurrent = blockCurrentState.getBlock();
		BlockState blockBelowState = this.world.getBlockState(blockPosition.down());
		Block blockBelow = blockBelowState.getBlock();

		// Check 1: Deceleration Rail has the highest priority. This resets minecart to vanilla mode.
		if (blockCurrent instanceof DecelerationRailBlock) {
			if (blockCurrentState.get(DecelerationRailBlock.POWERED)) {
				System.out.println("Deceleration Rail");
				System.out.println("63 Return value: " + cir.getReturnValue());
				shouldAccelerate = false;
				return;
			}
		}

		// Check 2: Manual slowdown has the second priority.
		if (config.manualMinecartSlowDown) {
			if (blockBelow instanceof SoulSandBlock || blockBelow instanceof SlimeBlock || blockBelow instanceof HoneyBlock) {
				System.out.println("Soul Sand");
				System.out.println("73 Return value: " + cir.getReturnValue());
				return;
			}
		}

		// Also Check 2.
		if (config.storageMinecartSlowDown) {
			if (this.getMinecartType().equals(AbstractMinecartEntity.Type.CHEST) || this.getMinecartType().equals(AbstractMinecartEntity.Type.HOPPER)) {
				BlockEntity entity = world.getBlockEntity(getBlockPos().down());
				// Return if above hopper block entity
				if (!(blockCurrentState.getBlock() instanceof AbstractRailBlock) || entity instanceof HopperBlockEntity) {
					System.out.println("Hopper");
					System.out.println("85 Return value: " + cir.getReturnValue());
					return;
				}
			}
		}

		// Check 3: Prevent derailing has the third priority.
		derail_check:
		if (config.automaticMinecartSlowDown) {
			Vec3d v = this.getVelocity();

			if (Math.abs(v.getX()) < 0.5 && Math.abs(v.getZ()) < 0.5) {
				// Speed is too low, no need to check for derailing.
				break derail_check;
			}

			System.out.println(v);

			int additionalOffset = 1;

			if (config.automaticAscendingSlowDown) {
				// additionalOffset determines how many blocks ahead to check.
				// To prevent bouncing back by ascending rails, check distance must be higher.
				// This number can be changed from mod settings.
				additionalOffset += config.ascendingSlowDownCheckDistance;
			}


			final int offset = (int) getMaximumSpeed() + additionalOffset;

			System.out.println("Current block: " + blockCurrent.getName().toString());
			System.out.println("Block below: " + blockBelow.getName().toString());

			if (blockCurrent instanceof AbstractRailBlock abstractRailBlock) {
				RailShape railShape = blockCurrentState.get(abstractRailBlock.getShapeProperty());
				switch (railShape) {
					case SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST -> {
						cir.setReturnValue(getCornerSpeed());
						System.out.println("Curved Rail");
						System.out.println("127 Return value: " + cir.getReturnValue());
						return;
					}
					case ASCENDING_EAST, ASCENDING_NORTH, ASCENDING_SOUTH, ASCENDING_WEST -> {
						System.out.println("Ascending Rail");
						System.out.println("132 Return value: " + cir.getReturnValue());
						return;
					}
				}

//				Vec3i nextRailOffset = MinecartUtility.getNextRailOffsetByVelocity(railShape, v);
//				// Next rail offset: valid values are (1,0,0), (-1,0,0), (0,0,1), or (0,0,-1).
//
//				System.out.println(nextRailOffset);
//
//				if (nextRailOffset == null) {
//					// nextRailOffset is null means current railShape is curved.
//
//				}

				// Current railShape is not curved.

				Vec3i nextRailOffset = MinecartUtility.getNextRailOffsetByVelocity(railShape, v);

				if (nextRailOffset == null) {
					// Should not happen because "current rail is curved" is already short-circuited. Just in case.
					cir.setReturnValue(getCornerSpeed());
					System.out.println("Curved Rail");
					System.out.println("151 Return value: " + cir.getReturnValue());
					return;
				}

				for (int i = 0; i < offset; i++) {
					// offset: how many blocks you can go in one second.
					RailShape railShapeAtOffset = null;

					railShapeAtOffset = MinecartUtility.getRailShapeAtOffset(
							new Vec3i(nextRailOffset.getX() * i, 0, nextRailOffset.getZ() * i), blockPosition, this.world);

					System.out.println("Offset: " + i + ", railShapeAtOffset: " + railShapeAtOffset);

					if (railShapeAtOffset == null) {
						cir.setReturnValue(getCornerSpeed());
						System.out.println("Curved Rail");
						System.out.println("167 Return value: " + cir.getReturnValue());
						return;
					}

					switch (railShapeAtOffset) {
						case SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST -> {
							System.out.println("Curved Rail");
							System.out.println("171 Return value: " + cir.getReturnValue());
							return;
						}
						case ASCENDING_EAST, ASCENDING_NORTH, ASCENDING_SOUTH, ASCENDING_WEST -> {
							System.out.println("Ascending Rail");
							System.out.println("177 Return value: " + cir.getReturnValue());
							return;
						}
					}
				}
			}

			if (blockCurrent instanceof AirBlock && blockBelow instanceof AbstractRailBlock abstractRailBlock) {
				RailShape railShape = blockBelowState.get(abstractRailBlock.getShapeProperty());
				switch (railShape) {
					case ASCENDING_EAST, ASCENDING_NORTH, ASCENDING_SOUTH, ASCENDING_WEST -> {
						// Current is air, block below is rail, we are moving, this probably means we are going up or down.
						// getVelocity() returned velocity y values are always 0.2 or something, regardless of the true minecart moving status.
						System.out.println("Ascending Rail");
						System.out.println("193 Return value: " + cir.getReturnValue());
						return;
					}
				}
			}
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

		System.out.println("Final Return value: " + cir.getReturnValue());
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
