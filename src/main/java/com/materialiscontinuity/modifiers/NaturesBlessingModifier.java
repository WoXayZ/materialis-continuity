package com.materialiscontinuity.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.BlockInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class NaturesBlessingModifier extends Modifier implements BlockInteractionModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BLOCK_INTERACT);
	}

	@Override
	public InteractionResult beforeBlockUse(IToolStackView tool, ModifierEntry modifier, UseOnContext context, InteractionSource source) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if (player == null || tool.isBroken()) {
			return InteractionResult.PASS;
		}
		BlockPos pos = context.getClickedPos();
		if (BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), level, pos) || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), level, pos, context.getClickedFace())) {
			if (!level.isClientSide) {
				level.levelEvent(1505, pos, 0);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}
}
