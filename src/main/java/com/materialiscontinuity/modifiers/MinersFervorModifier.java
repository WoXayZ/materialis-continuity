package com.materialiscontinuity.modifiers;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MinersFervorModifier extends Modifier implements BreakSpeedModifierHook {

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.BREAK_SPEED);
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		if (!isEffective) {
			return;
		}
		Player player = event.getEntity();
		int y = player.blockPosition().getY();
		float bonus = (64 - y) / 64.0f;
		if (bonus > 0) {
			event.setNewSpeed(event.getNewSpeed() + event.getOriginalSpeed() * bonus * 0.2f * modifier.getLevel());
		}
	}
}
