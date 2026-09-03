package com.materialiscontinuity.modifiers;

import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class NeptunesBlessingModifier extends NoLevelsModifier implements MeleeDamageModifierHook, BreakSpeedModifierHook {

	private static final ModifierId AIRBORNE = new ModifierId("tconstruct", "airborne");

	@Override
	public int getPriority() {
		return 130;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.BREAK_SPEED);
	}

	@Override
	public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
		if (context.getTarget().isEyeInFluid(FluidTags.WATER)) {
			return damage * 1.25f;
		}
		return damage;
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		if (isEffective) {
			event.setNewSpeed(event.getNewSpeed() * negateWaterMiningModifier(event.getEntity(), tool.getModifierLevel(AIRBORNE) == 0));
		}
	}

	public static float negateWaterMiningModifier(LivingEntity entity, boolean negateSwimming) {
		float modifier = 1.0f;
		if (entity.isEyeInFluid(FluidTags.WATER)) {
			if (!EnchantmentHelper.hasAquaAffinity(entity)) {
				modifier *= 5.0F;
			}
			if (!entity.onGround() && negateSwimming) {
				modifier *= 5.0F;
			}
		}
		return modifier;
	}
}
