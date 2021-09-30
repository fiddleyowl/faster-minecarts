package com.philipzhan.fasterminecarts;

import com.philipzhan.fasterminecarts.config.FasterMinecartsConfig;

import com.philipzhan.fasterminecarts.registry.ModBlocks;
import com.philipzhan.fasterminecarts.registry.ModItems;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class FasterMinecarts implements ModInitializer {
	@Override
	public void onInitialize() {
		AutoConfig.register(FasterMinecartsConfig.class, GsonConfigSerializer::new);
		ModItems.registerItems();
		ModBlocks.registerBlocks();

	}

}
