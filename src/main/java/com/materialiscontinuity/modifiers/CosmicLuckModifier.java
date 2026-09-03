package com.materialiscontinuity.modifiers;

import java.util.Map;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.LootingModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.LootingContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CosmicLuckModifier extends NoLevelsModifier implements LootingModifierHook, EnchantmentModifierHook {

	@Override
	public int getPriority() {
		return 900;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.WEAPON_LOOTING, ModifierHooks.ENCHANTMENTS);
	}

	@Override
	public int updateLooting(IToolStackView tool, ModifierEntry modifier, LootingContext context, int looting) {
		return looting + 10;
	}

	@Override
	public int updateEnchantmentLevel(IToolStackView tool, ModifierEntry modifier, Enchantment enchantment, int level) {
		if (enchantment == Enchantments.BLOCK_FORTUNE) {
			return level + 10;
		}
		return level;
	}

	@Override
	public void updateEnchantments(IToolStackView tool, ModifierEntry modifier, Map<Enchantment, Integer> map) {
		EnchantmentModifierHook.addEnchantment(map, Enchantments.BLOCK_FORTUNE, 10);
	}
}
