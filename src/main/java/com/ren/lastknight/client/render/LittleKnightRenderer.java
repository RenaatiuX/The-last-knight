package com.ren.lastknight.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.ren.lastknight.client.model.LittleKnightModel;
import com.ren.lastknight.common.entity.LittleKnight;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class LittleKnightRenderer extends GeoEntityRenderer<LittleKnight> {
    public LittleKnightRenderer(EntityRendererManager renderManager) {
        super(renderManager, new LittleKnightModel());
    }


    @Override
    public void render(LittleKnight entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entity.getSpawn() && GeckoLibUtil.getControllerForID(entity.getFactory(), entity.getEntityId(), "controller").getAnimationState() == AnimationState.Transitioning)
            stack.scale(0f, 0f, 0f);
        else
            stack.scale(1f, 1f, 1f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(LittleKnight entity) {
        return this.modelProvider.getTextureLocation(entity);
    }
}
