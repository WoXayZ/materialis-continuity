package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ElytraFlightModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class ElytraUpgradeModifier extends NoLevelsModifier implements VolatileDataModifierHook, ElytraFlightModifierHook, ValidateModifierHook {

	private static final ResourceLocation ELYTRA_FLAG = new ResourceLocation("tconstruct", "elyta");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.ELYTRA_FLIGHT, ModifierHooks.VALIDATE);
	}

	@Override
	@Nullable
	public Component validate(IToolStackView tool, ModifierEntry modifier) {
		if (tool.getModifierLevel(MaterialisModifiers.gliderModifier.get()) > 0) {
			return Component.translatable("recipe.materialis.modifier.glider_elytra");
		}
		return null;
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		volatileData.putBoolean(ELYTRA_FLAG, true);
	}

	@Override
	public boolean elytraFlightTick(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int flightTicks) {
		return false;
	}
}
