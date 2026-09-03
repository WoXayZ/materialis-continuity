package com.materialiscontinuity.registry;

import java.util.Objects;

import com.materialiscontinuity.Materialis;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.tags.ITag;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;

public class MaterialisCreativeTabs {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Materialis.MOD_ID);

	public static final RegistryObject<CreativeModeTab> TAB = TABS.register("items", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.materialis"))
			.icon(() -> new ItemStack(MaterialisItems.FAIRY_INGOT.get()))
			.displayItems((parameters, output) -> {
				output.accept(MaterialisItems.FAIRY_INGOT.get());
				output.accept(MaterialisItems.FAIRY_NUGGET.get());
				output.accept(MaterialisItems.FAIRY_BLOCK_ITEM.get());
				output.accept(MaterialisFluids.MOLTEN_FAIRY.getBucket());

				for (FlowingFluidObject<?> fluid : MaterialisFluids.COMPAT_FLUIDS) {
					if (fluid == MaterialisFluids.MOLTEN_FAIRY) {
						continue;
					}
					String name = fluid.getId().getPath().replace("molten_", "");
					ITag<Item> ingot = getTag(new ResourceLocation("forge", "ingots/" + name));
					if (ingot != null && !ingot.isEmpty()) {
						output.accept(fluid.getBucket());
					}
				}

				acceptCast(output, MaterialisItems.WRENCH_HEAD_CAST);
				acceptCastIfTag(output, MaterialisItems.INLAY_CAST, "forge:inlays");

				MaterialisItems.WRENCH_HEAD.get().addVariants(output::accept, "");
				ToolBuildHandler.addVariants(output::accept, (IModifiable) MaterialisItems.WRENCH.get(), "");
				ToolBuildHandler.addVariants(output::accept, (IModifiable) MaterialisItems.BATTLEWRENCH.get(), "");

				if (ModList.get().isLoaded("botania")) {
					output.accept(MaterialisItems.MANASHOT.get());
				}
				if (ModList.get().isLoaded("psi")) {
					for (ArmorItem.Type type : ArmorItem.Type.values()) {
						output.accept(MaterialisItems.PSIMETAL_EXOSUIT.get(type));
					}
				}
			})
			.build());

	private static void acceptCast(CreativeModeTab.Output output, CastItemObject cast) {
		output.accept(cast.get());
		output.accept(cast.getSand());
		output.accept(cast.getRedSand());
	}

	private static void acceptCastIfTag(CreativeModeTab.Output output, CastItemObject cast, String tagPath) {
		ITag<Item> tag = getTag(new ResourceLocation(tagPath));
		if (tag != null && !tag.isEmpty()) {
			acceptCast(output, cast);
		}
	}

	private static ITag<Item> getTag(ResourceLocation name) {
		return Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(TagKey.create(Registries.ITEM, name));
	}
}
