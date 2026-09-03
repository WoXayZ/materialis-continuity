package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.mana.ManaItemHandler;

public class ManashieldModifier extends Modifier implements ToolDamageModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("botania");
	private static final int MANA_PER_DAMAGE = 60;

	@Override
	public int getPriority() {
		return 110;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE);
	}

	@Override
	public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
		if (ENABLED && !tool.isBroken() && holder instanceof Player player) {
			ItemStack toolStack = holder.getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			int dealt = 0;
			for (int i = 0; i < amount; i++) {
				if (RANDOM.nextFloat() >= 0.1f * modifier.getLevel()) {
					dealt++;
				} else {
					int cost = MANA_PER_DAMAGE;
					if (tool.getModifierLevel(MaterialisModifiers.elvenBeamModifier.get()) > 0) {
						cost = 200;
					} else if (tool.getModifierLevel(MaterialisModifiers.terrabeamModifier.get()) > 0) {
						cost = 100;
					}
					if (!ManaItemHandler.instance().requestManaExactForTool(toolStack, player, cost, true)) {
						dealt++;
					}
				}
			}
			return dealt;
		}
		return amount;
	}
}
