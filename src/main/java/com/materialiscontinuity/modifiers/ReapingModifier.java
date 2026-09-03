package com.materialiscontinuity.modifiers;

import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.SingleLevelModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReapingModifier extends SingleLevelModifier implements ProcessLootModifierHook {

	@Override
	public int getPriority() {
		return 95; // after other loot modifiers, before voiding
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
		if (entity instanceof LivingEntity target && target.isInvertedHealAndHarm()) {
			generatedLoot.clear();
			int luck = modifier.getLevel() - 1;
			if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof LivingEntity killer) {
				luck += EnchantmentHelper.getMobLooting(killer);
			}
			if (tool.getItem().builtInRegistryHolder().is(TinkerTags.Items.SCYTHES)) {
				luck++;
			}
			// Soul shard drops stay gated behind Eidolon; original 1.18 drop/packet code was already commented out.
		}
	}
}
