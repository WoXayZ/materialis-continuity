package com.materialiscontinuity.modifiers;

import javax.annotation.Nullable;

import com.materialiscontinuity.util.TinkerToolFluxed;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FluxshieldModifier extends CapacitorModifier implements ToolDamageModifierHook {

	private static final int ENERGY_COST = 200;

	@Override
	public int getPriority() {
		return 110;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		super.registerHooks(hookBuilder);
		hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE);
	}

	@Override
	public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
		if (!tool.isBroken()) {
			int dealt = 0;
			for (int i = 0; i < amount; i++) {
				if (RANDOM.nextFloat() >= 0.1f * modifier.getLevel()) {
					dealt++;
				} else if (!TinkerToolFluxed.removeEnergy(tool, ENERGY_COST, false, false)) {
					dealt++;
				}
			}
			return dealt;
		}
		return amount;
	}

	@Override
	public int getCapacity() {
		return 5000;
	}
}
