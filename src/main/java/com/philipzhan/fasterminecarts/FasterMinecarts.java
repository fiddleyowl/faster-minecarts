package com.philipzhan.fasterminecarts;

import com.philipzhan.fasterminecarts.config.FasterMinecartsConfig;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class FasterMinecarts implements ModInitializer {
	@Override
	public void onInitialize() {
		AutoConfig.register(FasterMinecartsConfig.class, GsonConfigSerializer::new);
	}
}
