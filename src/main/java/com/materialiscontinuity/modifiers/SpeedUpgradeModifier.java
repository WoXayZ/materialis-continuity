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
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SpeedUpgradeModifier extends Modifier implements AttributesModifierHook {

	private static final UUID SPEED_UUID = UUID.fromString("e2b17c90-4a5d-4c8e-9f31-6d0a8b47c152");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
	}

	@Override
	public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
		if (slot == EquipmentSlot.LEGS) {
			consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_UUID, "materialis.speed_upgrade", 0.1 * modifier.getLevel(), AttributeModifier.Operation.MULTIPLY_BASE));
		}
	}
}
