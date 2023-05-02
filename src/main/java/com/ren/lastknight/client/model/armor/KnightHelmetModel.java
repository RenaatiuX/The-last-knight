package com.ren.lastknight.client.model.armor;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

public class KnightHelmetModel extends BasicArmorModel{
    private final EquipmentSlotType slot;

    public KnightHelmetModel(EquipmentSlotType slot, float modelSize) {
        super(modelSize);
        this.slot = slot;

        bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
        bipedHead.setTextureOffset(45, 27).addBox(-1.0F, -20.5F, -4.0F, 2.0F, 8.0F, 18.0F, 0.0F, false);
        bipedHead.setTextureOffset(0, 34).addBox(-2.0F, -12.5F, -3.0F, 4.0F, 1.0F, 10.0F, 0.0F, false);
        bipedHead.setTextureOffset(77, 79).addBox(-1.0F, -12.5F, 7.0F, 2.0F, 7.0F, 7.0F, 0.0F, false);
        bipedHead.setTextureOffset(43, 79).addBox(-2.0F, -11.5F, 6.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
        bipedHead.setTextureOffset(7, 72).addBox(-1.0F, -12.5F, 1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        ModelRenderer Newhat_r1 = new ModelRenderer(this);
        Newhat_r1.setRotationPoint(0.0F, -6.0F, 1.0F);
        bipedHead.addChild(Newhat_r1);
        setRotationAngle(Newhat_r1, -0.5236F, 0.0F, 0.0F);
        Newhat_r1.setTextureOffset(0, 55).addBox(-5.5F, -0.5F, -8.0F, 11.0F, 5.0F, 9.0F, 0.0F, false);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
