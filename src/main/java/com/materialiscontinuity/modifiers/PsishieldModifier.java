package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

public class PsishieldModifier extends Modifier implements ToolDamageModifierHook {

	private static final boolean ENABLED = ModList.get().isLoaded("psi");

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
			PlayerData data = PlayerDataHandler.get(player);
			if (!data.overflowed && (float) data.getAvailablePsi() / (float) data.getTotalPsi() > 0.5F) {
				int dealt = 0;
				for (int i = 0; i < amount; i++) {
					if (RANDOM.nextFloat() >= 0.1f * modifier.getLevel()) {
						dealt++;
					}
				}
				if (dealt != amount) {
					data.deductPsi(150 * (amount - dealt), 0, true);
				}
				return dealt;
			}
		}
		return amount;
	}
}
