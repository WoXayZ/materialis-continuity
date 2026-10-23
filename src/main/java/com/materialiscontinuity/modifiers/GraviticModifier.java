package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GraviticModifier extends Modifier implements MeleeHitModifierHook, OnAttackedModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.ON_ATTACKED);
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		LivingEntity target = context.getLivingTarget();
		if (target != null && damageDealt > 0) {
			Vec3 motion = target.getDeltaMovement().add(0, 0.35 * modifier.getLevel(), 0);
			target.setDeltaMovement(motion);
			target.hurtMarked = true;
			target.fallDistance = 0;
		}
	}

	@Override
	public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, net.minecraft.world.entity.EquipmentSlot slotType, net.minecraft.world.damagesource.DamageSource source, float amount, boolean isDirectDamage) {
		LivingEntity entity = context.getEntity();
		entity.fallDistance = Math.max(0, entity.fallDistance - 2.0f * modifier.getLevel());
	}
}
