package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class WorkHardenedModifier extends Modifier implements ToolDamageModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE);
	}

	@Override
	public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
		int level = modifier.getLevel();
		float total = (tool.getDamage() + tool.getCurrentDurability());
		float multiplier = (tool.getCurrentDurability() * level) / (total * 10.0f) - (level - 10.0f) / 10.0f;
		float damage = amount * multiplier;
		int roundDamage = (int) damage;
		float extraChance = damage - roundDamage;
		if (RANDOM.nextFloat() < extraChance) {
			roundDamage++;
		}
		return roundDamage;
	}
}
