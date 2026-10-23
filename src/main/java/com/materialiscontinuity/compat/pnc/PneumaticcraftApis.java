package com.materialiscontinuity.compat.pnc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.ModList;

public final class PneumaticcraftApis {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final boolean LOADED = ModList.get().isLoaded("pneumaticcraft");

	private PneumaticcraftApis() {
	}

	@SuppressWarnings("unchecked")
	public static int drainAirFromInventory(Player player, int needed) {
		if (!LOADED || needed <= 0) {
			return 0;
		}
		int remaining = needed;
		try {
			Capability<Object> cap = (Capability<Object>) Class.forName("me.desht.pneumaticcraft.api.PNCCapabilities")
					.getField("AIR_HANDLER_ITEM_CAPABILITY").get(null);
			for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (stack.isEmpty()) {
					continue;
				}
				var optional = stack.getCapability(cap);
				if (optional.isPresent()) {
					Object handler = optional.orElse(null);
					int air = ((Number) handler.getClass().getMethod("getAir").invoke(handler)).intValue();
					if (air <= 0) {
						continue;
					}
					int take = Math.min(air, remaining);
					handler.getClass().getMethod("addAir", int.class).invoke(handler, -take);
					remaining -= take;
				}
			}
		} catch (Throwable t) {
			LOGGER.debug("PneumaticCraft air capability unavailable", t);
		}
		return needed - remaining;
	}
}
