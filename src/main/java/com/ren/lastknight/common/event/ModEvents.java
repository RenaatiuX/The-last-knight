package com.ren.lastknight.common.event;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.common.entity.Knight;
import com.ren.lastknight.core.init.EntityInit;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TheLastKnight.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void spawnListStructure(StructureSpawnListGatherEvent event){
        if (event.getStructure() == Structure.BASTION_REMNANT)
            event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityInit.KNIGHT.get(), 1, 0, 1));
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        EntitySpawnPlacementRegistry.register(EntityInit.KNIGHT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Knight::canSpawn);
    }
}
