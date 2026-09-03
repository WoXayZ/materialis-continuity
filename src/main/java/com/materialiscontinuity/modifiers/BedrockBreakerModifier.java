package com.materialiscontinuity.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class BedrockBreakerModifier extends NoLevelsModifier {

	public BedrockBreakerModifier() {
		MinecraftForge.EVENT_BUS.addListener(this::leftClickBlock);
	}

	@Override
	public int getPriority() {
		return 901;
	}

	private void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		Level world = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		if (event.getEntity().isCreative() || state.getDestroySpeed(world, pos) >= 0.0F) {
			return;
		}
		ToolStack tool = getHeldTool(event.getEntity(), InteractionHand.MAIN_HAND);
		if (tool == null || tool.isBroken() || tool.getModifierLevel(this) < 1) {
			return;
		}
		if (event.getEntity() instanceof ServerPlayer player) {
			state.getBlock().playerWillDestroy(world, pos, state, player);
			boolean removed = world.removeBlock(pos, false);
			if (removed) {
				state.getBlock().destroy(world, pos, state);
				if (state.getBlock().getLootTable() == BuiltInLootTables.EMPTY) {
					Block.popResource(world, pos, new ItemStack(state.getBlock()));
				} else {
					Block.dropResources(state, world, pos, world.getBlockEntity(pos), player, player.getMainHandItem());
				}
				ToolDamageUtil.damageAnimated(tool, 1, player);
			}
		}
	}
}
