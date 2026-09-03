package com.materialiscontinuity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.materialiscontinuity.datagen.MaterialisData;
import com.materialiscontinuity.modifiers.MaterialisModifiers;
import com.materialiscontinuity.registry.MaterialisBlocks;
import com.materialiscontinuity.registry.MaterialisCreativeTabs;
import com.materialiscontinuity.registry.MaterialisEntities;
import com.materialiscontinuity.registry.MaterialisFluids;
import com.materialiscontinuity.registry.MaterialisItems;
import com.materialiscontinuity.registry.MaterialisRecipeSerializers;
import com.materialiscontinuity.util.MaterialisConfigCondition;
import com.materialiscontinuity.util.MaterialisPacketHandler;
import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;

@Mod(Materialis.MOD_ID)
public class Materialis {

	public static final String MOD_ID = "materialis";
	public static final Logger LOGGER = LogManager.getLogger();

	public Materialis() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		bus.addListener(MaterialisData::gatherData);

		MaterialisFluids.FLUIDS.register(bus);
		MaterialisBlocks.BLOCKS.register(bus);
		MaterialisItems.ITEMS.register(bus);
		MaterialisEntities.ENTITIES.register(bus);
		MaterialisRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
		MaterialisRecipeSerializers.RECIPE_TYPES.register(bus);
		MaterialisCreativeTabs.TABS.register(bus);
		MaterialisModifiers.MODIFIERS.register(bus);

		MaterialisConfig.register();
	}

	private void setup(final FMLCommonSetupEvent event) {
		MaterialisPacketHandler.init();
		if (ModList.get().isLoaded("psi")) {
			try {
				Class.forName("com.materialiscontinuity.compat.TinkerToolSocketable")
						.getMethod("registerCapability")
						.invoke(null);
			} catch (ReflectiveOperationException e) {
				LOGGER.error("Failed to register Psi tool sockets", e);
			}
		}
		if (ModList.get().isLoaded("ars_nouveau")) {
			try {
				Object api = Class.forName("com.hollingsworth.arsnouveau.api.ArsNouveauAPI").getMethod("getInstance").invoke(null);
				@SuppressWarnings("unchecked")
				java.util.Set<net.minecraft.world.item.crafting.RecipeType<?>> types =
						(java.util.Set<net.minecraft.world.item.crafting.RecipeType<?>>) api.getClass().getMethod("getEnchantingRecipeTypes").invoke(api);
				types.add(MaterialisRecipeSerializers.TINKER_SPELL_WRITE_TYPE.get());
			} catch (ReflectiveOperationException e) {
				LOGGER.warn("Could not register Ars Nouveau spell-write recipe type", e);
			}
		}
		ToolCapabilityProvider.register(TinkerToolFluxed::new);
		CraftingHelper.register(MaterialisConfigCondition.SERIALIZER);
		if (!TierSortingRegistry.isTierSorted(com.materialiscontinuity.util.InfinityTier.instance)) {
			var sorted = TierSortingRegistry.getSortedTiers();
			if (!sorted.isEmpty()) {
				TierSortingRegistry.registerTier(com.materialiscontinuity.util.InfinityTier.instance, getResource("infinity"), java.util.List.of(sorted.get(sorted.size() - 1)), java.util.List.of());
			} else {
				TierSortingRegistry.registerTier(com.materialiscontinuity.util.InfinityTier.instance, getResource("infinity"), java.util.List.of(Tiers.NETHERITE), java.util.List.of());
			}
		}
	}

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static String makeDescriptionId(String type, String name) {
		return type + "." + MOD_ID + "." + name;
	}
}
