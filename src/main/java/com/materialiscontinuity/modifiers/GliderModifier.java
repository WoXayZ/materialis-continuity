package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GliderModifier extends NoLevelsModifier implements InventoryTickModifierHook, ValidateModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK, ModifierHooks.VALIDATE);
	}

	@Override
	@Nullable
	public Component validate(IToolStackView tool, ModifierEntry modifier) {
		if (tool.getModifierLevel(MaterialisModifiers.elytraUpgradeModifier.get()) > 0) {
			return Component.translatable("recipe.materialis.modifier.glider_elytra");
		}
		return null;
	}

	@Override
	public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
		if (!isCorrectSlot || holder.onGround() || holder.isInWater() || holder.isPassenger() || holder.isFallFlying()) {
			return;
		}
		if (holder.getDeltaMovement().y < 0 && holder.isShiftKeyDown()) {
			Vec3 motion = holder.getDeltaMovement();
			Vec3 look = holder.getLookAngle();
			holder.setDeltaMovement(motion.x * 0.99 + look.x * 0.05, Math.max(motion.y, -0.15), motion.z * 0.99 + look.z * 0.05);
			holder.fallDistance = 0;
		}
	}
}
