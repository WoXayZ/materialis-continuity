package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.registry.MaterialisBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ResidualLightModifier extends NoLevelsModifier implements BlockBreakModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BLOCK_BREAK);
	}

	@Override
	public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
		Level level = context.getWorld();
		BlockPos pos = context.getPos();
		if (!level.isClientSide && level.getBlockState(pos).isAir()) {
			BlockState residue = MaterialisBlocks.LIGHT_RESIDUE.get().defaultBlockState();
			if (residue.canSurvive(level, pos)) {
				level.setBlockAndUpdate(pos, residue);
			}
		}
	}
}
