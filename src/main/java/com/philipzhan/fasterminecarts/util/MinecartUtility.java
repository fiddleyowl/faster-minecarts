package com.philipzhan.fasterminecarts.util;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class MinecartUtility {
	/*
	 * Returns null if the block at the given offset is not a rail
	 */
	public static RailShape getRailShapeAtOffset(Vec3i railOffset, BlockPos blockPos, World world) {
		
		BlockState state = world.getBlockState(blockPos.add(railOffset));
		
		if (state.getBlock() instanceof AbstractRailBlock) {
			AbstractRailBlock abstractRailBlock = (AbstractRailBlock)state.getBlock();
			RailShape railShape = (RailShape) state.get(abstractRailBlock.getShapeProperty());
			return railShape;
		} else {
			return null;
		}
	}
	
	/*
	 * Returns null if the rail is a curved rail
	 */
	public static Vec3i getNextRailOffsetByVelocity(RailShape railShape, Vec3d velocity) {
		
		if (railShape == RailShape.EAST_WEST) {
			double x = velocity.getX();
			x = (x > 0) ? 1 : ((x == 0) ? 0 : -1);
			return new Vec3i(x, 0, 0);
		} else if (railShape == RailShape.NORTH_SOUTH) {
			double z = velocity.getZ();
			z = (z > 0) ? 1 : ((z == 0) ? 0 : -1);
			return new Vec3i(0, 0, z);
		}
		return null;
	}
}
