package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.compat.curios.CuriosApis;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CurioPocketModifier extends NoLevelsModifier implements EquipmentChangeModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE);
	}

	@Override
	public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
		EquipmentSlot slot = context.getChangedSlot();
		LivingEntity entity = context.getEntity();
		String id = CuriosApis.slotFor(slot);
		if (id != null) {
			CuriosApis.addSlot(entity, id, CuriosApis.uuidFor(slot), 1);
		}
	}

	@Override
	public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
		EquipmentSlot slot = context.getChangedSlot();
		LivingEntity entity = context.getEntity();
		String id = CuriosApis.slotFor(slot);
		if (id != null) {
			CuriosApis.removeSlot(entity, id, CuriosApis.uuidFor(slot));
		}
	}
}
