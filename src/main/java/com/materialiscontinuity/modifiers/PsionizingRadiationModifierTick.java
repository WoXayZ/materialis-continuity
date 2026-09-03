package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.util.MaterialisUtil;
import com.materialiscontinuity.util.SpellSockets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;

public class PsionizingRadiationModifierTick extends Modifier implements VolatileDataModifierHook, InventoryTickModifierHook {

	@Override
	public int getPriority() {
		return 200;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.INVENTORY_TICK);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		MaterialisUtil.addToVolatileInt(PsionizingRadiationModifier.RADIATION_LEVEL, volatileData, modifier.getLevel());
	}

	@Override
	public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
		if (isCorrectSlot && MaterialisUtil.PSI_LOADED && !tool.isBroken() && holder instanceof Player player && !tool.getVolatileData().getBoolean(PsionizingRadiationModifier.SUPPRESS_TOOLCASTING)) {
			PlayerData data = PlayerDataHandler.get(player);
			ItemStack playerCad = PsiAPI.getPlayerCAD(player);

			if (!playerCad.isEmpty()) {
				int timesCast = tool.getPersistentData().getInt(SpellSockets.TIMES_CAST);
				ItemStack bullet = ISocketable.socketable(stack).getSelectedBullet();
				ItemCAD.cast(player.level(), player, data, bullet, playerCad, 0, 0, 0F, (SpellContext spellContext) -> {
					spellContext.tool = stack;
					spellContext.loopcastIndex = timesCast;
				});
				tool.getPersistentData().putInt(SpellSockets.TIMES_CAST, timesCast + 1);
			}
		}
	}
}
