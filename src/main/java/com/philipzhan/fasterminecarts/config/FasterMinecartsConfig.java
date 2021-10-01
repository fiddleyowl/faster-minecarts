package com.philipzhan.fasterminecarts.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.text.TranslatableText;

@Config(name = "fasterminecarts")
public class FasterMinecartsConfig implements ConfigData {

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("general")
	public boolean enableMod = true;

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("general")
    public boolean automaticMinecartSlowDown = true;

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("general")
	public boolean manualMinecartSlowDown = true;

	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Category("general")
	public boolean storageMinecartSlowDown = true;
	
	@ConfigEntry.Gui.Tooltip(count = 2)
	@ConfigEntry.BoundedDiscrete(min = 8, max = 30)
	@ConfigEntry.Category("general")
    public int maxSpeed = 24;

	@ConfigEntry.Gui.Tooltip(count = 4)
	@ConfigEntry.Category("preciseControl")
	public boolean preciseValueMode = true;

	@ConfigEntry.Gui.Tooltip(count = 3)
	@ConfigEntry.Category("preciseControl")
	public int preciseSpeedValue = 5;

	@ConfigEntry.Gui.Tooltip(count = 3)
	@ConfigEntry.Category("preciseControl")
	public int preciseSpeedPercentage = 10;

	@Override
	public void validatePostLoad() throws ValidationException {
		if (preciseSpeedPercentage <= 0) {
			throw new ValidationException(String.valueOf(new TranslatableText("Precise Speed Percentage must be an integer above 0.")));
		}
		if (preciseSpeedValue <= 0) {
			throw new ValidationException(String.valueOf(new TranslatableText("Precise Speed Value must be an integer above 0.")));
		}
	}
}
