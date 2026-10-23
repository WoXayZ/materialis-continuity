package com.materialiscontinuity.modifiers;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GildedModifier extends Modifier implements ProcessLootModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
	}

	@Override
	public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
		if (RANDOM.nextFloat() < 0.04f * modifier.getLevel()) {
			generatedLoot.add(new ItemStack(Items.GOLD_NUGGET, 1 + RANDOM.nextInt(modifier.getLevel())));
		}
	}
}
