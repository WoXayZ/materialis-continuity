package com.materialiscontinuity.modifiers;

import java.util.UUID;
import java.util.function.BiConsumer;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class DenseModifier extends Modifier implements ToolStatsModifierHook, AttributesModifierHook {

	private static final UUID[] KB_UUIDS = {
			UUID.fromString("a11c0d2e-4b73-4e90-9c18-7f6a2d5e8b40"),
			UUID.fromString("b22d1e3f-5c84-4f01-ad29-8a7b3e6f9c51"),
			UUID.fromString("c33e2f40-6d95-4002-be3a-9b8c4f70ad62"),
			UUID.fromString("d44f3051-7ea6-4113-cf4b-ac9d5081be73")
	};

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.ATTRIBUTES);
	}

	@Override
	public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
		ToolStats.ATTACK_DAMAGE.multiply(builder, 1.0f + 0.08f * modifier.getLevel());
		ToolStats.MINING_SPEED.multiply(builder, 1.0f - 0.08f * modifier.getLevel());
	}

	@Override
	public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
		if (slot.getType() == EquipmentSlot.Type.ARMOR) {
			consumer.accept(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KB_UUIDS[slot.getIndex()], "materialis.dense", 0.05 * modifier.getLevel(), AttributeModifier.Operation.ADDITION));
		}
	}
}
