package com.materialiscontinuity.modifiers;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class CataclysmicModifier extends Modifier implements VolatileDataModifierHook {

	@Override
	public int getPriority() {
		return 905;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		volatileData.addSlots(SlotType.UPGRADE, modifier.getLevel() * 5);
		volatileData.addSlots(SlotType.ABILITY, modifier.getLevel());
	}
}
