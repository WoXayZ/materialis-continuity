package com.materialiscontinuity.util;

import com.materialiscontinuity.modifiers.PsionizingRadiationModifier;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.BlockSideHitListener;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;

public class MaterialisUtil {

	public static final boolean PSI_LOADED = ModList.get().isLoaded("psi");

	public static void addToVolatileInt(ResourceLocation tag, ModDataNBT volatileData, int add) {
		if (volatileData.contains(tag, Tag.TAG_INT)) {
			volatileData.putInt(tag, volatileData.getInt(tag) + add);
		} else {
			volatileData.putInt(tag, add);
		}
	}

	public static void castOnBlockBreak(IToolStackView tool, int level, ToolHarvestContext context, boolean checkTag) {
		if (PSI_LOADED && !tool.isBroken() && context.getPlayer() != null && !tool.getVolatileData().getBoolean(PsionizingRadiationModifier.SUPPRESS_TOOLCASTING)) {
			if (context.isAOE() && tool.getVolatileData().getInt(PsionizingRadiationModifier.RADIATION_LEVEL) < 2) {
				return;
			}
			ItemStack toolStack = context.getPlayer().getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			if (checkTag && !toolStack.is(TinkerTags.Items.HARVEST_PRIMARY)) {
				return;
			}
			PlayerData data = PlayerDataHandler.get(context.getPlayer());
			ItemStack playerCad = PsiAPI.getPlayerCAD(context.getPlayer());

			if (!playerCad.isEmpty()) {
				ItemStack bullet = ISocketable.socketable(toolStack).getSelectedBullet();
				final ItemStack finalTool = toolStack;
				Direction sideHit = BlockSideHitListener.getSideHit(context.getPlayer());
				Vec3 hit = new Vec3(context.getPos().getX() + 0.5D - sideHit.getStepX() * 0.5D, context.getPos().getY() + 0.5D - sideHit.getStepY() * 0.5D, context.getPos().getZ() + 0.5D - sideHit.getStepZ() * 0.5D);
				ItemCAD.cast(context.getPlayer().level(), context.getPlayer(), data, bullet, playerCad, 5, 10, 0.05F, (SpellContext spellContext) -> {
					spellContext.tool = finalTool;
					spellContext.positionBroken = new BlockHitResult(hit, sideHit, context.getPos(), false);
				});
			}
		}
	}

	public static void castOnEntityHit(IToolStackView tool, int level, ToolAttackContext context, float damageDealt, boolean checkTag) {
		if (PSI_LOADED && !tool.isBroken() && context.getPlayerAttacker() != null && context.getLivingTarget() != null && !tool.getVolatileData().getBoolean(PsionizingRadiationModifier.SUPPRESS_TOOLCASTING)) {
			if (context.isExtraAttack() && tool.getVolatileData().getInt(PsionizingRadiationModifier.RADIATION_LEVEL) < 2) {
				return;
			}
			ItemStack toolStack = context.getPlayerAttacker().getMainHandItem();
			if (tool instanceof ToolStack) {
				toolStack = ((ToolStack) tool).createStack();
			}
			if (checkTag && !toolStack.is(TinkerTags.Items.MELEE_PRIMARY)) {
				return;
			}
			PlayerData data = PlayerDataHandler.get(context.getPlayerAttacker());
			ItemStack playerCad = PsiAPI.getPlayerCAD(context.getPlayerAttacker());

			if (!playerCad.isEmpty()) {
				ItemStack bullet = ISocketable.socketable(toolStack).getSelectedBullet();
				final ItemStack finalTool = toolStack;
				ItemCAD.cast(context.getPlayerAttacker().level(), context.getPlayerAttacker(), data, bullet, playerCad, 5, 10, 0.05F, (SpellContext spellContext) -> {
					spellContext.attackedEntity = context.getLivingTarget();
					spellContext.tool = finalTool;
				});
			}
		}
	}

	public static void castOnArmorEvent(PsiArmorEvent event, Modifier modifier, String type) {
		if (event.type.equals(type) && !event.getEntity().isSpectator()) {
			for (int i = 0; i < 4; i++) {
				ItemStack armor = event.getEntity().getInventory().armor.get(i);
				ToolStack armorTool = ToolStack.copyFrom(armor);
				if (armorTool.getDefinition() != ToolDefinition.EMPTY && !armorTool.isBroken() && armorTool.getModifierLevel(modifier) > 0 && !armorTool.getVolatileData().getBoolean(PsionizingRadiationModifier.SUPPRESS_TOOLCASTING)) {
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
	}
}
