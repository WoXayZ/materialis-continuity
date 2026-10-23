package com.materialiscontinuity.compat.jei;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipe;
import slimeknights.tconstruct.plugin.jei.ToolBuildingCategory;

/**
 * Tinkers draws the recipe preview at 3.7x. Large tools are already modeled at 2x,
 * so that preview overflows the category. Halve it before the category scale.
 */
@Mixin(ToolBuildingCategory.class)
public class ToolBuildingLargeIconMixin {
	private static final Set<ResourceLocation> LARGE_TOOLS = Set.of(
			new ResourceLocation("tconstruct", "cleaver"),
			new ResourceLocation("tconstruct", "scythe"),
			new ResourceLocation("tconstruct", "broad_axe"),
			new ResourceLocation("tconstruct", "excavator"),
			new ResourceLocation("tconstruct", "vein_hammer"),
			new ResourceLocation("tconstruct", "sledge_hammer"),
			new ResourceLocation("tconstruct", "plate_shield"),
			new ResourceLocation("materialis", "battlewrench"));

	@Inject(method = "draw", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
	private void materialis$shrinkLargePreview(ToolBuildingRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView slots,
			GuiGraphics graphics, double mouseX, double mouseY, CallbackInfo ci) {
		Item item = recipe.getOutput().asItem();
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
		if (id != null && LARGE_TOOLS.contains(id)) {
			graphics.pose().scale(0.5F, 0.5F, 1.0F);
		}
	}
}
