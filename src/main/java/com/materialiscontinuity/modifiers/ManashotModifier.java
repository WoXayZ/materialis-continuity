package com.materialiscontinuity.modifiers;

import java.util.function.Predicate;

import com.materialiscontinuity.registry.MaterialisItems;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.mana.ManaItemHandler;

public class ManashotModifier extends NoLevelsModifier implements BowAmmoModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("botania");
	private static final int MANA_COST = 200;

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BOW_AMMO);
	}

	@Override
	public int getPriority() {
		return 60; // before bulk quiver, after
	}

	@Override
	public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack standardAmmo, Predicate<ItemStack> ammoPredicate) {
		if (ENABLED && !tool.isBroken() && shooter instanceof Player player) {
			ItemStack toolStack = player.getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			if (ManaItemHandler.instance().requestManaExactForTool(toolStack, player, MANA_COST * modifier.getLevel(), false)) {
				return new ItemStack(MaterialisItems.MANASHOT.get(), 64);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void shrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, int needed) {
		if (ENABLED && shooter instanceof Player player) {
			ItemStack toolStack = player.getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			ManaItemHandler.instance().requestManaExactForTool(toolStack, player, MANA_COST * needed, true);
		}
	}
}
