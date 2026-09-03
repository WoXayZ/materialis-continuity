package com.materialiscontinuity.util;

import java.util.function.Supplier;

import com.materialiscontinuity.modifiers.TerrabeamModifier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;


public class PacketTerraBeam {

	public static void encode(PacketTerraBeam msg, FriendlyByteBuf buf) {}

	public static PacketTerraBeam decode(FriendlyByteBuf buf) {
		return new PacketTerraBeam();
	}

	public static void handle(PacketTerraBeam msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> {
				if (ctx.get().getSender() != null) {
					TerrabeamModifier.BurstHandler.trySpawnBurst(ctx.get().getSender(), InteractionHand.MAIN_HAND, true, true);
				}
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
