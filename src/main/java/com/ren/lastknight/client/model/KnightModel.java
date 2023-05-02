package com.ren.lastknight.client.model;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.common.entity.Knight;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class KnightModel extends AnimatedTickingGeoModel<Knight> {
    @Override
    public ResourceLocation getModelLocation(Knight object) {
        return new ResourceLocation(TheLastKnight.MOD_ID, "geo/knight.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Knight object) {
        return new ResourceLocation(TheLastKnight.MOD_ID, "textures/entity/knight.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Knight animatable) {
        return new ResourceLocation(TheLastKnight.MOD_ID, "animations/knight.animation.json");
    }
}
