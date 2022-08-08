package com.fiddleyowl.fasterminecarts;

import com.fiddleyowl.fasterminecarts.config.FasterMinecartsConfig;

import com.fiddleyowl.fasterminecarts.registry.ModBlocks;
import com.fiddleyowl.fasterminecarts.registry.ModItems;
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
