package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class CleansingModifier extends Modifier implements ProjectileHitModifierHook, MeleeDamageModifierHook {

	public static final TagKey<EntityType<?>> ROTSPAWN = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("undergarden", "rotspawn"));

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT, ModifierHooks.MELEE_DAMAGE);
	}

	@Override
	public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
		if (target != null && target.getType().is(ROTSPAWN)) {
			ToolAttackUtil.attackEntitySecondary(projectile.damageSources().mobProjectile(projectile, attacker), (float) (projectile.getDeltaMovement().length() * 0.25 * modifier.getLevel()), target, target, true);
		}
		return false;
	}

	@Override
	public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
		if (context.getTarget().getType().is(ROTSPAWN)) {
			return damage * (1.0f + 0.25f * modifier.getLevel());
		}
		return damage;
	}
}
