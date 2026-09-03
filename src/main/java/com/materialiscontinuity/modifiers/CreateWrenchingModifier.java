package com.materialiscontinuity.modifiers;

import java.util.Random;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.BlockInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CreateWrenchingModifier extends NoLevelsModifier implements BlockInteractionModifierHook, MeleeHitModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("create");
	private static final Random RAND = new Random();

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BLOCK_INTERACT, ModifierHooks.MELEE_HIT);
	}

	@Override
	public InteractionResult beforeBlockUse(IToolStackView tool, ModifierEntry modifier, UseOnContext context, InteractionSource source) {
		if (ENABLED && !tool.isBroken() && context.getPlayer() != null) {
			Level world = context.getLevel();
			BlockPos pos = context.getClickedPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if (!(block instanceof IWrenchable actor)) {
				if (context.getPlayer().isSecondaryUseActive() && AllTags.AllBlockTags.WRENCH_PICKUP.matches(state)) {
					ToolDamageUtil.damage(tool, 1, context.getPlayer(), context.getItemInHand());
					return onItemUseOnOther(world, pos, state, context);
				}
				return InteractionResult.PASS;
			}
			ToolDamageUtil.damage(tool, 1, context.getPlayer(), context.getItemInHand());
			if (context.getPlayer().isSecondaryUseActive()) {
				return actor.onSneakWrenched(state, context);
			}
			return actor.onWrenched(state, context);
		}
		return InteractionResult.PASS;
	}

	private InteractionResult onItemUseOnOther(Level world, BlockPos pos, BlockState state, UseOnContext context) {
		Player player = context.getPlayer();
		if (!(world instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}
		if (player != null && !player.isCreative()) {
			Block.getDrops(state, serverLevel, pos, world.getBlockEntity(pos), player, context.getItemInHand())
					.forEach(itemStack -> player.getInventory().placeItemBackInInventory(itemStack));
		}
		state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY, false);
		world.destroyBlock(pos, false);
		AllSoundEvents.WRENCH_REMOVE.playOnServer(world, pos, 1, RAND.nextFloat() * .5f + .5f);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		if (context.getTarget() instanceof AbstractMinecart) {
			Player player = context.getPlayerAttacker();
			if (player != null) {
				context.getTarget().hurt(player.damageSources().playerAttack(player), 100);
			} else {
				context.getTarget().hurt(context.getAttacker().damageSources().mobAttack(context.getAttacker()), 100);
			}
		}
	}
}
