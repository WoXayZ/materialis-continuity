package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.util.MaterialisUtil;
import com.materialiscontinuity.util.SpellSockets;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;

public class PsionizingRadiationModifierDamage extends Modifier implements VolatileDataModifierHook, OnAttackedModifierHook {

	@Override
	public int getPriority() {
		return 200;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.ON_ATTACKED);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		MaterialisUtil.addToVolatileInt(PsionizingRadiationModifier.RADIATION_LEVEL, volatileData, modifier.getLevel());
	}

	@Override
	public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
		if (MaterialisUtil.PSI_LOADED && !tool.isBroken() && context.getEntity() instanceof Player player && !tool.getVolatileData().getBoolean(PsionizingRadiationModifier.SUPPRESS_TOOLCASTING)) {
			ItemStack toolStack = context.getEntity().getItemBySlot(slotType);
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			PlayerData data = PlayerDataHandler.get(player);
			ItemStack playerCad = PsiAPI.getPlayerCAD(player);

			if (!playerCad.isEmpty()) {
				int timesCast = tool.getPersistentData().getInt(SpellSockets.TIMES_CAST);
				ItemStack bullet = ISocketable.socketable(toolStack).getSelectedBullet();
				final ItemStack finalTool = toolStack;
				ItemCAD.cast(player.level(), player, data, bullet, playerCad, 5, 0, 0.025F, (SpellContext spellContext) -> {
					spellContext.tool = finalTool;
					if (source.getEntity() instanceof LivingEntity living) {
						spellContext.attackingEntity = living;
					}
					spellContext.damageTaken = amount;
					spellContext.loopcastIndex = timesCast;
				});
				tool.getPersistentData().putInt(SpellSockets.TIMES_CAST, timesCast + 1);
			}
		}
	}
}
