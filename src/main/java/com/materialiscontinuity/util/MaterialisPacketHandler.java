package com.materialiscontinuity.util;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import com.materialiscontinuity.Materialis;

public class MaterialisPacketHandler {

	private static final String PROTOCOL = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			Materialis.getResource("main"),
			() -> PROTOCOL,
			PROTOCOL::equals,
			PROTOCOL::equals);

	private static int id = 0;

	public static void init() {
		if (ModList.get().isLoaded("botania")) {
			INSTANCE.registerMessage(id++, PacketTerraBeam.class, PacketTerraBeam::encode, PacketTerraBeam::decode, PacketTerraBeam::handle);
			INSTANCE.registerMessage(id++, PacketElvenBeam.class, PacketElvenBeam::encode, PacketElvenBeam::decode, PacketElvenBeam::handle);
		}
		if (ModList.get().isLoaded("ars_nouveau")) {
			INSTANCE.registerMessage(id++, PacketReactiveSwing.class, PacketReactiveSwing::encode, PacketReactiveSwing::decode, PacketReactiveSwing::handle);
		}
	}
}
