package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.compat.pnc.PneumaticcraftApis;

import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockHarvestModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class PressurizedModifier extends Modifier implements VolatileDataModifierHook, InventoryTickModifierHook, ConditionalStatModifierHook, MeleeHitModifierHook, BlockHarvestModifierHook, TooltipModifierHook {

	public static final ResourceLocation MAX_AIR = Materialis.getResource("max_air");
	public static final ResourceLocation STORED_AIR = Materialis.getResource("stored_air");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.INVENTORY_TICK, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.MELEE_HIT, ModifierHooks.BLOCK_HARVEST, ModifierHooks.TOOLTIP);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		volatileData.putInt(MAX_AIR, volatileData.getInt(MAX_AIR) + 1000 * modifier.getLevel());
	}

	private static int getAir(IToolStackView tool) {
		return tool.getPersistentData().contains(STORED_AIR, Tag.TAG_INT) ? tool.getPersistentData().getInt(STORED_AIR) : 0;
	}

	private static void setAir(IToolStackView tool, int air) {
		tool.getPersistentData().putInt(STORED_AIR, Math.max(0, Math.min(air, tool.getVolatileData().getInt(MAX_AIR))));
	}

	private static boolean consume(IToolStackView tool, int amount) {
		int air = getAir(tool);
		if (air < amount) {
			return false;
		}
		setAir(tool, air - amount);
		return true;
	}

	@Override
	public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
		if (world.isClientSide || !(holder instanceof Player player)) {
			return;
		}
		int max = tool.getVolatileData().getInt(MAX_AIR);
		int missing = max - getAir(tool);
		if (missing > 0) {
			int drained = PneumaticcraftApis.drainAirFromInventory(player, missing);
			if (drained > 0) {
				setAir(tool, getAir(tool) + drained);
			}
		}
	}

	@Override
	public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
		if (getAir(tool) <= 0 && (stat == ToolStats.MINING_SPEED || stat == ToolStats.ATTACK_DAMAGE)) {
			return baseValue * 0.6f;
		}
		return baseValue;
	}

	@Override
	public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
		if (damageDealt > 0) {
			consume(tool, 20);
		}
	}

	@Override
	public void finishHarvest(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context, int harvested) {
		if (harvested > 0) {
			consume(tool, 10 * harvested);
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		tooltip.add(Component.translatable("tooltip.materialis.stored_air", getAir(tool), tool.getVolatileData().getInt(MAX_AIR)).withStyle(style -> style.withColor(getColor())));
	}
}
