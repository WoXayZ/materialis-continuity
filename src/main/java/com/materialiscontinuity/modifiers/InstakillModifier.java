package com.materialiscontinuity.modifiers;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class InstakillModifier extends NoLevelsModifier implements MeleeHitModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
	}

	@Override
	public int getPriority() {
		return 903;
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		LivingEntity attacker = context.getAttacker();
		if (attacker.level().isClientSide) {
			return;
		}
		LivingEntity target = context.getLivingTarget();
		if (target == null) {
			return;
		}
		if (attacker instanceof net.minecraft.world.entity.player.Player player) {
			target.hurt(attacker.damageSources().playerAttack(player), Float.MAX_VALUE);
		} else {
			target.hurt(attacker.damageSources().mobAttack(attacker), Float.MAX_VALUE);
		}
	}
}
