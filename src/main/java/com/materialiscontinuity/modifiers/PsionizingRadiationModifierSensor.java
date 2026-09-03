package com.materialiscontinuity.modifiers;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.util.ITintingModifier;
import com.materialiscontinuity.util.MaterialisUtil;
import com.materialiscontinuity.util.SpellSockets;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;

public class PsionizingRadiationModifierSensor extends Modifier implements VolatileDataModifierHook, ModifierRemovalHook, TooltipModifierHook, ITintingModifier {

	public static final ResourceLocation SENSOR = Materialis.getResource("sensor");

	public PsionizingRadiationModifierSensor() {
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
		hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA, ModifierHooks.REMOVE, ModifierHooks.TOOLTIP);
	}

	@Override
	public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
		MaterialisUtil.addToVolatileInt(PsionizingRadiationModifier.RADIATION_LEVEL, volatileData, modifier.getLevel());
	}

	@Override
	@Nullable
	public Component onRemoved(IToolStackView tool, Modifier modifier) {
		tool.getPersistentData().remove(SENSOR);
		return null;
	}

	public void onPsiArmorEvent(PsiArmorEvent event) {
		if (event.getEntity().isSpectator()) {
			return;
		}
		for (int i = 0; i < 4; i++) {
			ItemStack armor = event.getEntity().getInventory().armor.get(i);
			ToolStack armorTool = ToolStack.copyFrom(armor);
			if (armorTool.getDefinition() != ToolDefinition.EMPTY && !armorTool.isBroken() && armorTool.getModifierLevel(this) > 0 && armorTool.getPersistentData().contains(SENSOR, Tag.TAG_COMPOUND) && !armorTool.getVolatileData().getBoolean(PsionizingRadiationModifier.SUPPRESS_TOOLCASTING)) {
				ItemStack sensor = ItemStack.of(armorTool.getPersistentData().getCompound(SENSOR));
				if (sensor.isEmpty() || !(sensor.getItem() instanceof IExosuitSensor) || !((IExosuitSensor) sensor.getItem()).getEventType(sensor).equals(event.type)) {
					return;
				}

				PlayerData data = PlayerDataHandler.get(event.getEntity());
				ItemStack playerCad = PsiAPI.getPlayerCAD(event.getEntity());

				if (!playerCad.isEmpty()) {
					int timesCast = armorTool.getPersistentData().getInt(SpellSockets.TIMES_CAST);
					ItemStack bullet = ISocketable.socketable(armor).getSelectedBullet();
					ItemCAD.cast(event.getEntity().level(), event.getEntity(), data, bullet, playerCad, 5, 0, 0.025F, (SpellContext spellContext) -> {
						spellContext.tool = armor;
						spellContext.loopcastIndex = timesCast;
					});
					armorTool.getPersistentData().putInt(SpellSockets.TIMES_CAST, timesCast + 1);
				}
			}
		}
	}

	@Override
	public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
		if (MaterialisUtil.PSI_LOADED) {
			ItemStack sensor = ItemStack.of(tool.getPersistentData().getCompound(SENSOR));
			if (!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor exosuitSensor) {
				tooltip.add(Component.translatable(exosuitSensor.getEventType(sensor)));
			}
		}
	}

	@Override
	public Component getDisplayName(IToolStackView tool, ModifierEntry entry, RegistryAccess access) {
		if (MaterialisUtil.PSI_LOADED) {
			ItemStack sensor = ItemStack.of(tool.getPersistentData().getCompound(SENSOR));
			if (!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor exosuitSensor) {
				return Component.translatable(getTranslationKey()).withStyle(style -> style.withColor(TextColor.fromRgb(exosuitSensor.getColor(sensor))));
			}
		}
		return super.getDisplayName(tool, entry, access);
	}

	@Override
	public int getTint(IToolStackView tool) {
		if (MaterialisUtil.PSI_LOADED && tool.getPersistentData().contains(SENSOR, Tag.TAG_COMPOUND)) {
			ItemStack stack = ItemStack.of(tool.getPersistentData().getCompound(SENSOR));
			if (stack.getItem() instanceof IExosuitSensor sensor) {
				return sensor.getColor(stack);
			}
		}
		return 0x82190A;
	}

	@Override
	public int getLuminosity(IToolStackView tool) {
		return 15;
	}
}
