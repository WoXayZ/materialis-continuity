package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class FluxripperModifier extends CapacitorModifier implements MeleeDamageModifierHook {

	private static final int ENERGY_COST = 100;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		super.registerHooks(hookBuilder);
		hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
	}

	@Override
	public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
		if (!tool.isBroken() && TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), false, false)) {
			return damage + 1.5f * modifier.getLevel() * tool.getMultiplier(ToolStats.ATTACK_DAMAGE);
		}
		return damage;
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		super.addTooltip(tool, modifier, player, tooltip, tooltipKey, tooltipFlag);
		float bonus = TinkerToolFluxed.removeEnergy(tool, ENERGY_COST * modifier.getLevel(), true, false) ? 1.5f * modifier.getLevel() : 0;
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.ATTACK_DAMAGE), bonus, tooltip);
	}

	@Override
	public int getCapacity() {
		return 5000;
	}
}
