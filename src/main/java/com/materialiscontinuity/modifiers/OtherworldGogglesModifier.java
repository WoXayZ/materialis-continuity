package com.materialiscontinuity.modifiers;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.RawDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.KeybindInteractModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.RestrictedCompoundTag;

public class OtherworldGogglesModifier extends NoLevelsModifier implements RawDataModifierHook, KeybindInteractModifierHook {

	public static final String OTHERWORLD_GOGGLES = "occultism:otherworld_goggles";

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.RAW_DATA, ModifierHooks.ARMOR_INTERACT);
	}

	@Override
	public void addRawData(IToolStackView tool, ModifierEntry modifier, RestrictedCompoundTag tag) {
		tag.putBoolean(OTHERWORLD_GOGGLES, true);
	}

	@Override
	public void removeRawData(IToolStackView tool, slimeknights.tconstruct.library.modifiers.Modifier modifier, RestrictedCompoundTag tag) {
		tag.remove(OTHERWORLD_GOGGLES);
	}

	@Override
	public boolean startInteract(IToolStackView tool, ModifierEntry modifier, Player player, EquipmentSlot slot, TooltipKey keyModifier) {
		if (player.isShiftKeyDown() && tool instanceof ToolStack toolStack) {
			RestrictedCompoundTag tag = toolStack.getRestrictedNBT();
			tag.putBoolean(OTHERWORLD_GOGGLES, !tag.getBoolean(OTHERWORLD_GOGGLES));
			return true;
		}
		return false;
	}
}
