package com.materialiscontinuity.modifiers;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class IcyThornsModifier extends Modifier implements OnAttackedModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED);
	}

	@Override
	public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
		if (source.getEntity() instanceof LivingEntity attacker && isDirectDamage) {
			attacker.setTicksFrozen(attacker.getTicksFrozen() + 40 * modifier.getLevel());
			attacker.hurt(context.getEntity().damageSources().thorns(context.getEntity()), 1.0f * modifier.getLevel());
		}
	}
}
