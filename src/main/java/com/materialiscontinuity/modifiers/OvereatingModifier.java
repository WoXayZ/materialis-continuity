package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.Materialis;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class OvereatingModifier extends Modifier implements GeneralInteractionModifierHook {

	private static final ResourceLocation IS_EATING = Materialis.getResource("eating_overslime");
	public static final int OVERSLIME_AMOUNT = 20;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
	}

	@Override
	public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
		if (!tool.isBroken() && OverslimeModule.INSTANCE.getAmount(tool) >= OVERSLIME_AMOUNT * modifier.getLevel() && player.canEat(true)) {
			GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, hand);
			tool.getPersistentData().putBoolean(IS_EATING, true);
			return InteractionResult.CONSUME;
		}
		tool.getPersistentData().remove(IS_EATING);
		return InteractionResult.PASS;
	}

	@Override
	public void onStoppedUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int timeLeft) {
		tool.getPersistentData().remove(IS_EATING);
	}

	@Override
	public void onFinishUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity) {
		ModDataNBT persistentData = tool.getPersistentData();
		boolean wasEating = persistentData.getBoolean(IS_EATING);
		int level = modifier.getLevel();
		persistentData.remove(IS_EATING);

		if (!tool.isBroken() && OverslimeModule.INSTANCE.getAmount(tool) >= OVERSLIME_AMOUNT * level && wasEating && entity instanceof Player player) {
			if (player.canEat(false)) {
				Level world = entity.level();
				player.getFoodData().eat(level, level * 0.1f);
				player.awardStat(Stats.ITEM_USED.get(tool.getItem()));
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
				OverslimeModule.INSTANCE.removeAmount(tool, OVERSLIME_AMOUNT * level);
			}
		}
	}

	@Override
	public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
		return tool.getPersistentData().getBoolean(IS_EATING) ? UseAnim.EAT : UseAnim.NONE;
	}

	@Override
	public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
		return tool.getPersistentData().getBoolean(IS_EATING) ? 32 : 0;
	}
}
