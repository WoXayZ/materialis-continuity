package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import vazkii.botania.api.mana.ManaItemHandler;

public class ManaburnerModifier extends Modifier implements BreakSpeedModifierHook, BlockBreakModifierHook, TooltipModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("botania");
	private static final int MANA_COST = 80;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BREAK_SPEED, ModifierHooks.BLOCK_BREAK, ModifierHooks.TOOLTIP);
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		if (ENABLED && isEffective && !tool.isBroken()) {
			ItemStack toolStack = event.getEntity().getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			if (ManaItemHandler.instance().requestManaExactForTool(toolStack, event.getEntity(), MANA_COST * modifier.getLevel(), false)) {
				event.setNewSpeed(event.getNewSpeed() + 2.5f * modifier.getLevel() * tool.getMultiplier(ToolStats.MINING_SPEED));
			}
		}
	}

	@Override
	public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
		if (ENABLED && !context.isAOE() && context.isEffective() && !tool.isBroken() && context.getPlayer() != null) {
			ItemStack toolStack = context.getPlayer().getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			ManaItemHandler.instance().requestManaExactForTool(toolStack, context.getPlayer(), MANA_COST * modifier.getLevel(), true);
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		TooltipModifierHook.addFlatBoost(this, TooltipModifierHook.statName(this, ToolStats.MINING_SPEED), 2.5f * modifier.getLevel(), tooltip);
	}
}
