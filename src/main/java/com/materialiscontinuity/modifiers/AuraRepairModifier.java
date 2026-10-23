package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.compat.naturesaura.NaturesAuraApis;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class AuraRepairModifier extends Modifier implements InventoryTickModifierHook {

	private static final int AURA_COST = 200;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
	}

	@Override
	public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
		if (world.isClientSide || tool.getDamage() <= 0) {
			return;
		}
		if (holder.tickCount % Math.max(20, 60 / modifier.getLevel()) != 0) {
			return;
		}
		if (NaturesAuraApis.drainAura(world, holder.blockPosition(), AURA_COST * modifier.getLevel())) {
			ToolDamageUtil.repair(tool, 1);
		}
	}
}
