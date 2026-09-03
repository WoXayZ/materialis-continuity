package com.materialiscontinuity.registry;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.item.ManaShotItem.ManashotEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MaterialisEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Materialis.MOD_ID);

	public static final RegistryObject<EntityType<ManashotEntity>> MANASHOT = ENTITIES.register("manashot",
			() -> EntityType.Builder.<ManashotEntity>of(ManashotEntity::new, MobCategory.MISC)
					.sized(0.5F, 0.5F)
					.clientTrackingRange(4)
					.updateInterval(20)
					.build(Materialis.MOD_ID + ":manashot"));
}
