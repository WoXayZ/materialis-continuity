package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class CosmicUnbreakableModifier extends NoLevelsModifier implements ToolDamageModifierHook, VolatileDataModifierHook, DurabilityDisplayModifierHook {

	@Override
	public int getPriority() {
		return 904;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE, ModifierHooks.VOLATILE_DATA, ModifierHooks.DURABILITY_DISPLAY);
	}

	@Override
	public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
		return 0;
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		volatileData.putBoolean(IModifiable.INDESTRUCTIBLE_ENTITY, true);
	}

	@Override
	@Nullable
	public Boolean showDurabilityBar(IToolStackView tool, ModifierEntry modifier) {
		return null;
	}

	@Override
	public int getDurabilityWidth(IToolStackView tool, ModifierEntry modifier) {
		return 0;
	}

	@Override
	public int getDurabilityRGB(IToolStackView tool, ModifierEntry modifier) {
		return 0xFF5555;
	}
}
