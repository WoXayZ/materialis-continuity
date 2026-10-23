package com.materialiscontinuity.compat.evilcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public final class EvilCraftApis {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final boolean LOADED = ModList.get().isLoaded("evilcraft");

	private EvilCraftApis() {
	}

	public static int drainBloodFromInventory(Player player, int millibuckets) {
		if (!LOADED || millibuckets <= 0) {
			return 0;
		}
		var blood = ForgeRegistries.FLUIDS.getValue(new ResourceLocation("evilcraft", "blood"));
		if (blood == null) {
			return 0;
		}
		int remaining = millibuckets;
		try {
			for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (stack.isEmpty()) {
					continue;
				}
				var cap = stack.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.FLUID_HANDLER_ITEM);
				if (cap.isPresent()) {
					IFluidHandlerItem handler = cap.orElse(null);
					FluidStack drained = handler.drain(new FluidStack(blood, remaining), IFluidHandler.FluidAction.EXECUTE);
					remaining -= drained.getAmount();
					player.getInventory().setItem(i, handler.getContainer());
				}
			}
		} catch (Throwable t) {
			LOGGER.debug("EvilCraft blood drain failed", t);
		}
		return millibuckets - remaining;
	}
}
