package com.ren.lastknight.core.init;

import com.ren.lastknight.TheLastKnight;
import com.ren.lastknight.common.entity.Knight;
import com.ren.lastknight.common.entity.LittleKnight;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, TheLastKnight.MOD_ID);

    public static final RegistryObject<EntityType<Knight>> KNIGHT = ENTITY_TYPES.register("knight",
            () -> EntityType.Builder.<Knight>create(Knight::new, EntityClassification.MONSTER).size(0.9F, 2.8F).build("knight"));

    public static final RegistryObject<EntityType<LittleKnight>> LITTLE_KNIGHT = ENTITY_TYPES.register("little_knight",
            () -> EntityType.Builder.<LittleKnight>create(LittleKnight::new, EntityClassification.MONSTER).size(0.7F, 1.5F).build("little_knight"));

}
