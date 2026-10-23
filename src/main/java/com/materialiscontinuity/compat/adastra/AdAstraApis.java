package com.materialiscontinuity.compat.adastra;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.materialiscontinuity.util.ArmorModifierFinder;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.ModList;

public final class AdAstraApis {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final boolean LOADED = ModList.get().isLoaded("ad_astra");
	private static boolean eventsRegistered;

	private AdAstraApis() {
	}

	public static void registerEvents() {
		if (!LOADED || eventsRegistered) {
			return;
		}
		eventsRegistered = true;
		MinecraftForge.EVENT_BUS.addListener(AdAstraApis::onHurt);
		MinecraftForge.EVENT_BUS.addListener(AdAstraApis::onTick);
	}

	private static void onTick(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.level().isClientSide) {
			return;
		}
		if (ArmorModifierFinder.hasArmorModifier(entity, "pressurized_suit")
				|| ArmorModifierFinder.hasArmorModifier(entity, "space_breathing")) {
			setEntityOxygen(entity, true);
		}
		if (ArmorModifierFinder.hasArmorModifier(entity, "thermal_plating")
				|| ArmorModifierFinder.hasArmorModifier(entity, "insulated")) {
			setEntityTemperature(entity, (short) 21);
		}
		if (ArmorModifierFinder.hasArmorModifier(entity, "gravity_normalizing")) {
			setEntityGravity(entity, 1.0f);
		}
		if (ArmorModifierFinder.hasArmorModifier(entity, "space_fire_proof")) {
			entity.clearFire();
			entity.setTicksFrozen(0);
		}
	}

	private static void onHurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		ResourceLocation type = event.getSource().typeHolder().unwrapKey().map(ResourceKey::location).orElse(null);
		String path = type != null ? type.getPath() : event.getSource().getMsgId();
		String namespace = type != null ? type.getNamespace() : "";
		boolean adAstra = "ad_astra".equals(namespace) || path.contains("ad_astra");
		if ((path.contains("oxygen") || "drown".equals(path)) && (ArmorModifierFinder.hasArmorModifier(entity, "pressurized_suit")
				|| ArmorModifierFinder.hasArmorModifier(entity, "space_breathing"))) {
			event.setCanceled(true);
			return;
		}
		if ((path.contains("acid") || path.contains("acid_rain")) && ArmorModifierFinder.hasArmorModifier(entity, "acid_rain_proof")) {
			event.setCanceled(true);
			return;
		}
		if (adAstra && (path.contains("heat") || path.contains("temperature") || path.contains("cryo") || path.contains("freeze"))
				&& (ArmorModifierFinder.hasArmorModifier(entity, "thermal_plating")
						|| ArmorModifierFinder.hasArmorModifier(entity, "insulated")
						|| ArmorModifierFinder.hasArmorModifier(entity, "space_fire_proof"))) {
			event.setCanceled(true);
		}
	}

	public static boolean isHotEnvironment(Entity entity) {
		if (!LOADED) {
			return entity.level().dimension() == Level.NETHER || entity.isInLava() || entity.getRemainingFireTicks() > 0;
		}
		Short temperature = getTemperature(entity);
		if (temperature != null) {
			return temperature > 40;
		}
		return entity.level().dimension() == Level.NETHER;
	}

	public static void setEntityOxygen(Entity entity, boolean value) {
		invokeSystem("earth.terrarium.adastra.api.systems.OxygenApi", "setOxygen", entity, value);
	}

	public static void setEntityTemperature(Entity entity, short value) {
		if (!LOADED) {
			return;
		}
		try {
			Class<?> api = Class.forName("earth.terrarium.adastra.api.systems.TemperatureApi");
			Object inst = api.getField("API").get(null);
			try {
				api.getMethod("setTemperature", Level.class, Entity.class, short.class).invoke(inst, entity.level(), entity, value);
			} catch (NoSuchMethodException ignored) {
				api.getMethod("setTemperature", Entity.class, short.class).invoke(inst, entity, value);
			}
		} catch (Throwable t) {
			LOGGER.debug("Ad Astra temperature API unavailable", t);
		}
	}

	public static void setEntityGravity(Entity entity, float gravity) {
		if (!LOADED) {
			return;
		}
		try {
			Class<?> api = Class.forName("earth.terrarium.adastra.api.systems.GravityApi");
			Object inst = api.getField("API").get(null);
			try {
				api.getMethod("setGravity", Entity.class, float.class).invoke(inst, entity, gravity);
			} catch (NoSuchMethodException ignored) {
				api.getMethod("setGravity", Level.class, Entity.class, float.class).invoke(inst, entity.level(), entity, gravity);
			}
		} catch (Throwable t) {
			LOGGER.debug("Ad Astra gravity API unavailable", t);
		}
	}

	public static Short getTemperature(Entity entity) {
		if (!LOADED) {
			return null;
		}
		try {
			Class<?> api = Class.forName("earth.terrarium.adastra.api.systems.TemperatureApi");
			Object inst = api.getField("API").get(null);
			Object result;
			try {
				result = api.getMethod("getTemperature", Entity.class).invoke(inst, entity);
			} catch (NoSuchMethodException ignored) {
				result = api.getMethod("getTemperature", Level.class, net.minecraft.core.BlockPos.class)
						.invoke(inst, entity.level(), entity.blockPosition());
			}
			if (result instanceof Number number) {
				return number.shortValue();
			}
		} catch (Throwable t) {
			LOGGER.debug("Ad Astra getTemperature unavailable", t);
		}
		return null;
	}

	private static void invokeSystem(String className, String method, Entity entity, boolean value) {
		if (!LOADED) {
			return;
		}
		try {
			Class<?> api = Class.forName(className);
			Object inst = api.getField("API").get(null);
			try {
				Method m = api.getMethod(method, Level.class, Entity.class, boolean.class);
				m.invoke(inst, entity.level(), entity, value);
				return;
			} catch (NoSuchMethodException ignored) {
			}
			api.getMethod(method, Entity.class, boolean.class).invoke(inst, entity, value);
		} catch (Throwable t) {
			LOGGER.debug("Ad Astra API {}#{} unavailable", className, method, t);
		}
	}
}
