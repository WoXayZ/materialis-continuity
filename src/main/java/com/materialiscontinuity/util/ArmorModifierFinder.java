package com.materialiscontinuity.util;

import com.materialiscontinuity.Materialis;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public final class ArmorModifierFinder {

	private ArmorModifierFinder() {
	}

	public static int getArmorLevel(LivingEntity entity, ModifierId id) {
		int total = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() != EquipmentSlot.Type.ARMOR) {
				continue;
			}
			ItemStack stack = entity.getItemBySlot(slot);
			if (stack.isEmpty()) {
				continue;
			}
			ToolStack tool = ToolStack.from(stack);
			if (tool.isBroken()) {
				continue;
			}
			total += tool.getModifierLevel(id);
		}
		return total;
	}

	public static int getArmorLevel(LivingEntity entity, String path) {
		return getArmorLevel(entity, new ModifierId(Materialis.MOD_ID, path));
	}

	public static boolean hasArmorModifier(LivingEntity entity, String path) {
		return getArmorLevel(entity, path) > 0;
	}

	public static int getHeldLevel(LivingEntity entity, String path) {
		ModifierId id = new ModifierId(Materialis.MOD_ID, path);
		int total = 0;
		for (EquipmentSlot slot : new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND }) {
			ItemStack stack = entity.getItemBySlot(slot);
			if (stack.isEmpty()) {
				continue;
			}
			ToolStack tool = ToolStack.from(stack);
			if (!tool.isBroken()) {
				total += tool.getModifierLevel(id);
			}
		}
		return total;
	}
}
