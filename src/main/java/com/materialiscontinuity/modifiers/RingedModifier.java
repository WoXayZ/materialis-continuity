package com.materialiscontinuity.modifiers;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class RingedModifier extends Modifier implements ToolStatsModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
	}

	@Override
	public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
		ToolStats.MINING_SPEED.multiply(builder, 1.0f + 0.1f * modifier.getLevel());
		ToolStats.ATTACK_SPEED.multiply(builder, 1.0f + 0.05f * modifier.getLevel());
	}
}
