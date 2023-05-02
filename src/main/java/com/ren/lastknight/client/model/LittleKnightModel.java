package com.ren.lastknight.client.model;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.common.entity.LittleKnight;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class LittleKnightModel extends AnimatedTickingGeoModel<LittleKnight> {
    @Override
    public ResourceLocation getModelLocation(LittleKnight object) {
        return new ResourceLocation(TheLastKnight.MOD_ID, "geo/little_knight.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(LittleKnight object) {
        return new ResourceLocation(TheLastKnight.MOD_ID, "textures/entity/little_knight.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(LittleKnight animatable) {
        return new ResourceLocation(TheLastKnight.MOD_ID, "animations/little_knight.animation.json");
    }
}
