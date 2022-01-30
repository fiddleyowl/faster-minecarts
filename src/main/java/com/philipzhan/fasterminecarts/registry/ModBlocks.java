package com.philipzhan.fasterminecarts.registry;

import com.philipzhan.fasterminecarts.Blocks.CustomSpeedRailOneBlock;
import com.philipzhan.fasterminecarts.Blocks.AccelerationRailBlock;
import com.philipzhan.fasterminecarts.Blocks.CustomSpeedRailTwoBlock;
import com.philipzhan.fasterminecarts.Blocks.DecelerationRailBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.philipzhan.fasterminecarts.SupportingFiles.PublicDefinitions.modId;

public class ModBlocks {
    public static final Block ACCELERATION_RAIL_BLOCK = new AccelerationRailBlock(FabricBlockSettings.copyOf(Blocks.POWERED_RAIL).nonOpaque());
    public static final Block DECELERATION_RAIL_BLOCK = new DecelerationRailBlock(FabricBlockSettings.copyOf(Blocks.POWERED_RAIL).nonOpaque());

    public static final Block CUSTOM_SPEED_RAIL_ONE_BLOCK = new CustomSpeedRailOneBlock(FabricBlockSettings.copyOf(ModBlocks.ACCELERATION_RAIL_BLOCK));
    public static final Block CUSTOM_SPEED_RAIL_TWO_BLOCK = new CustomSpeedRailTwoBlock(FabricBlockSettings.copyOf(ModBlocks.DECELERATION_RAIL_BLOCK));

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(modId, "acceleration_rail"), ACCELERATION_RAIL_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(modId, "deceleration_rail"), DECELERATION_RAIL_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(modId, "custom_speed_rail_one"), CUSTOM_SPEED_RAIL_ONE_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(modId, "custom_speed_rail_two"), CUSTOM_SPEED_RAIL_TWO_BLOCK);
    }
}
