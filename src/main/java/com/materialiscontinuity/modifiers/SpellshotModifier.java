package com.materialiscontinuity.modifiers;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.common.items.SpellArrow;
import com.hollingsworth.arsnouveau.common.spell.casters.ReactiveCaster;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class SpellshotModifier extends NoLevelsModifier implements BowAmmoModifierHook, TooltipModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("ars_nouveau");

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BOW_AMMO, ModifierHooks.TOOLTIP);
	}

	@Override
	public int getPriority() {
		return 60;
	}

	@Override
	public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack standardAmmo, Predicate<ItemStack> ammoPredicate) {
		if (ENABLED && shooter instanceof Player player && tool instanceof ToolStack) {
			Predicate<ItemStack> predicate = i -> i.getItem() instanceof SpellArrow && canPlayerCastSpell(((ToolStack) tool).createStack(), player);
			ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(player, predicate);
			if (!itemstack.isEmpty()) {
				return itemstack;
			}
			for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
				itemstack = player.getInventory().getItem(i);
				if (predicate.test(itemstack)) {
					return itemstack;
				}
			}
			for (ModifierEntry entry : tool.getModifierList()) {
				if (entry == modifier) {
					continue;
				}
				itemstack = entry.getHook(ModifierHooks.BOW_AMMO).findAmmo(tool, entry, player, standardAmmo, predicate);
				if (!itemstack.isEmpty()) {
					return itemstack;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean canPlayerCastSpell(ItemStack bow, Player player) {
		Spell spell = new ReactiveCaster(bow).getSpell();
		return new SpellResolver(new SpellContext(player.level(), spell, player, LivingCaster.from(player))).withSilent(true).canCast(player);
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		if (ENABLED && tool instanceof ToolStack) {
			Spell spell = new ReactiveCaster(((ToolStack) tool).createStack()).getSpell();
			if (spell.isValid()) {
				tooltip.add(Component.literal(spell.getDisplayString()));
			}
		}
	}
}
