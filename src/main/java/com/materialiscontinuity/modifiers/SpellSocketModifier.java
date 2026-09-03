package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.SpellSockets;

import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class SpellSocketModifier extends Modifier implements VolatileDataModifierHook, ValidateModifierHook, ModifierRemovalHook, TooltipModifierHook {

	public static final boolean PSI_LOADED = ModList.get().isLoaded("psi");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.VALIDATE, ModifierHooks.REMOVE, ModifierHooks.TOOLTIP);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		if (!PSI_LOADED) {
			return;
		}
		int add = modifier.getLevel();
		if (volatileData.contains(SpellSockets.SOCKETS, Tag.TAG_INT)) {
			volatileData.putInt(SpellSockets.SOCKETS, volatileData.getInt(SpellSockets.SOCKETS) + add);
		} else {
			volatileData.putInt(SpellSockets.SOCKETS, add);
		}
		if (!volatileData.contains(SpellSockets.SOCKET_OWNER, Tag.TAG_STRING)) {
			volatileData.putString(SpellSockets.SOCKET_OWNER, getId().toString());
		}
	}

	@Override
	@Nullable
	public Component validate(IToolStackView tool, ModifierEntry modifier) {
		if (!PSI_LOADED) {
			return null;
		}
		int sockets = tool.getVolatileData().contains(SpellSockets.SOCKETS, Tag.TAG_INT) ? tool.getVolatileData().getInt(SpellSockets.SOCKETS) : 0;
		if (sockets > SpellSockets.MAX_SLOTS) {
			return Component.translatable("recipe.materialis.add_modifier.too_many_spell_slots");
		}
		for (int slot = sockets; slot < SpellSockets.MAX_SLOTS; slot++) {
			if (tool.getPersistentData().contains(SpellSockets.SPELL_SLOTS[slot], Tag.TAG_COMPOUND)) {
				return Component.translatable("recipe.materialis.remove_modifier.spell_slot_not_empty");
			}
		}
		return null;
	}

	@Override
	@Nullable
	public Component onRemoved(IToolStackView tool, Modifier modifier) {
		if (PSI_LOADED && tool.getVolatileData().getInt(SpellSockets.SOCKETS) == 0) {
			tool.getPersistentData().remove(SpellSockets.SELECTED_SPELL);
		}
		return null;
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		if (PSI_LOADED && isOwner(tool.getVolatileData())) {
			tooltip.add(Component.translatable("tooltip.materialis.spell_sockets", tool.getVolatileData().getInt(SpellSockets.SOCKETS)));
		}
	}

	public boolean isOwner(IModDataView volatileData) {
		return getId().toString().equals(volatileData.getString(SpellSockets.SOCKET_OWNER));
	}
}
