package com.materialiscontinuity.compat;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.registry.MaterialisFluids;
import com.materialiscontinuity.registry.MaterialisItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.common.registration.CastItemObject;

@JeiPlugin
public class MaterialisJEIPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Materialis.MOD_ID, "jei_plugin");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		IIngredientManager manager = jeiRuntime.getIngredientManager();
		for (FlowingFluidObject<?> fluid : MaterialisFluids.COMPAT_FLUIDS) {
			String name = fluid.getId().getPath().replace("molten_", "");
			ITag<Item> ingot = getTag(new ResourceLocation("forge", "ingots/" + name));
			if (ingot == null || ingot.isEmpty()) {
				removeFluid(manager, fluid.get(), fluid.getBucket());
			}
		}
		optionalCast(manager, MaterialisItems.INLAY_CAST, "forge:inlays");

		if (!ModList.get().isLoaded("psi")) {
			for (ArmorItem.Type type : ArmorItem.Type.values()) {
				manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK,
						Collections.singleton(new ItemStack(MaterialisItems.PSIMETAL_EXOSUIT.get(type))));
			}
		}
		if (!ModList.get().isLoaded("botania")) {
			manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK,
					Collections.singleton(new ItemStack(MaterialisItems.MANASHOT.get())));
		}
		if (!ModList.get().isLoaded("avaritia")) {
			manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK,
					Collections.singleton(new ItemStack(MaterialisItems.HEAVENSHOT.get())));
		}
	}

	public static void removeFluid(IIngredientManager manager, Fluid fluid, Item bucket) {
		manager.removeIngredientsAtRuntime(ForgeTypes.FLUID_STACK, Collections.singleton(new FluidStack(fluid, FluidType.BUCKET_VOLUME)));
		manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Collections.singleton(new ItemStack(bucket)));
	}

	public static void optionalCast(IIngredientManager manager, CastItemObject cast, String tagPath) {
		ITag<Item> tag = getTag(new ResourceLocation(tagPath));
		if (tag == null || tag.isEmpty()) {
			manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(
					new ItemStack(cast.get()),
					new ItemStack(cast.getSand()),
					new ItemStack(cast.getRedSand())));
		}
	}

	private static ITag<Item> getTag(ResourceLocation name) {
		return getTag(TagKey.create(Registries.ITEM, name));
	}

	private static ITag<Item> getTag(TagKey<Item> name) {
		return Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(name);
	}
}
