package com.materialiscontinuity.modifiers;

import net.minecraft.world.entity.MobType;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class HallowedModifier extends Modifier implements MeleeDamageModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
	}

	@Override
	public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
		if (context.getLivingTarget() != null && context.getLivingTarget().getMobType() == MobType.UNDEAD) {
			return damage * (1.0f + 0.25f * modifier.getLevel());
		}
		return damage;
	}
}
