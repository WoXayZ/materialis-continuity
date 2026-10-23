package com.materialiscontinuity.compat.malum;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public final class MalumApis {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final boolean LOADED = ModList.get().isLoaded("malum");

	private MalumApis() {
	}

	public static void harvestSpirits(LivingEntity attacker, LivingEntity target, int level) {
		if (!LOADED || attacker.level().isClientSide || target.level().isClientSide) {
			return;
		}
		try {
			Class<?> helper = Class.forName("com.sammy.malum.util.helper.SpiritHelper");
			try {
				helper.getMethod("createSpiritEntity", LivingEntity.class, LivingEntity.class).invoke(null, target, attacker);
				return;
			} catch (NoSuchMethodException ignored) {
			}
			helper.getMethod("spawnSpirits", LivingEntity.class, LivingEntity.class, int.class).invoke(null, target, attacker, level);
			return;
		} catch (Throwable t) {
			LOGGER.debug("Malum spirit helper unavailable, falling back to item drops", t);
		}
		dropSpiritItems(target, level);
	}

	public static void tickSoulWard(LivingEntity wearer, int level) {
		if (!LOADED || wearer.level().isClientSide) {
			return;
		}
		try {
			Class<?> handler = Class.forName("com.sammy.malum.core.handlers.SoulWardHandler");
			try {
				handler.getMethod("recoverSoulWard", LivingEntity.class, float.class).invoke(null, wearer, 0.05f * level);
				return;
			} catch (NoSuchMethodException ignored) {
			}
			Object instance = handler.getMethod("getSoulWardHandler", LivingEntity.class).invoke(null, wearer);
			instance.getClass().getMethod("addSoulWard", float.class).invoke(instance, 0.05f * level);
		} catch (Throwable t) {
			LOGGER.debug("Malum soul ward unavailable, applying absorption", t);
			if (wearer.tickCount % 80 == 0 && wearer.getAbsorptionAmount() < 2.0f * level) {
				wearer.setAbsorptionAmount(Math.min(2.0f * level, wearer.getAbsorptionAmount() + 0.5f * level));
			}
		}
	}

	private static void dropSpiritItems(LivingEntity target, int level) {
		List<Item> spirits = new ArrayList<>();
		for (Item item : ForgeRegistries.ITEMS) {
			ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
			if (id != null && "malum".equals(id.getNamespace()) && id.getPath().endsWith("_spirit")) {
				spirits.add(item);
			}
		}
		if (spirits.isEmpty()) {
			Item soulstone = ForgeRegistries.ITEMS.getValue(new ResourceLocation("malum", "processed_soulstone"));
			if (soulstone != null) {
				spirits.add(soulstone);
			}
		}
		if (spirits.isEmpty()) {
			return;
		}
		for (int i = 0; i < level; i++) {
			if (target.getRandom().nextFloat() > 0.35f) {
				continue;
			}
			ItemStack stack = new ItemStack(spirits.get(target.getRandom().nextInt(spirits.size())));
			ItemEntity entity = new ItemEntity(target.level(), target.getX(), target.getY() + 0.5, target.getZ(), stack);
			target.level().addFreshEntity(entity);
		}
	}
}
