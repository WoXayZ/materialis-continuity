package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.compat.evilcraft.EvilCraftApis;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BloodboundModifier extends Modifier implements InventoryTickModifierHook {

	private static final int BLOOD_PER_REPAIR = 50;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
	}

	@Override
	public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
		if (world.isClientSide || tool.getDamage() <= 0 || !(holder instanceof Player player)) {
			return;
		}
		if (holder.tickCount % Math.max(20, 40 / modifier.getLevel()) != 0) {
			return;
		}
		if (EvilCraftApis.drainBloodFromInventory(player, BLOOD_PER_REPAIR) >= BLOOD_PER_REPAIR) {
			ToolDamageUtil.repair(tool, 1);
		}
	}
}
