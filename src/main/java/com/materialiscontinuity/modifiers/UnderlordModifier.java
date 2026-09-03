package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class UnderlordModifier extends Modifier implements ProjectileHitModifierHook, MeleeDamageModifierHook, BreakSpeedModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT, ModifierHooks.MELEE_DAMAGE, ModifierHooks.BREAK_SPEED);
	}

	private static boolean isUndergarden(net.minecraft.resources.ResourceLocation id) {
		return id != null && "undergarden".equals(id.getNamespace());
	}

	@Override
	public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
		if (target != null && isUndergarden(ForgeRegistries.ENTITY_TYPES.getKey(target.getType())) && target.canChangeDimensions()) {
			ToolAttackUtil.attackEntitySecondary(projectile.damageSources().mobProjectile(projectile, attacker), (float) (projectile.getDeltaMovement().length() * 0.5 * modifier.getLevel()), target, target, true);
		}
		return false;
	}

	@Override
	public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
		if (isUndergarden(ForgeRegistries.ENTITY_TYPES.getKey(context.getTarget().getType())) && context.getTarget().canChangeDimensions()) {
			return damage * (1.0f + 0.5f * modifier.getLevel());
		}
		return damage;
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		BlockState state = event.getState();
		if (isEffective && state != null && isUndergarden(ForgeRegistries.BLOCKS.getKey(state.getBlock()))) {
			event.setNewSpeed(event.getNewSpeed() * (1.0f + 0.25f * modifier.getLevel()));
		}
	}
}
