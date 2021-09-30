package com.philipzhan.fasterminecarts.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "fasterminecarts")
public class FasterMinecartsConfig implements ConfigData {

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("default")
	public boolean enableMod = true;

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("default")
    public boolean automaticMinecartSlowDownBeforeCurvedRail = true;

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("default")
	public boolean manualMinecartSlowDownOnSoulSand = true;

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("default")
	public boolean furnaceMinecartSlowDownOnHopper = true;
	
	@ConfigEntry.Gui.Tooltip(count = 2)
	@ConfigEntry.BoundedDiscrete(min = 8, max = 30)
	@ConfigEntry.Category("default")
    public int maxSpeed = 24;
}
