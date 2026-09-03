package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class NocturnalModifier extends Modifier implements ConditionalStatModifierHook, BreakSpeedModifierHook, TooltipModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.BREAK_SPEED, ModifierHooks.TOOLTIP);
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		int time = (int) (living.level().getDayTime() % 24000L);
		if (time > 12000) {
			float bonus = (float) (-Math.sin(time * Math.PI / 12000.0f) * modifier.getLevel()) * multiplier;
			if (stat == ToolStats.MINING_SPEED) {
				return baseValue + bonus * 2.0f;
			}
			if (stat == ToolStats.DRAW_SPEED) {
				return baseValue + bonus * 0.1f;
			}
		}
		return baseValue;
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		int time = (int) (event.getEntity().level().getDayTime() % 24000L);
		if (time > 12000) {
			float bonus = (float) (-Math.sin(time * Math.PI / 12000.0f) * 2 * modifier.getLevel());
			event.setNewSpeed(event.getNewSpeed() + bonus * tool.getMultiplier(ToolStats.MINING_SPEED));
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		int time = player == null ? 0 : (int) (player.level().getDayTime() % 24000L);
		float bonus = 0;
		if (time > 12000) {
			bonus = (float) (-Math.sin(time * Math.PI / 12000.0f) * modifier.getLevel());
		}
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.MINING_SPEED), bonus * 2.0f, tooltip);
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.DRAW_SPEED), bonus * 0.1f, tooltip);
	}
}
