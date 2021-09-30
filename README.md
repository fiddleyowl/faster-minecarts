# Faster Minecarts
This mod aims to balance the speed of vanilla minecarts compared to the other means of transportation available.

If you like the thought of building long railways connecting your world together, but feel that you are hampering yourself because the fastest horse can reach almost double the speed a vanilla minecart can while requiring close to no resources at the same time, then hopefully this mod can remedy that.

## Features
* Increases the maximum speed of all vanilla minecarts from 8 m/s up to 30 m/s.
* Automatic slowdown on corners to prevent potential derailing.
* Acceleration rails and deceleration rails to manually toggle speed.
* Support for per-block slowing with soul sand blocks or hoppers (furnace minecarts only).
* Flexible configuration with ModMenu.

## Recipes
### Acceleration Rails
You can make 6 acceleration rails with 6 diamonds, 1 redstone, and 1 stick.

![](https://github.com/fiddleyowl/faster-minecarts/blob/master/Resources/acceleration-rail-recipe.png?raw=true)

Feeling expensive? Go to YouTube and find tutorials for duplicating them!

### Deceleration Rails
Similar to acceleration rails, replace diamonds with slime balls.

![](https://github.com/fiddleyowl/faster-minecarts/blob/master/Resources/deceleration-rail-recipe.png?raw=true)

## Usage
Demonstration Video: [here](https://raw.githubusercontent.com/fiddleyowl/faster-minecarts/master/Resources/demo.mp4)

Minecarts are not accelerated by default. By placing acceleration rails, minecarts will start accelerating to given maximum speed (configure in ModMenu). By placing deceleration rails, minecarts will run at vanilla speed.
* Acceleration rails set passed minecarts to *acceleration mode*, 2-3 acceleration rails are enough. There's no need to place too many of them.
* Deceleration rails set passed minecarts to *vanilla mode*, 2-3 deceleration rails are enough.
* Acceleration rails and deceleration rails must be powered to work.
* Cross powering is not available. Acceleration rails, deceleration rails, and powered rails must be powered separately.
* Acceleration rails can only accelerate minecarts that are already accelerated to vanilla full speed by powered rails. If you drive a minecart from regular rails onto acceleration rails without being on powered rails, acceleration rails will only set your minecart to *acceleration mode*, but not accelerating your minecart.

Minecarts will automatically slow down at corners or on zigzag tracks to prevent derailing. 
* Your minecart may bounce back on walls when your track goes up. Place deceleration rails in advance to prevent this.
* Running down a hill does not require slowing down.

Placing soul sand blocks or slime blocks beneath rails can force passing minecarts to run at vanilla speed, regardless of whether the minecart is in *vanilla mode* or not. 

Especially for furnace minecarts, hoppers can also force a furnace minecart to run in *vanilla mode*. Other minecarts are not affected by this.


## Requirements
This mod is designed for Minecraft 1.17+, and requires **Fabric API** and **Cloth Config API**.

## Copyright
This mod is a forked and improved version of [prathier/faster-minecarts](https://github.com/prathier/faster-minecarts).

Original author: [neondrp](https://www.curseforge.com/members/neondrp).

## License
This mod is licensed under GPL-3.0 license. 

