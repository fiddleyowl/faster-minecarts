package com.fiddleyowl.fasterminecarts.util;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class MinecartUtility {
	/**
	 * Gets the rail shape at a given block position, based on a given block and an offset.
	 * @param blockOffset Block offset given in Vec3i.
	 * @param blockPos Block position to apply offset to.
	 * @param world The current Minecraft world.
	 * @return Returns the rail shape at the given block position, null if that block is not a rail.
	 */
	public static RailShape getRailShapeAtOffset(Vec3i blockOffset, BlockPos blockPos, World world) {
		BlockState blockState = world.getBlockState(blockPos.add(blockOffset));
		
		if (blockState.getBlock() instanceof AbstractRailBlock abstractRailBlock) {
			return blockState.get(abstractRailBlock.getShapeProperty());
		} else {
			return null;
		}
	}

	/**
	 * Gets the running direction of a minecart, given in Vec3i.
	 * @param railShape Rail shape where the minecart is on. Should not be a curved rail or an ascending rail.
	 * @param velocity Current minecart velocity, in Vec3d.
	 * @return Returns a Vec3i denoting the running direction. Valid return values are (+-1, 0, +-1). Only one number can be non-zero. Returns null if the given rail is curved or ascending.
	 */
	public static Vec3i getMinecartRunningDirection(RailShape railShape, Vec3d velocity) {
		
		if (railShape == RailShape.EAST_WEST) {
			double x = velocity.getX();
//			x = (x > 0) ? 1 : ((x == 0) ? 0 : -1);

			if (x > 0) {
				x = 1;
			} else if (x == 0) {
				x = 0;
			} else {
				x = -1;
			}

			return new Vec3i(x, 0, 0);

		} else if (railShape == RailShape.NORTH_SOUTH) {

			double z = velocity.getZ();
//			z = (z > 0) ? 1 : ((z == 0) ? 0 : -1);
			if (z > 0) {
				z = 1;
			} else if (z == 0) {
				z = 0;
			} else {
				z = -1;
			}
			return new Vec3i(0, 0, z);
		}
		return null;
	}
}
