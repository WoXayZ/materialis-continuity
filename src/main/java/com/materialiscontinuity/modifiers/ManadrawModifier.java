package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import vazkii.botania.api.mana.ManaItemHandler;

public class ManadrawModifier extends Modifier implements ConditionalStatModifierHook, ProjectileLaunchModifierHook, TooltipModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("botania");
	private static final int MANA_COST = 80;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.PROJECTILE_LAUNCH, ModifierHooks.TOOLTIP);
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		if (ENABLED && stat == ToolStats.DRAW_SPEED && !tool.isBroken() && living instanceof Player player) {
			ItemStack toolStack = player.getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			if (ManaItemHandler.instance().requestManaExactForTool(toolStack, player, MANA_COST * modifier.getLevel(), false)) {
				return baseValue + 0.15f * multiplier * modifier.getLevel();
			}
		}
		return baseValue;
	}

	@Override
	public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
		if (ENABLED && primary && shooter instanceof Player player) {
			ItemStack toolStack = player.getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			ManaItemHandler.instance().requestManaExactForTool(toolStack, player, MANA_COST * modifier.getLevel(), true);
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.DRAW_SPEED), 0.15f * modifier.getLevel(), tooltip);
	}
}
