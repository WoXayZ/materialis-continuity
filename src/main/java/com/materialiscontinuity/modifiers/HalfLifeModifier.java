package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.utils.RomanNumeralHelper;

public class HalfLifeModifier extends Modifier implements ToolDamageModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE);
	}

	@Override
	public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
		int level = modifier.getLevel();
		float multiplier = (float) Math.pow(4.0f * tool.getCurrentDurability() / (tool.getCurrentDurability() + tool.getDamage()), 0.5f);
		float damage = (float) (amount * Math.pow(multiplier * level, level));
		int roundDamage = (int) damage;
		float extraChance = damage - roundDamage;
		extraChance = Math.max(extraChance, 0.01f);
		if (RANDOM.nextFloat() < extraChance) {
			roundDamage++;
		}
		return roundDamage;
	}

	@Override
	public Component getDisplayName(int level) {
		if (level > 2 && level < 5) {
			return applyStyle(Component.translatable(getTranslationKey() + "." + level));
		}
		if (level > 2) {
			level -= 2;
		}
		return applyStyle(Component.translatable(getTranslationKey()).append(" ").append(RomanNumeralHelper.getNumeral(level)));
	}
}
