package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EmpoweredModifier extends CapacitorModifier implements ToolDamageModifierHook, ModifyDamageModifierHook {

	private static final int ENERGY_PER_DAMAGE = 250;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		super.registerHooks(hookBuilder);
		hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE, ModifierHooks.MODIFY_DAMAGE);
	}

	@Override
	public int getCapacity() {
		return 20000;
	}

	@Override
	public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
		int dealt = 0;
		for (int i = 0; i < amount; i++) {
			if (!TinkerToolFluxed.removeEnergy(tool, ENERGY_PER_DAMAGE, false, false)) {
				dealt++;
			}
		}
		return dealt;
	}

	@Override
	public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
		if (amount <= 0) {
			return amount;
		}
		int cost = (int) (amount * ENERGY_PER_DAMAGE);
		if (TinkerToolFluxed.removeEnergy(tool, cost, false, false)) {
			return amount * (1.0f - 0.08f * modifier.getLevel());
		}
		return amount;
	}
}
