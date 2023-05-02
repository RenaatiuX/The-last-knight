package com.ren.lastknight.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.ren.lastknight.client.model.LittleKnightModel;
import com.ren.lastknight.common.entity.LittleKnight;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LittleKnightRenderer extends GeoEntityRenderer<LittleKnight> {
    public LittleKnightRenderer(EntityRendererManager renderManager) {
        super(renderManager, new LittleKnightModel());
    }

    @Override
    public ResourceLocation getEntityTexture(LittleKnight entity) {
        return this.modelProvider.getTextureLocation(entity);
    }
}
