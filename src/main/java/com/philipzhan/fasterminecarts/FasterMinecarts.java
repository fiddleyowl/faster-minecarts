package com.philipzhan.fasterminecarts;

import com.philipzhan.fasterminecarts.config.FasterMinecartsConfig;

import com.philipzhan.fasterminecarts.registry.ModBlocks;
import com.philipzhan.fasterminecarts.registry.ModItems;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class FasterMinecarts implements ModInitializer {
	@Override
	public void onInitialize() {
		AutoConfig.register(FasterMinecartsConfig.class, GsonConfigSerializer::new);
		ModItems.registerItems();
		ModBlocks.registerBlocks();
	}
}
