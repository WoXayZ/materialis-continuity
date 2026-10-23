package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.mana.ManaItemHandler;

public class PhotosynthModifier extends Modifier implements InventoryTickModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("botania");
	private static final int MANA_PER_REPAIR = 80;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
	}

	@Override
	public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
		if (!ENABLED || world.isClientSide || tool.getDamage() <= 0 || !(holder instanceof Player player)) {
			return;
		}
		if (holder.tickCount % Math.max(20, 80 / modifier.getLevel()) != 0) {
			return;
		}
		if (world.canSeeSky(holder.blockPosition()) && world.isDay()) {
			ItemStack toolStack = stack;
			if (tool instanceof ToolStack toolStackView) {
				toolStack = toolStackView.createStack();
			}
			if (ManaItemHandler.instance().requestManaExactForTool(toolStack, player, MANA_PER_REPAIR, true)) {
				ToolDamageUtil.repair(tool, 1);
			}
		}
	}
}
