# Faster Minecarts
[![build](https://github.com/fiddleyowl/faster-minecarts/actions/workflows/build.yml/badge.svg)](https://github.com/fiddleyowl/faster-minecarts/actions/workflows/build.yml)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)


[![](http://cf.way2muchnoise.eu/full_532511_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/faster-minecarts-2)
[![](http://cf.way2muchnoise.eu/versions/532511.svg)](https://www.curseforge.com/minecraft/mc-mods/faster-minecarts-2)

[![Requires Fabric API](https://i.ibb.co/16LQcDJ/Requires-Fabric-API.png)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

This mod aims to balance the speed of vanilla minecarts compared to the other means of transportation available.

If you like the thought of building long railways connecting your world together, but feel that you are hampering yourself because the fastest horse can reach almost double the speed a vanilla minecart can while requiring close to no resources at the same time, then hopefully this mod can remedy that.

This is a forked and remade version of [faster-minecarts](https://www.curseforge.com/minecraft/mc-mods/faster-minecarts) by [neondrp](https://www.curseforge.com/members/neondrp).

If you find issues like derailing, please open an issue on GitHub and describe what you builds look like. Also, feel free to fork my repository and submit a pull request to let me know your ideas!

Demo video on Streamable: [https://streamable.com/2g4slc](https://streamable.com/2g4slc)

## Features
* Increases the maximum speed of all vanilla minecarts from 8 m/s to up to 30 m/s.
* Automatic slowdown before corners and hills to prevent derailing.
* Acceleration rails, deceleration rails, and two custom speed rails to manually toggle speed.
* Support for per-block slowing with soul sand blocks, slime blocks, honey blocks, or hoppers (storage minecarts only).
* Flexible configuration with ModMenu.

## Recipes
### Acceleration Rail
You can make 6 acceleration rails with 6 diamonds, 1 redstone, and 1 stick.

![](https://i.ibb.co/Wxhbqh1/acceleration-rail-recipe.png)

Feeling expensive? Go to YouTube and find tutorials for duplicating them!

### Deceleration Rail
Similar to acceleration rails, replace diamonds with slime balls.

![](https://i.ibb.co/nMWhKJp/deceleration-rail-recipe.png)

### Lapis Lazuli Rail (Custom Speed Rail One)

![](https://i.ibb.co/S6v2xYG/custom-speed-rail-one-recipe.png)

### Copper Rail (Custom Speed Rail Two)

![](https://i.ibb.co/rdw8Bvj/custom-speed-rail-two-recipe.png)

## Usage
Minecarts are not accelerated by default. By placing acceleration rails, minecarts will start accelerating to given maximum speed (configurable in ModMenu).
* Acceleration rails set passed minecarts to acceleration mode, 2-3 acceleration rails are enough (3 recommended to make sure the rails work). There's no need to place too many of them.
* Custom speed rails work exactly like acceleration rails, just with different maximum speeds. You can configure the speeds in ModMenu. The speeds can even be lower than the vanilla limit of 8 m/s.
* Deceleration rails set passed minecarts to vanilla mode, 2-3 deceleration rails are enough. If you set one of the custom speeds to a value below vanilla speed, deceleration rails will revert the speed limit to 8 (does acceleration in this case).
* Acceleration rails, deceleration rails, and custom speed rails must be powered to work. Cross powering is not available. Different rails must be powered separately.
* Acceleration rails and custom speed rails only does the speed setting. They don't actually accelerate minecarts. You still need to use vanilla powered rails to accelerate minecarts.

Minecarts will automatically slow down before corners, on zigzag tracks, or before hills to prevent derailing.
* ~~Your minecart may bounce back on walls when your track goes up. Place deceleration rails in advance to prevent this.~~
* ~~Running down a hill does not require slowing down.~~
* The 2.2.0 update brings automatic slowdown before hills. You can turn off this feature in mod config menu.
* Speeding up on diagonal tracks is scheduled. May come in a future update.
* If you are still experiencing derailing, increase Slowdown Check Distance and try again.

Placing soul sand blocks or slime blocks beneath rails can force passing minecarts to run at vanilla speed, regardless of whether the minecart is in *vanilla mode* or not. 

Especially for storage minecarts(chest, hopper), hoppers can also force a furnace minecart to run in *vanilla mode*. Other minecarts are not affected by this.

## Requirements
This mod is designed for Minecraft 1.17+, and requires **Fabric API** and **Cloth Config API**.

## Copyright
This mod is a forked and improved version of [faster-minecarts](http://www.curseforge.com/minecraft/mc-mods/faster-minecarts).

Original author: [neondrp](https://www.curseforge.com/members/neondrp).

This mod is only published on [CurseForge](http://www.curseforge.com/minecraft/mc-mods/faster-minecarts-2).

## License
This mod is licensed under GPL-3.0 license. 

