package com.materialiscontinuity.modifiers;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SkyrootedModifier extends Modifier implements ProcessLootModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
	}

	@Override
	public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
		if (generatedLoot.isEmpty() || !context.hasParam(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE)) {
			return;
		}
		int extra = 0;
		for (int i = 0; i < modifier.getLevel(); i++) {
			if (RANDOM.nextFloat() < 0.5f) {
				extra++;
			}
		}
		if (extra > 0) {
			List<ItemStack> copy = List.copyOf(generatedLoot);
			for (int i = 0; i < extra; i++) {
				for (ItemStack stack : copy) {
					if (!stack.isEmpty()) {
						generatedLoot.add(stack.copy());
					}
				}
			}
		}
	}
}
