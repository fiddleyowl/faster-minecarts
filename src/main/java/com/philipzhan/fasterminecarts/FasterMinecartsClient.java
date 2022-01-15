package com.philipzhan.fasterminecarts;

import com.philipzhan.fasterminecarts.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class FasterMinecartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // For transparency
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.ACCELERATION_RAIL_BLOCK, ModBlocks.DECELERATION_RAIL_BLOCK, ModBlocks.CUSTOM_SPEED_RAIL_ONE_BLOCK, ModBlocks.CUSTOM_SPEED_RAIL_TWO_BLOCK);
    }
}
