package com.ren.lastknight.common.item;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.client.model.armor.KnightHelmetModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class KnightHelmetItem extends ArmorItem {

    private static final Map<EquipmentSlotType, BipedModel<?>> knightHelmetModel = new EnumMap<>(EquipmentSlotType.class);

    public KnightHelmetItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) knightHelmetModel.get(armorSlot);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return TheLastKnight.MOD_ID + ":textures/armor/knight_helmet_layer_1.png";
    }

    @OnlyIn(Dist.CLIENT)
    public static void initArmorModel() {
        knightHelmetModel.put(EquipmentSlotType.HEAD, new KnightHelmetModel(EquipmentSlotType.HEAD,0.5F));
    }
}
