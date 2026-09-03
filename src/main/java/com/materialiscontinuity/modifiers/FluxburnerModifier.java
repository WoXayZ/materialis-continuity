package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class FluxburnerModifier extends CapacitorModifier implements BreakSpeedModifierHook, BlockBreakModifierHook {

	private static final int ENERGY_COST = 100;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		super.registerHooks(hookBuilder);
		hookBuilder.addHook(this, ModifierHooks.BREAK_SPEED, ModifierHooks.BLOCK_BREAK);
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		if (isEffective && !tool.isBroken() && TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), true, false)) {
			event.setNewSpeed(event.getNewSpeed() + 2.5f * modifier.getLevel() * tool.getMultiplier(ToolStats.MINING_SPEED));
		}
	}

	@Override
	public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
		if (!context.isAOE() && context.isEffective() && !tool.isBroken()) {
			TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), false, false);
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		super.addTooltip(tool, modifier, player, tooltip, tooltipKey, tooltipFlag);
		float bonus = TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), true, false) ? 2.5f * modifier.getLevel() : 0;
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.MINING_SPEED), bonus, tooltip);
	}

	@Override
	public int getCapacity() {
		return 5000;
	}
}
