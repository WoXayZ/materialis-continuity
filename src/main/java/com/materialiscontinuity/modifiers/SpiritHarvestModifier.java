package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.compat.malum.MalumApis;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SpiritHarvestModifier extends Modifier implements MeleeHitModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		LivingEntity target = context.getLivingTarget();
		LivingEntity attacker = context.getAttacker();
		if (target != null && attacker != null && !target.isAlive()) {
			MalumApis.harvestSpirits(attacker, target, modifier.getLevel());
		}
	}
}
