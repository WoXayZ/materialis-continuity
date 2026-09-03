package com.materialiscontinuity.util;

import com.materialiscontinuity.Materialis;

import net.minecraft.resources.ResourceLocation;

public final class SpellSockets {

	public static final int MAX_SLOTS = 12;
	public static final ResourceLocation SOCKETS = Materialis.getResource("spell_sockets");
	public static final ResourceLocation SELECTED_SPELL = Materialis.getResource("selected_slot");
	public static final ResourceLocation CAN_LOOPCAST = Materialis.getResource("can_loopcast");
	public static final ResourceLocation SHOW_PSI_BAR = Materialis.getResource("show_psi_bar");
	public static final ResourceLocation TIMES_CAST = Materialis.getResource("times_cast");
	public static final ResourceLocation SOCKET_OWNER = Materialis.getResource("socket_owner");
	public static final ResourceLocation[] SPELL_SLOTS = new ResourceLocation[] {
			Materialis.getResource("spell_slot_0"),
			Materialis.getResource("spell_slot_1"),
			Materialis.getResource("spell_slot_2"),
			Materialis.getResource("spell_slot_3"),
			Materialis.getResource("spell_slot_4"),
			Materialis.getResource("spell_slot_5"),
			Materialis.getResource("spell_slot_6"),
			Materialis.getResource("spell_slot_7"),
			Materialis.getResource("spell_slot_8"),
			Materialis.getResource("spell_slot_9"),
			Materialis.getResource("spell_slot_10"),
			Materialis.getResource("spell_slot_11")
	};

	public static void addToVolatileInt(ResourceLocation tag, slimeknights.tconstruct.library.tools.nbt.ToolDataNBT volatileData, int add) {
		if (volatileData.contains(tag, net.minecraft.nbt.Tag.TAG_INT)) {
			volatileData.putInt(tag, volatileData.getInt(tag) + add);
		} else {
			volatileData.putInt(tag, add);
		}
	}

	private SpellSockets() {}
}
