package com.materialiscontinuity.modifiers;

import java.util.UUID;
import java.util.function.BiConsumer;

import com.materialiscontinuity.Materialis;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraftforge.common.ForgeMod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ShortSightedModifier extends Modifier implements AttributesModifierHook {

	private static final UUID MAINHAND_ATTRIBUTE_UUID = UUID.fromString("f8a6e738-642b-11eb-ae93-0242ac136002");
	private static final UUID OFFHAND_ATTRIBUTE_UUID = UUID.fromString("9720e9f3-c123-4b0b-bdb2-b4bc52eb39c9");
	private static final String ATTRIBUTE_NAME = Materialis.MOD_ID + ".short_sighted";

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
	}

	@Override
	public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
		if (slot.getType() == Type.HAND) {
			consumer.accept(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(slot == EquipmentSlot.OFFHAND ? OFFHAND_ATTRIBUTE_UUID : MAINHAND_ATTRIBUTE_UUID, ATTRIBUTE_NAME, -0.2 * modifier.getLevel(), Operation.MULTIPLY_TOTAL));
		}
	}
}
