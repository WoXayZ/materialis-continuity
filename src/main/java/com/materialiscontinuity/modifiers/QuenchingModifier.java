package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class QuenchingModifier extends Modifier implements ProjectileHitModifierHook, MeleeHitModifierHook {

	@Override
	public int getPriority() {
		return 85;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT, ModifierHooks.MELEE_HIT);
	}

	@Override
	public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
		if (target != null && target.isAlive() && target.isOnFire()) {
			float bonus = (target.getRemainingFireTicks() * modifier.getLevel()) / 60.0f;
			target.clearFire();
			ToolAttackUtil.attackEntitySecondary(projectile.damageSources().mobProjectile(projectile, attacker), bonus, target, target, true);
		}
		return false;
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		if (context.getTarget().isAlive() && context.getTarget().isOnFire()) {
			float bonus = (context.getTarget().getRemainingFireTicks() * modifier.getLevel()) / 60.0f;
			context.getTarget().clearFire();
			LivingEntity attacker = context.getAttacker();
			DamageSource source;
			Player player = context.getPlayerAttacker();
			if (player != null) {
				source = attacker.damageSources().playerAttack(player);
			} else {
				source = attacker.damageSources().mobAttack(attacker);
			}
			ToolAttackUtil.attackEntitySecondary(source, bonus * tool.getMultiplier(ToolStats.ATTACK_DAMAGE), context.getTarget(), context.getLivingTarget(), true);
		}
	}
}
