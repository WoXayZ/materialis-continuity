package com.materialiscontinuity.compat.curios;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.fml.ModList;

public final class CuriosApis {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final boolean LOADED = ModList.get().isLoaded("curios");
	private static final UUID HEAD = UUID.fromString("3c8d1b6a-5f21-4d3e-9a44-2f6c8e1a90b1");
	private static final UUID CHEST = UUID.fromString("7e2a9c14-88d0-4b6f-a1c3-0d5e7f9b2c48");
	private static final UUID LEGS = UUID.fromString("b4f6d208-1a9c-4e70-8d3b-6c5a0e2f1749");
	private static final UUID FEET = UUID.fromString("0a17e5c3-2b8d-4f91-9e46-3d7c8a5b6012");

	private CuriosApis() {
	}

	public static String slotFor(EquipmentSlot slot) {
		return switch (slot) {
			case HEAD -> "head";
			case CHEST -> "back";
			case LEGS -> "belt";
			case FEET -> "curios";
			default -> null;
		};
	}

	public static UUID uuidFor(EquipmentSlot slot) {
		return switch (slot) {
			case HEAD -> HEAD;
			case CHEST -> CHEST;
			case LEGS -> LEGS;
			case FEET -> FEET;
			default -> HEAD;
		};
	}

	public static void addSlot(LivingEntity entity, String identifier, UUID uuid, int amount) {
		if (!LOADED || identifier == null) {
			return;
		}
		try {
			Object api = Class.forName("top.theillusivec4.curios.api.CuriosApi").getMethod("getInstance").invoke(null);
			try {
				api.getClass().getMethod("addTransientSlotModifier", LivingEntity.class, String.class, UUID.class, double.class, AttributeModifier.Operation.class)
						.invoke(api, entity, identifier, uuid, (double) amount, AttributeModifier.Operation.ADDITION);
				return;
			} catch (NoSuchMethodException ignored) {
			}
			Object helper = Class.forName("top.theillusivec4.curios.api.CuriosApi").getMethod("getSlotHelper").invoke(null);
			helper.getClass().getMethod("addTransientSlotModifier", LivingEntity.class, String.class, UUID.class, double.class, AttributeModifier.Operation.class)
					.invoke(helper, entity, identifier, uuid, (double) amount, AttributeModifier.Operation.ADDITION);
		} catch (Throwable t) {
			LOGGER.debug("Curios slot modifier API unavailable", t);
		}
	}

	public static void removeSlot(LivingEntity entity, String identifier, UUID uuid) {
		if (!LOADED || identifier == null) {
			return;
		}
		try {
			Object api = Class.forName("top.theillusivec4.curios.api.CuriosApi").getMethod("getInstance").invoke(null);
			try {
				api.getClass().getMethod("removeSlotModifier", LivingEntity.class, String.class, UUID.class)
						.invoke(api, entity, identifier, uuid);
				return;
			} catch (NoSuchMethodException ignored) {
			}
			Object helper = Class.forName("top.theillusivec4.curios.api.CuriosApi").getMethod("getSlotHelper").invoke(null);
			helper.getClass().getMethod("removeSlotModifier", LivingEntity.class, String.class, UUID.class)
					.invoke(helper, entity, identifier, uuid);
		} catch (Throwable t) {
			LOGGER.debug("Curios remove slot API unavailable", t);
		}
	}
}
