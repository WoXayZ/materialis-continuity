package com.materialiscontinuity.modifiers;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class OverweightModifier extends Modifier implements ToolStatsModifierHook {

	@Override
	public int getPriority() {
		return 80;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
	}

	@Override
	public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
		float multiplier = 0.1f * modifier.getLevel();
		float speedEaten = builder.getStat(ToolStats.ATTACK_DAMAGE) * 2.0f * multiplier + builder.getStat(ToolStats.MINING_SPEED) * multiplier;
		OverslimeModule.OVERSLIME_STAT.add(builder, (int) (speedEaten * 300.0f));
		float remaining = 1.0f - 0.1f * modifier.getLevel();
		ToolStats.ATTACK_DAMAGE.multiply(builder, remaining);
		ToolStats.MINING_SPEED.multiply(builder, remaining);
	}
}
