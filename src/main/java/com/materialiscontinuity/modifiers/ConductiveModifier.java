package com.materialiscontinuity.modifiers;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class ConductiveModifier extends Modifier implements ConditionalStatModifierHook, MeleeDamageModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.MELEE_DAMAGE);
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		if (living != null && living.isInWaterOrRain() && stat == ToolStats.MINING_SPEED) {
			return baseValue + 2.0f * modifier.getLevel() * multiplier;
		}
		return baseValue;
	}

	@Override
	public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
		if (context.getAttacker() != null && context.getAttacker().isInWaterOrRain()) {
			return damage + 1.5f * modifier.getLevel();
		}
		return damage;
	}
}
