package com.materialiscontinuity;

import com.materialiscontinuity.registry.MaterialisEntities;
import com.materialiscontinuity.registry.MaterialisItems;
import com.materialiscontinuity.util.ArmorModelHelper;
import com.materialiscontinuity.util.ManashotRenderer;
import com.materialiscontinuity.util.TintedModifierModel;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.mantle.client.model.NBTKeyModel;
import slimeknights.tconstruct.library.client.model.tools.ToolModel;
import slimeknights.tconstruct.library.client.modifiers.ModifierModelManager.ModifierModelRegistrationEvent;

@EventBusSubscriber(modid = Materialis.MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
public class MaterialisClient {

	@SubscribeEvent
	static void clientSetup(FMLClientSetupEvent event) {
		NBTKeyModel.registerExtraTexture(new ResourceLocation("tconstruct", "creative_slot"), "sensor", Materialis.getResource("item/sensor_slot"));
		if (ModList.get().isLoaded("psi")) {
			NBTKeyModel.registerExtraTexture(new ResourceLocation("tconstruct", "creative_slot"), "colorizer", new ResourceLocation("psi", "item/dye_cannister_special_psi"));
		}
		EntityRenderers.register(MaterialisEntities.MANASHOT.get(), ManashotRenderer::new);
		ArmorModelHelper.init();
	}

	@SubscribeEvent
	static void reloadListeners(RegisterClientReloadListenersEvent event) {
		if (ModList.get().isLoaded("psi")) {
			event.registerReloadListener((net.minecraft.server.packs.resources.ResourceManagerReloadListener) manager -> {
				try {
					Class.forName("com.materialiscontinuity.util.ExosuitModel").getMethod("clearCache").invoke(null);
				} catch (ReflectiveOperationException ignored) {
				}
			});
		}
	}

	@SubscribeEvent
	static void itemColors(RegisterColorHandlersEvent.Item event) {
		ToolModel.registerItemColors(event.getItemColors(), MaterialisItems.WRENCH);
		ToolModel.registerItemColors(event.getItemColors(), MaterialisItems.BATTLEWRENCH);
		for (ArmorItem.Type type : ArmorItem.Type.values()) {
			ToolModel.registerItemColors(event.getItemColors(), () -> MaterialisItems.PSIMETAL_EXOSUIT.get(type));
		}
	}

	@SubscribeEvent
	static void registerModifierModels(ModifierModelRegistrationEvent event) {
		event.registerModel(Materialis.getResource("tinted"), TintedModifierModel.UNBAKED_INSTANCE);
	}
}
