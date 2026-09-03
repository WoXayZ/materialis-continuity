package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.util.ITintingModifier;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.common.item.component.ItemCADColorizerPsi;

public class ColorizedModifier extends NoLevelsModifier implements ModifierRemovalHook, ITintingModifier {

	public static final boolean ENABLED = ModList.get().isLoaded("psi");
	public static final ResourceLocation COLORIZER = Materialis.getResource("colorizer");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.REMOVE);
	}

	@Override
	@Nullable
	public Component onRemoved(IToolStackView tool, Modifier modifier) {
		tool.getPersistentData().remove(COLORIZER);
		return null;
	}

	@Override
	public Component getDisplayName(IToolStackView tool, ModifierEntry entry, RegistryAccess access) {
		return Component.translatable(getTranslationKey()).withStyle(style -> style.withColor(TextColor.fromRgb(getTint(tool))));
	}

	@Override
	public int getTint(IToolStackView tool) {
		if (ENABLED && tool.getPersistentData().contains(COLORIZER, Tag.TAG_COMPOUND)) {
			ItemStack stack = ItemStack.of(tool.getPersistentData().getCompound(COLORIZER));
			if (stack.getItem() instanceof ItemCADColorizerPsi) {
				return getPsiColor();
			}
			if (stack.getItem() instanceof ICADColorizer colorizer) {
				return colorizer.getColor(stack);
			}
		}
		return getPsiColor();
	}

	@Override
	public int getLuminosity(IToolStackView tool) {
		return 15;
	}

	public static int getPsiColor() {
		if (ENABLED && FMLEnvironment.dist == Dist.CLIENT) {
			float time = ClientTickHandler.total;
			float w = (float) (Math.sin(time * 0.4) * 0.5 + 0.5) * 0.1F;
			float r = (float) (Math.sin(time * 0.1) * 0.5 + 0.5) * 0.5F + 0.25F + w;
			float g = 0.5F + w;
			float b = 1F;
			return new java.awt.Color((int) (r * 255), (int) (g * 255), (int) (b * 255)).getRGB();
		}
		return 0xFFFFFF;
	}
}
