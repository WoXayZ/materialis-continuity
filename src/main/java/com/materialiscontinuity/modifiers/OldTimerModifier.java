package com.materialiscontinuity.modifiers;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.RepairFactorModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class OldTimerModifier extends Modifier implements RepairFactorModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.REPAIR_FACTOR);
	}

	@Override
	public float getRepairFactor(IToolStackView tool, ModifierEntry entry, float factor) {
		float half = (tool.getDamage() + tool.getCurrentDurability()) / 2.0f;
		if (tool.getCurrentDurability() < half) {
			return factor;
		}
		float strength = entry.getLevel() / 2.0f;
		return factor + strength - strength * tool.getCurrentDurability() / half;
	}
}
