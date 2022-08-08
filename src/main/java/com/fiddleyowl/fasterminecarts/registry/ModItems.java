package com.fiddleyowl.fasterminecarts.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static com.fiddleyowl.fasterminecarts.SupportingFiles.PublicDefinitions.modId;

public class ModItems {

    public static final BlockItem ACCELERATION_RAIL_ITEM = new BlockItem(ModBlocks.ACCELERATION_RAIL_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));
    public static final BlockItem DECELERATION_RAIL_ITEM = new BlockItem(ModBlocks.DECELERATION_RAIL_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));

    public static final BlockItem CUSTOM_SPEED_RAIL_ONE_ITEM = new BlockItem(ModBlocks.CUSTOM_SPEED_RAIL_ONE_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));
    public static final BlockItem CUSTOM_SPEED_RAIL_TWO_ITEM = new BlockItem(ModBlocks.CUSTOM_SPEED_RAIL_TWO_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));


    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(modId, "acceleration_rail"), ACCELERATION_RAIL_ITEM);
        Registry.register(Registry.ITEM, new Identifier(modId, "deceleration_rail"), DECELERATION_RAIL_ITEM);

        Registry.register(Registry.ITEM, new Identifier(modId, "custom_speed_rail_one"), CUSTOM_SPEED_RAIL_ONE_ITEM);
        Registry.register(Registry.ITEM, new Identifier(modId, "custom_speed_rail_two"), CUSTOM_SPEED_RAIL_TWO_ITEM);
    }

}
