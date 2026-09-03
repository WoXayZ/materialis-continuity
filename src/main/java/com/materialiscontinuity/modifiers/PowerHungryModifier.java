package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class PowerHungryModifier extends CapacitorModifier implements ConditionalStatModifierHook, ToolDamageModifierHook {

	private static final int ENERGY_COST = 100;

	@Override
	public int getPriority() {
		return 160;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		super.registerHooks(hookBuilder);
		hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.TOOL_DAMAGE);
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		if ((stat == ToolStats.VELOCITY || stat == ToolStats.MINING_SPEED || stat == ToolStats.ATTACK_DAMAGE) && !TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), true, false)) {
			return baseValue / 1.5f;
		}
		return baseValue;
	}

	@Override
	public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
		TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), false, true);
		return amount;
	}

	@Override
	public int getCapacity() {
		return 10000;
	}
}
