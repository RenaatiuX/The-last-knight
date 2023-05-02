package com.ren.lastknight.client.event;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.client.render.KnightRenderer;
import com.ren.lastknight.client.render.LittleKnightRenderer;
import com.ren.lastknight.common.entity.Knight;
import com.ren.lastknight.common.entity.LittleKnight;
import com.ren.lastknight.common.item.KnightHelmetItem;
import com.ren.lastknight.core.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TheLastKnight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        armorModel();
        rendererEntity();
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityInit.KNIGHT.get(), Knight.createAttributes().create());
        event.put(EntityInit.LITTLE_KNIGHT.get(), LittleKnight.createAttributes().create());
    }

    public static void rendererEntity(){
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.KNIGHT.get(), KnightRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.LITTLE_KNIGHT.get(), LittleKnightRenderer::new);

    }

    private static void armorModel(){
        KnightHelmetItem.initArmorModel();
    }
}
