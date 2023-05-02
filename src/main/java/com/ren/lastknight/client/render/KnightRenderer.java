package com.ren.lastknight.client.render;

import com.ren.lastknight.client.model.KnightModel;
import com.ren.lastknight.common.entity.Knight;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class KnightRenderer extends GeoEntityRenderer<Knight> {
    public KnightRenderer(EntityRendererManager renderManager) {
        super(renderManager, new KnightModel());
    }

    @Override
    public ResourceLocation getEntityTexture(Knight entity) {
        return this.modelProvider.getTextureLocation(entity);
    }
}
