package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class FluxboltModifier extends CapacitorModifier implements ConditionalStatModifierHook, ProjectileLaunchModifierHook {

	private static final int ENERGY_COST = 100;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		super.registerHooks(hookBuilder);
		hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.PROJECTILE_LAUNCH);
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		if (stat == ToolStats.VELOCITY && TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), true, false)) {
			return baseValue + 0.1f * multiplier * modifier.getLevel();
		}
		return baseValue;
	}

	@Override
	public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
		if (primary) {
			TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), false, false);
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		super.addTooltip(tool, modifier, player, tooltip, tooltipKey, tooltipFlag);
		float bonus = TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), true, false) ? 0.1f * modifier.getLevel() : 0;
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.VELOCITY), bonus, tooltip);
	}

	@Override
	public int getCapacity() {
		return 5000;
	}
}
