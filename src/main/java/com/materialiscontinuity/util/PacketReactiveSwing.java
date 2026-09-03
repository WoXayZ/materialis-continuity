package com.materialiscontinuity.util;

import java.util.function.Supplier;

import com.materialiscontinuity.modifiers.ReactiveModifier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketReactiveSwing {

	public static void encode(PacketReactiveSwing msg, FriendlyByteBuf buf) {}

	public static PacketReactiveSwing decode(FriendlyByteBuf buf) {
		return new PacketReactiveSwing();
	}

	public static void handle(PacketReactiveSwing msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> {
				if (ctx.get().getSender() != null) {
					ReactiveModifier.receiveLeftClick(ctx.get().getSender());
				}
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
