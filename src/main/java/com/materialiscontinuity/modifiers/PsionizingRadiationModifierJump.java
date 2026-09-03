package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.util.MaterialisUtil;

import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class PsionizingRadiationModifierJump extends Modifier implements VolatileDataModifierHook {

	public PsionizingRadiationModifierJump() {
		if (MaterialisUtil.PSI_LOADED) {
			MinecraftForge.EVENT_BUS.addListener(this::onPsiArmorEvent);
		}
	}

	@Override
	public int getPriority() {
		return 200;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		MaterialisUtil.addToVolatileInt(PsionizingRadiationModifier.RADIATION_LEVEL, volatileData, modifier.getLevel());
	}

	public void onPsiArmorEvent(PsiArmorEvent event) {
		MaterialisUtil.castOnArmorEvent(event, this, PsiArmorEvent.JUMP);
	}
}
