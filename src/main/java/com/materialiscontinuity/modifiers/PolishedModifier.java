package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class PolishedModifier extends Modifier implements ConditionalStatModifierHook, TooltipModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.TOOLTIP);
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		float ratio = tool.getCurrentDurability() / (float) (tool.getDamage() + tool.getCurrentDurability());
		if (stat == ToolStats.ATTACK_DAMAGE) {
			return baseValue + 1.5f * multiplier * modifier.getLevel() * ratio;
		}
		if (stat == ToolStats.VELOCITY) {
			return baseValue + 0.1f * multiplier * modifier.getLevel() * ratio;
		}
		return baseValue;
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		float extra = modifier.getLevel() * tool.getCurrentDurability() / (float) (tool.getDamage() + tool.getCurrentDurability());
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.ATTACK_DAMAGE), 1.5f * extra, tooltip);
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.VELOCITY), 0.1f * extra, tooltip);
	}
}
