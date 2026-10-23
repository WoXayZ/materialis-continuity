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

public class JovicModifier extends Modifier implements ToolStatsModifierHook, AttributesModifierHook {

	private static final UUID[] KB_UUIDS = {
			UUID.fromString("c90e2a14-6d5b-4f18-aa37-1e9c4d7b8021"),
			UUID.fromString("d01f3b25-7e6c-4029-bb48-2fad5e8c9132"),
			UUID.fromString("e1204c36-8f7d-4130-cc59-30be6f9d0243"),
			UUID.fromString("f2315d47-908e-4241-dd6a-41cf70ae1354")
	};

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.ATTRIBUTES);
	}

	@Override
	public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
		ToolStats.DURABILITY.multiply(builder, 1.0f + 0.12f * modifier.getLevel());
	}

	@Override
	public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
		if (slot.getType() == EquipmentSlot.Type.ARMOR) {
			consumer.accept(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KB_UUIDS[slot.getIndex()], "materialis.jovic", 0.08 * modifier.getLevel(), AttributeModifier.Operation.ADDITION));
		}
	}
}
