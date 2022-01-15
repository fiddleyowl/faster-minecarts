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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.philipzhan.fasterminecarts.config.FasterMinecartsConfig;

import java.util.ArrayList;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

	@Shadow public abstract AbstractMinecartEntity.Type getMinecartType();

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

		// Check 1: Deceleration Rail has the highest priority.
		if (state.getBlock() instanceof DecelerationRailBlock) {
			if (state.get(DecelerationRailBlock.POWERED)) {
				cir.setReturnValue(getDefaultSpeed());
				return;
			}
		}

		// Check 2: Manual slowdown has the second priority.
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

		// Also Check 2.
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

		// Check 3: Prevent derailing has the third priority.
		if (config.automaticMinecartSlowDown) {
			Vec3d v = this.getVelocity();

			if (Math.abs(v.getX()) < 0.5 && Math.abs(v.getZ()) < 0.5) {
				// Return early if at a speed where we can't possibly derail.
				cir.setReturnValue(getMaximumSpeed());
				return;
			}

			final int additionalOffset = 1;
			final int offset = (int) getMaximumSpeed() + additionalOffset;

			if (state.getBlock() instanceof AbstractRailBlock abstractRailBlock) {

				RailShape railShape = state.get(abstractRailBlock.getShapeProperty());
				Vec3i nextRailOffset = MinecartUtility.getNextRailOffsetByVelocity(railShape, v);

				if (nextRailOffset == null) {
					// it's a curved rail
					cir.setReturnValue(getCornerSpeed());
					return;
				}

				for (int i = 0; i < offset; i++) {
					RailShape railShapeAtOffset = null;

					railShapeAtOffset = MinecartUtility.getRailShapeAtOffset(
							new Vec3i(nextRailOffset.getX() * i, 0, nextRailOffset.getZ() * i), blockPos, this.world);

					if (railShapeAtOffset == null) {
						cir.setReturnValue(getCornerSpeed());
						return;
					}

					switch (railShapeAtOffset) {
						case SOUTH_EAST:
						case SOUTH_WEST:
						case NORTH_WEST:
						case NORTH_EAST:
							cir.setReturnValue(getCornerSpeed());
							return;
						default:
					}
				}
			}

		}

		if (state.getBlock() instanceof AccelerationRailBlock) {
			if (state.get(AccelerationRailBlock.POWERED)) {
				cir.setReturnValue(getMaximumSpeed());
				return;
			}
		}

		if (state.getBlock() instanceof CustomSpeedRailOneBlock) {
			if (state.get(CustomSpeedRailOneBlock.POWERED)) {
				cir.setReturnValue(getCustomSpeedOne());
				return;
			}
		}

		if (state.getBlock() instanceof CustomSpeedRailTwoBlock) {
			if (state.get(CustomSpeedRailTwoBlock.POWERED)) {
				cir.setReturnValue(getCustomSpeedTwo());
			}
		}

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

	public double getMinecartSpeed() {
		return Math.sqrt(Math.pow(this.getVelocity().x,2)+Math.pow(this.getVelocity().y,2)+Math.pow(this.getVelocity().z,2));
	}

	public double getMinecartPlanarSpeed() {
		return Math.sqrt(Math.pow(this.getVelocity().x,2)+Math.pow(this.getVelocity().z,2));
	}

	@Inject(method = "tick", at = @At("TAIL"))
	protected void tick(CallbackInfo ci) {
//		System.out.println("tick");
		BlockPos blockPos = this.getBlockPos();
		BlockState state = this.world.getBlockState(blockPos);
		Block under = world.getBlockState(getBlockPos().down()).getBlock();
		if (under == Blocks.IRON_BLOCK) {
			System.out.println("Iron Block");
		}

		if (under == Blocks.GOLD_BLOCK) {
			System.out.println("Gold Block");
		}

		if (under == Blocks.DIAMOND_BLOCK) {
			System.out.println("Diamond Block");
		}
	}


}
