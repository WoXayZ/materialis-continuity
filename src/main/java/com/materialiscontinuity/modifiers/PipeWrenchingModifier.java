package com.materialiscontinuity.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.BlockInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class PipeWrenchingModifier extends NoLevelsModifier implements BlockInteractionModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("prettypipes");
	private static final ResourceLocation WRENCH_ID = new ResourceLocation("prettypipes", "wrench");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BLOCK_INTERACT);
	}

	@Override
	public InteractionResult beforeBlockUse(IToolStackView tool, ModifierEntry modifier, UseOnContext context, InteractionSource source) {
		if (ENABLED && !tool.isBroken() && context.getPlayer() != null) {
			Item wrench = ForgeRegistries.ITEMS.getValue(WRENCH_ID);
			if (wrench == null || wrench == Items.AIR) {
				return InteractionResult.PASS;
			}
			InteractionResult result = wrench.useOn(context);
			if (result == InteractionResult.SUCCESS) {
				ToolDamageUtil.damage(tool, 1, context.getPlayer(), context.getItemInHand());
			}
			return result;
		}
		return InteractionResult.PASS;
	}
}
