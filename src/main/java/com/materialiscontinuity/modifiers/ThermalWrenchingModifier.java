package com.materialiscontinuity.modifiers;

import java.lang.reflect.Method;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
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

public class ThermalWrenchingModifier extends NoLevelsModifier implements BlockInteractionModifierHook, MeleeHitModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("cofh_core");
	private static final ResourceLocation WRENCHED = new ResourceLocation("cofh_core", "wrenched");

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
			if (context.getPlayer().isSecondaryUseActive() && block.canEntityDestroy(state, world, pos, context.getPlayer()) && Cofh.canDismantle(block, world, pos, state, context.getPlayer())) {
				if (!world.isClientSide) {
					BlockHitResult target = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside());
					Cofh.dismantle(block, world, pos, state, target, context.getPlayer());
				}
				ToolDamageUtil.damage(tool, 1, context.getPlayer(), context.getItemInHand());
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		if (ENABLED) {
			LivingEntity target = context.getLivingTarget();
			if (target != null && context.isFullyCharged() && target.isAlive()) {
				MobEffect wrenched = ForgeRegistries.MOB_EFFECTS.getValue(WRENCHED);
				if (wrenched != null) {
					target.addEffect(new MobEffectInstance(wrenched, 60, 0, false, false));
				}
			}
		}
	}

	private static final class Cofh {
		private static final Class<?> DISMANTLEABLE;
		private static final Method CAN_DISMANTLE;
		private static final Method DISMANTLE_BLOCK;

		static {
			Class<?> type = null;
			Method can = null;
			Method dismantle = null;
			try {
				type = Class.forName("cofh.lib.api.block.IDismantleable");
				can = type.getMethod("canDismantle", Level.class, BlockPos.class, BlockState.class, net.minecraft.world.entity.player.Player.class);
				dismantle = type.getMethod("dismantleBlock", Level.class, BlockPos.class, BlockState.class, BlockHitResult.class, net.minecraft.world.entity.player.Player.class, boolean.class);
			} catch (ReflectiveOperationException ignored) {
			}
			DISMANTLEABLE = type;
			CAN_DISMANTLE = can;
			DISMANTLE_BLOCK = dismantle;
		}

		static boolean canDismantle(Block block, Level world, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player) {
			if (DISMANTLEABLE == null || CAN_DISMANTLE == null || !DISMANTLEABLE.isInstance(block)) {
				return false;
			}
			try {
				return Boolean.TRUE.equals(CAN_DISMANTLE.invoke(block, world, pos, state, player));
			} catch (ReflectiveOperationException e) {
				return false;
			}
		}

		static void dismantle(Block block, Level world, BlockPos pos, BlockState state, BlockHitResult target, net.minecraft.world.entity.player.Player player) {
			if (DISMANTLE_BLOCK == null) {
				return;
			}
			try {
				DISMANTLE_BLOCK.invoke(block, world, pos, state, target, player, false);
			} catch (ReflectiveOperationException ignored) {
			}
		}
	}
}
