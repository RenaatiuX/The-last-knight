package com.ren.lastknight.core.group;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.core.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

import java.util.function.Supplier;

public class TheLastKnightGroup extends ItemGroup {

    public static final ItemGroup ITEMS = new TheLastKnightGroup("items", ItemInit.KNIGHT_HELMET::get);
    private final Supplier<IItemProvider> icon;
    private TheLastKnightGroup(String name, Supplier<IItemProvider> icon) {
        super(TheLastKnight.MOD_ID + "." + name);
        this.icon = icon;
    }

    @Override
    public ItemStack createIcon() {
        return this.icon.get().asItem().getDefaultInstance();
    }
}
