package com.philipzhan.fasterminecarts.registry;

import com.philipzhan.fasterminecarts.blocks.AccelerationRailBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.philipzhan.fasterminecarts.SupportingFiles.PublicDefinitions.modId;

public class ModBlocks {
    public static final Block ACCELERATION_RAIL_BLOCK = new AccelerationRailBlock(FabricBlockSettings.of(Material.METAL));
    public static final Block DECELERATION_RAIL_BLOCK = new AccelerationRailBlock(FabricBlockSettings.of(Material.METAL));

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(modId, "acceleration_rail"), ACCELERATION_RAIL_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(modId, "deceleration_rail"), DECELERATION_RAIL_BLOCK);
    }
}
