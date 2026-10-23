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

public class StepAssistModifier extends NoLevelsModifier implements AttributesModifierHook {

	private static final UUID STEP_UUID = UUID.fromString("9f4c2e18-7b60-4d91-a3e5-1c8d0f6a2497");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
	}

	@Override
	public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
		if (slot == EquipmentSlot.FEET) {
			consumer.accept(ForgeMod.STEP_HEIGHT_ADDITION.get(), new AttributeModifier(STEP_UUID, "materialis.step_assist", 0.5, AttributeModifier.Operation.ADDITION));
		}
	}
}
