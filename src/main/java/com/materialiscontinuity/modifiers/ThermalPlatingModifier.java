package com.materialiscontinuity.modifiers;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ThermalPlatingModifier extends NoLevelsModifier implements ProtectionModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROTECTION);
	}

	@Override
	public float getProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float modifierValue) {
		String id = source.getMsgId();
		if (id.contains("temperature") || id.contains("freeze") || id.contains("heat") || source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE) || source.is(net.minecraft.tags.DamageTypeTags.IS_FREEZING)) {
			return modifierValue + 3.0f;
		}
		return modifierValue;
	}
}
