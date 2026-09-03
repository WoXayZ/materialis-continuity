package com.materialiscontinuity.modifiers;

import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.Tags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.SingleLevelModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.severing.SeveringRecipeCache;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SkullfireModifire extends SingleLevelModifier implements ProcessLootModifierHook {

	@Override
	public int getPriority() {
		return 97;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
	}

	@Override
	public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
		if (!context.hasParam(LootContextParams.DAMAGE_SOURCE)) {
			return;
		}
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if (entity != null) {
			if (generatedLoot.stream().noneMatch(stack -> stack.is(Tags.Items.HEADS))) {
				List<SeveringRecipe> recipes = SeveringRecipeCache.findRecipe(context.getLevel().getRecipeManager(), entity.getType());
				if (!recipes.isEmpty()) {
					for (SeveringRecipe recipe : recipes) {
						ItemStack result = recipe.getOutput(entity);
						if (result.getItem() == Items.WITHER_SKELETON_SKULL) {
							generatedLoot.add(result);
							return;
						}
						if (result.getItem() == Items.SKELETON_SKULL) {
							generatedLoot.add(new ItemStack(Items.WITHER_SKELETON_SKULL, result.getCount()));
							return;
						}
					}
				}
			} else {
				for (int i = 0; i < generatedLoot.size(); i++) {
					ItemStack stack = generatedLoot.get(i);
					if (stack.getItem() == Items.SKELETON_SKULL) {
						generatedLoot.set(i, new ItemStack(Items.WITHER_SKELETON_SKULL, stack.getCount()));
						return;
					}
				}
			}
		}
	}
}
