package com.materialiscontinuity.compat.naturesaura;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

public final class NaturesAuraApis {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final boolean LOADED = ModList.get().isLoaded("naturesaura");

	private NaturesAuraApis() {
	}

	public static boolean drainAura(Level level, BlockPos pos, int amount) {
		if (!LOADED || amount <= 0) {
			return false;
		}
		try {
			Class<?> chunk = Class.forName("de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk");
			Object auraChunk = chunk.getMethod("getAuraChunk", Level.class, BlockPos.class).invoke(null, level, pos);
			if (auraChunk == null) {
				return false;
			}
			int drained = ((Number) chunk.getMethod("drainAura", BlockPos.class, int.class).invoke(auraChunk, pos, amount)).intValue();
			return drained >= amount;
		} catch (Throwable t) {
			LOGGER.debug("Nature's Aura IAuraChunk unavailable", t);
			return false;
		}
	}
}
