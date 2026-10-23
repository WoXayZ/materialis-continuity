package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.compat.adastra.AdAstraApis;

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

public class HeatforgedModifier extends Modifier implements ConditionalStatModifierHook, MeleeDamageModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.MELEE_DAMAGE);
	}

	private static boolean hot(LivingEntity living) {
		return living != null && AdAstraApis.isHotEnvironment(living);
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		if (hot(living) && (stat == ToolStats.MINING_SPEED || stat == ToolStats.DRAW_SPEED || stat == ToolStats.VELOCITY)) {
			return baseValue + 1.5f * modifier.getLevel() * multiplier;
		}
		return baseValue;
	}

	@Override
	public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
		if (hot(context.getAttacker())) {
			return damage + 1.0f * modifier.getLevel();
		}
		return damage;
	}
}
