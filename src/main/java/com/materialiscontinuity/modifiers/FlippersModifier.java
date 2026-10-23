package com.materialiscontinuity.modifiers;

import java.util.UUID;
import java.util.function.BiConsumer;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FlippersModifier extends NoLevelsModifier implements AttributesModifierHook {

	private static final UUID SWIM_UUID = UUID.fromString("5a8d31c0-2e47-4b19-9f6e-0d3c7a15b864");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
	}

	@Override
	public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
		if (slot == EquipmentSlot.FEET) {
			consumer.accept(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(SWIM_UUID, "materialis.flippers", 0.5, AttributeModifier.Operation.MULTIPLY_BASE));
		}
	}
}
