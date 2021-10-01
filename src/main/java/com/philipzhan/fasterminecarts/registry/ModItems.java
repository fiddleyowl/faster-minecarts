package com.philipzhan.fasterminecarts.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static com.philipzhan.fasterminecarts.SupportingFiles.PublicDefinitions.modId;

public class ModItems {

    public static final BlockItem ACCELERATION_RAIL_ITEM = new BlockItem(ModBlocks.ACCELERATION_RAIL_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));
    public static final BlockItem DECELERATION_RAIL_ITEM = new BlockItem(ModBlocks.DECELERATION_RAIL_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));

    public static final BlockItem PRECISE_ACCELERATION_RAIL_ITEM = new BlockItem(ModBlocks.PRECISE_ACCELERATION_RAIL_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));
    public static final BlockItem PRECISE_DECELERATION_RAIL_ITEM = new BlockItem(ModBlocks.PRECISE_DECELERATION_RAIL_BLOCK, new Item.Settings().rarity(Rarity.COMMON).group(ItemGroup.TRANSPORTATION));


    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(modId, "acceleration_rail"), ACCELERATION_RAIL_ITEM);
        Registry.register(Registry.ITEM, new Identifier(modId, "deceleration_rail"), DECELERATION_RAIL_ITEM);

        Registry.register(Registry.ITEM, new Identifier(modId, "precise_acceleration_rail"), PRECISE_ACCELERATION_RAIL_ITEM);
        Registry.register(Registry.ITEM, new Identifier(modId, "precise_deceleration_rail"), PRECISE_DECELERATION_RAIL_ITEM);
    }

}
