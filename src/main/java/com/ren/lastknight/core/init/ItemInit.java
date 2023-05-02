package com.ren.lastknight.core.init;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.common.enums.TheLastKnightArmorMaterial;
import com.ren.lastknight.common.item.KnightHelmetItem;
import com.ren.lastknight.core.group.TheLastKnightGroup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheLastKnight.MOD_ID);

    public static final RegistryObject<Item> KNIGHT_HELMET = ITEMS.register("knight_helmet",
            () -> new KnightHelmetItem(TheLastKnightArmorMaterial.KNIGHT_HELMET, EquipmentSlotType.HEAD, new Item.Properties().group(TheLastKnightGroup.ITEMS)));

    public static final RegistryObject<Item> KNIGHT_SWORD = ITEMS.register("knight_sword",
            () -> new SwordItem(ItemTier.NETHERITE, 3, -2.4F, new Item.Properties().isImmuneToFire().group(TheLastKnightGroup.ITEMS)));

    public static final RegistryObject<Item> KNIGHT_SPAWN_EGG = ITEMS.register("knight_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityInit.KNIGHT, 4008243, 10637382, new Item.Properties().group(TheLastKnightGroup.ITEMS)));
    public static final RegistryObject<Item> LITTLE_KNIGHT_SPAWN_EGG = ITEMS.register("little_knight_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityInit.LITTLE_KNIGHT, 4008243, 10637382, new Item.Properties().group(TheLastKnightGroup.ITEMS)));
}
