package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
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
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class CapacitorModifier extends Modifier implements VolatileDataModifierHook, ValidateModifierHook, ModifierRemovalHook, TooltipModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.VALIDATE, ModifierHooks.REMOVE, ModifierHooks.TOOLTIP);
	}

	@Override
	@Nullable
	public Component validate(IToolStackView tool, ModifierEntry modifier) {
		int max = tool.getVolatileData().getInt(TinkerToolFluxed.MAX_ENERGY);
		if (tool.getPersistentData().getInt(TinkerToolFluxed.STORED_ENERGY) > max) {
			tool.getPersistentData().putInt(TinkerToolFluxed.STORED_ENERGY, max);
		}
		return null;
	}

	@Override
	@Nullable
	public Component onRemoved(IToolStackView tool, Modifier modifier) {
		if (tool.getVolatileData().getInt(TinkerToolFluxed.MAX_ENERGY) == 0) {
			tool.getPersistentData().remove(TinkerToolFluxed.STORED_ENERGY);
		}
		return null;
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		int level = modifier.getLevel();
		if (volatileData.contains(TinkerToolFluxed.MAX_ENERGY, Tag.TAG_INT)) {
			volatileData.putInt(TinkerToolFluxed.MAX_ENERGY, volatileData.getInt(TinkerToolFluxed.MAX_ENERGY) + getCapacity() * level);
		} else {
			volatileData.putInt(TinkerToolFluxed.MAX_ENERGY, getCapacity() * level);
		}
		if (!volatileData.contains(TinkerToolFluxed.ENERGY_OWNER, Tag.TAG_STRING)) {
			volatileData.putString(TinkerToolFluxed.ENERGY_OWNER, getId().toString());
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		if (tool instanceof ToolStack && isOwner(tool.getVolatileData())) {
			tooltip.add(Component.translatable(TinkerToolFluxed.STORED_ENERGY_KEY, tool.getPersistentData().getInt(TinkerToolFluxed.STORED_ENERGY), tool.getVolatileData().getInt(TinkerToolFluxed.MAX_ENERGY)).withStyle(style -> style.withColor(getColor())));
		}
	}

	public int getCapacity() {
		return 10000;
	}

	public boolean isOwner(IModDataView volatileData) {
		return getId().toString().equals(volatileData.getString(TinkerToolFluxed.ENERGY_OWNER));
	}
}
