package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.common.spell.casters.ReactiveCaster;
import com.materialiscontinuity.util.MaterialisPacketHandler;
import com.materialiscontinuity.util.PacketReactiveSwing;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.mantle.util.OffhandCooldownTracker;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.RawDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.RestrictedCompoundTag;
import slimeknights.tconstruct.tools.modifiers.ability.tool.OffhandAttackModifier;

public class ReactiveModifier extends Modifier implements GeneralInteractionModifierHook, MeleeHitModifierHook, OnAttackedModifierHook, TooltipModifierHook, RawDataModifierHook {

	public static boolean enabled = ModList.get().isLoaded("ars_nouveau");

	public ReactiveModifier() {
		if (enabled) {
			MinecraftForge.EVENT_BUS.addListener(this::leftClick);
			MinecraftForge.EVENT_BUS.addListener(this::leftClickBlock);
		}
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT, ModifierHooks.MELEE_HIT, ModifierHooks.ON_ATTACKED, ModifierHooks.TOOLTIP, ModifierHooks.RAW_DATA);
	}

	@Override
	public void addRawData(IToolStackView tool, ModifierEntry modifier, RestrictedCompoundTag tag) {
	}

	@Override
	public void removeRawData(IToolStackView tool, Modifier modifier, RestrictedCompoundTag tag) {
		tag.remove("ars_nouveau_reactiveCaster");
	}

	private void leftClick(PlayerInteractEvent.LeftClickEmpty event) {
		if (enabled && !event.getItemStack().isEmpty()) {
			ToolStack tool = getHeldTool(event.getEntity(), InteractionHand.MAIN_HAND);
			if (tool != null && tool.getModifierLevel(this) > 0) {
				MaterialisPacketHandler.INSTANCE.sendToServer(new PacketReactiveSwing());
			}
		}
	}

	public static void receiveLeftClick(Player player) {
		if (enabled && player != null) {
			ToolStack tool = getHeldTool(player, InteractionHand.MAIN_HAND);
			if (tool != null && !tool.isBroken() && tool.getModifierLevel(MaterialisModifiers.reactiveModifier.get()) > 0) {
				castSpell(tool, player, player.getMainHandItem(), InteractionHand.MAIN_HAND);
			}
		}
	}

	@Override
	public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
		if (enabled && !tool.isBroken() && hand == InteractionHand.OFF_HAND && OffhandCooldownTracker.isAttackReady(player) && tool.getVolatileData().getBoolean(OffhandAttackModifier.DUEL_WIELDING)) {
			ItemStack toolStack = player.getItemInHand(hand);
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			castSpell(tool, player, toolStack, InteractionHand.OFF_HAND);
		}
		return InteractionResult.PASS;
	}

	private void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (enabled) {
			Player player = event.getEntity();
			if (player.level().isClientSide) {
				return;
			}
			ToolStack tool = getHeldTool(player, InteractionHand.MAIN_HAND);
			if (tool != null && !tool.isBroken() && tool.getModifierLevel(this) > 0) {
				castSpell(tool, player, event.getItemStack(), InteractionHand.MAIN_HAND);
			}
		}
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		if (enabled && !tool.isBroken() && context.getPlayerAttacker() != null) {
			ItemStack toolStack = context.getPlayerAttacker().getItemInHand(context.getHand());
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			castSpell(tool, context.getPlayerAttacker(), toolStack, context.getHand());
		}
	}

	@Override
	public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
		if (enabled && !tool.isBroken() && context.getEntity() instanceof Player player) {
			ItemStack toolStack = player.getItemBySlot(slotType);
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			castSpell(tool, player, toolStack, InteractionHand.MAIN_HAND);
		}
	}

	public static void castSpell(IToolStackView tool, Player player, ItemStack stack, InteractionHand hand) {
		if (tool.getModifierLevel(MaterialisModifiers.reactiveModifier.get()) * .25 >= Math.random() && new ReactiveCaster(stack).getSpell().isValid()) {
			ReactiveCaster reactiveCaster = new ReactiveCaster(stack);
			reactiveCaster.castSpell(player.level(), player, hand, null);
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		if (enabled && tool instanceof ToolStack) {
			Spell spell = new ReactiveCaster(((ToolStack) tool).createStack()).getSpell();
			if (spell.isValid()) {
				tooltip.add(Component.literal(spell.getDisplayString()));
			}
		}
	}
}
