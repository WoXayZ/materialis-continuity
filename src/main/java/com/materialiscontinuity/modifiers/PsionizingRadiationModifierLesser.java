package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.util.MaterialisUtil;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.RemoveBlockModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class PsionizingRadiationModifierLesser extends Modifier implements VolatileDataModifierHook, RemoveBlockModifierHook, MeleeHitModifierHook {

	@Override
	public int getPriority() {
		return 200;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.REMOVE_BLOCK, ModifierHooks.MELEE_HIT);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		MaterialisUtil.addToVolatileInt(PsionizingRadiationModifier.RADIATION_LEVEL, volatileData, modifier.getLevel());
	}

	@Override
	public Boolean removeBlock(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
		MaterialisUtil.castOnBlockBreak(tool, modifier.getLevel(), context, true);
		return null;
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		MaterialisUtil.castOnEntityHit(tool, modifier.getLevel(), context, damageDealt, true);
	}
}
