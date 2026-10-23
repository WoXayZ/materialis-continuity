package com.materialiscontinuity.modifiers;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class AmbrosialModifier extends Modifier implements ProcessLootModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
	}

	@Override
	public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
		if (!context.hasParam(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE)) {
			return;
		}
		if (RANDOM.nextFloat() < 0.08f * modifier.getLevel()) {
			Item ambrosium = ForgeRegistries.ITEMS.getValue(new ResourceLocation("aether", "ambrosium_shard"));
			if (ambrosium != null) {
				generatedLoot.add(new ItemStack(ambrosium));
			}
		}
	}
}
