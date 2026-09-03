package com.materialiscontinuity.modifiers;

import java.util.Random;

import com.materialiscontinuity.util.MaterialisPacketHandler;
import com.materialiscontinuity.util.PacketTerraBeam;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.util.OffhandCooldownTracker;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.modifiers.ability.tool.OffhandAttackModifier;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.BotaniaItems;

public class TerrabeamModifier extends Modifier implements GeneralInteractionModifierHook, MeleeHitModifierHook {

	public static boolean enabled = ModList.get().isLoaded("botania");
	public static int MANA_PER_BEAM = 100;
	public static float CHANCE = 0.4f;
	public static Random rand = new Random();

	public TerrabeamModifier() {
		if (enabled) {
			MinecraftForge.EVENT_BUS.addListener(this::leftClick);
		}
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT, ModifierHooks.MELEE_HIT);
	}

	private void leftClick(PlayerInteractEvent.LeftClickEmpty event) {
		if (enabled && !event.getItemStack().isEmpty()) {
			if (ModifierUtil.getModifierLevel(event.getItemStack(), this.getId()) > 0) {
				MaterialisPacketHandler.INSTANCE.sendToServer(new PacketTerraBeam());
			}
		}
	}

	@Override
	public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
		if (enabled && !tool.isBroken() && hand == InteractionHand.OFF_HAND && OffhandCooldownTracker.isAttackReady(player) && OffhandCooldownTracker.getCooldown(player) == 1 && tool.getVolatileData().getBoolean(OffhandAttackModifier.DUEL_WIELDING)) {
			BurstHandler.trySpawnBurst(player, hand, true, false);
		}
		return InteractionResult.PASS;
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		if (enabled && context.getAttacker() instanceof Player player && context.isFullyCharged()) {
			BurstHandler.trySpawnBurst(player, context.getHand(), false, false);
		}
	}

	public static class BurstHandler { // this is in a separate class to make sure it doesn't try to load botania classes when botania isn't installed

		public static ItemStack sword = new ItemStack(BotaniaItems.terraSword);

		public static void trySpawnBurst(Player player, InteractionHand hand, boolean nothingForFree, boolean checkCooldown) {
			if (enabled && player != null && !player.getItemInHand(hand).isEmpty() && (!checkCooldown || player.getAttackStrengthScale(0) == 1)) {
				if (ModifierUtil.getModifierLevel(player.getItemInHand(hand), MaterialisModifiers.elvenBeamModifier.getId()) < 1) {
					int level = ModifierUtil.getModifierLevel(player.getItemInHand(hand), MaterialisModifiers.terrabeamModifier.getId());
					if (level > 0 && (level * CHANCE > 1.0f || rand.nextFloat() < level * CHANCE)) {
						ManaBurstEntity burst = BurstHandler.getBurst(player);
						player.level().addFreshEntity(burst);
						if (nothingForFree) {
							if (!ManaItemHandler.instance().requestManaExactForTool(player.getMainHandItem(), player, MANA_PER_BEAM, true)) {
								player.getMainHandItem().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
							}
						}
						player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.terraBlade, SoundSource.PLAYERS, 0.4F, 1.4F);
					}
				}
			}
		}

		public static ManaBurstEntity getBurst(Player player) {
			ManaBurstEntity burst = new ManaBurstEntity(player);
			float motionModifier = 7F;
			burst.setColor(0x20FF20);
			burst.setMana(MANA_PER_BEAM);
			burst.setStartingMana(MANA_PER_BEAM);
			burst.setMinManaLoss(40);
			burst.setManaLossPerTick(4F);
			burst.setGravity(0F);
			burst.setDeltaMovement(burst.getDeltaMovement().scale(motionModifier));
			burst.setSourceLens(sword);
			return burst;
		}
	}
}
