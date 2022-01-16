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

	FasterMinecartsConfig config = AutoConfig.getConfigHolder(FasterMinecartsConfig.class).getConfig();

	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "getMaxOffRailSpeed", at = @At("RETURN"), cancellable = true)
	protected void onGetMaxOffRailSpeed(CallbackInfoReturnable<Double> cir) {

		if(!config.enableMod) {
			cir.setReturnValue(getDefaultSpeed());
			System.out.println("Not Enabled.");
			System.out.println("52 Return value: " + cir.getReturnValue());
			return;
		}

		BlockPos blockPos = this.getBlockPos();
		BlockState state = this.world.getBlockState(blockPos);
		Block under = world.getBlockState(getBlockPos().down()).getBlock();

		// Check 1: Deceleration Rail has the highest priority.
		if (state.getBlock() instanceof DecelerationRailBlock) {
			if (state.get(DecelerationRailBlock.POWERED)) {
				cir.setReturnValue(getDefaultSpeed());
				System.out.println("Deceleration Rail");
				System.out.println("65 Return value: " + cir.getReturnValue());
				return;
			}
		}

		// Check 2: Manual slowdown has the second priority.
		if (config.manualMinecartSlowDown) {
			if (under instanceof SoulSandBlock) {
				cir.setReturnValue(getDefaultSpeed());
				System.out.println("Soul Sand");
				System.out.println("75 Return value: " + cir.getReturnValue());
				return;
			}

			if (under instanceof SlimeBlock) {
				cir.setReturnValue(getDefaultSpeed());
				System.out.println("Slime Block");
				System.out.println("82 Return value: " + cir.getReturnValue());
				return;
			}
		}

		// Also Check 2.
		if (config.storageMinecartSlowDown) {
			if (this.getMinecartType().equals(AbstractMinecartEntity.Type.CHEST) || this.getMinecartType().equals(AbstractMinecartEntity.Type.HOPPER)) {
				BlockEntity entity = world.getBlockEntity(getBlockPos().down());
				// Return if above hopper block entity
				if (!(state.getBlock() instanceof AbstractRailBlock) || entity instanceof HopperBlockEntity) {
					cir.setReturnValue(getDefaultSpeed());
					System.out.println("Hopper");
					System.out.println("95 Return value: " + cir.getReturnValue());
					return;
				}
			}
		}

		// Check 3: Prevent derailing has the third priority.
		if (config.automaticMinecartSlowDown) {
			Vec3d v = this.getVelocity();

			final int additionalOffset = 1;
			final int offset = (int) getMaximumSpeed() + additionalOffset;

			if (state.getBlock() instanceof AbstractRailBlock abstractRailBlock) {

				RailShape railShape = state.get(abstractRailBlock.getShapeProperty());
				Vec3i nextRailOffset = MinecartUtility.getNextRailOffsetByVelocity(railShape, v);
				// Next rail offset: valid values are (1,0,0), (-1,0,0), (0,0,1), or (0,0,-1).

				if (nextRailOffset == null) {
					// nextRailOffset is null means current railShape is curved.
					cir.setReturnValue(getCornerSpeed());
					System.out.println("Curved Rail");
					System.out.println("124 Return value: " + cir.getReturnValue());
					return;
				}

				// Current railShape is not curved.

				for (int i = 0; i < offset; i++) {
					// offset: how many blocks you can go in one second.
					RailShape railShapeAtOffset = null;

					railShapeAtOffset = MinecartUtility.getRailShapeAtOffset(
							new Vec3i(nextRailOffset.getX() * i, 0, nextRailOffset.getZ() * i), blockPos, this.world);

					if (railShapeAtOffset == null) {
						cir.setReturnValue(getCornerSpeed());
						System.out.println("Curved Rail");
						System.out.println("137 Return value: " + cir.getReturnValue());
						return;
					}

					switch (railShapeAtOffset) {
						case SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST, ASCENDING_EAST, ASCENDING_NORTH, ASCENDING_SOUTH, ASCENDING_WEST -> {
							cir.setReturnValue(getCornerSpeed());
							System.out.println("Curved Rail");
							System.out.println("148 Return value: " + cir.getReturnValue());
							return;
						}
						default -> {
						}
					}
				}
			}
		}

		if (state.getBlock() instanceof AccelerationRailBlock) {
			if (state.get(AccelerationRailBlock.POWERED)) {
				shouldAccelerateTo = getMaximumSpeed();
			}
		}

		if (state.getBlock() instanceof CustomSpeedRailOneBlock) {
			if (state.get(CustomSpeedRailOneBlock.POWERED)) {
				shouldAccelerateTo = getCustomSpeedOne();
			}
		}

		if (state.getBlock() instanceof CustomSpeedRailTwoBlock) {
			if (state.get(CustomSpeedRailTwoBlock.POWERED)) {
				shouldAccelerateTo = getCustomSpeedTwo();
			}
		}

		cir.setReturnValue(shouldAccelerateTo);

		System.out.println("Fallback Return value: " + cir.getReturnValue());

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
		return 0.3;
	}

}
