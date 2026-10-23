package com.materialiscontinuity.modifiers;

import java.util.List;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class KnowledgeOfAgesModifier extends Modifier implements ProcessLootModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
	}

	@Override
	public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
		if (!context.hasParam(LootContextParams.DAMAGE_SOURCE) || generatedLoot.isEmpty()) {
			return;
		}
		int xp = 0;
		for (ItemStack stack : generatedLoot) {
			xp += stack.getCount() * modifier.getLevel();
		}
		generatedLoot.clear();
		if (xp > 0 && context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity target) {
			target.level().addFreshEntity(new ExperienceOrb(target.level(), target.getX(), target.getY(), target.getZ(), xp));
		}
	}
}
