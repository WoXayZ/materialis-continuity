package com.materialiscontinuity.modifiers;

import java.util.function.BiConsumer;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.handler.PixieHandler;

public class PixiecallerModifier extends Modifier implements AttributesModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("botania");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
	}

	@Override
	public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
		if (ENABLED && slot.getType() == Type.HAND) {
			consumer.accept(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Tool modifier", 0.03 * modifier.getLevel()));
		}
	}
}
