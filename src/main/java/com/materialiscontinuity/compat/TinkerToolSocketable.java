package com.materialiscontinuity.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.materialiscontinuity.util.SpellSockets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider.IToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;

public class TinkerToolSocketable implements IToolCapabilityProvider, ISocketable, IPsiBarDisplay, ISpellAcceptor {

	protected final Supplier<? extends IToolStackView> tool;
	private final LazyOptional<?> capOptional;

	public TinkerToolSocketable(ItemStack stack, Supplier<? extends IToolStackView> toolStack) {
		this.tool = toolStack;
		this.capOptional = LazyOptional.of(() -> this);
	}

	public static void registerCapability() {
		slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider.register(TinkerToolSocketable::new);
	}

	public int getSlots() {
		IModDataView volatileData = tool.get().getVolatileData();
		if (volatileData.contains(SpellSockets.SOCKETS, Tag.TAG_INT))
			return Math.min(volatileData.getInt(SpellSockets.SOCKETS), MAX_ASSEMBLER_SLOTS);
		return 0;
	}

	@Override
	public <T> LazyOptional<T> getCapability(IToolStackView tool, Capability<T> cap) {
		if ((cap == PsiAPI.SOCKETABLE_CAPABILITY || cap == PsiAPI.PSI_BAR_DISPLAY_CAPABILITY || cap == PsiAPI.SPELL_ACCEPTOR_CAPABILITY) && tool.getVolatileData().getInt(SpellSockets.SOCKETS) > 0) {
			return capOptional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public boolean isSocketSlotAvailable(int slot) {
		return slot < getSlots();
	}

	@Override
	public List<Integer> getRadialMenuSlots() {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i <= getSlots(); i++) {
			list.add(i);
		}
		return list;
	}

	@Override
	public ItemStack getBulletInSocket(int slot) {
		IModDataView persistentData = tool.get().getPersistentData();
		if (slot >= SpellSockets.SPELL_SLOTS.length || !persistentData.contains(SpellSockets.SPELL_SLOTS[slot], Tag.TAG_COMPOUND))
			return ItemStack.EMPTY;
		CompoundTag cmp = persistentData.getCompound(SpellSockets.SPELL_SLOTS[slot]);

		if (cmp.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return ItemStack.of(cmp);
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		CompoundTag cmp = new CompoundTag();
		tool.get().getPersistentData().putInt(SpellSockets.TIMES_CAST, 0);

		if (bullet.isEmpty()) {
			tool.get().getPersistentData().remove(SpellSockets.SPELL_SLOTS[slot]);
			return;
		}
		cmp = bullet.save(cmp);
		tool.get().getPersistentData().put(SpellSockets.SPELL_SLOTS[slot], cmp);
	}

	@Override
	public int getSelectedSlot() {
		IModDataView persistentData = tool.get().getPersistentData();
		if (persistentData.contains(SpellSockets.SELECTED_SPELL, Tag.TAG_INT))
			return persistentData.getInt(SpellSockets.SELECTED_SPELL);
		return 0;
	}

	@Override
	public void setSelectedSlot(int slot) {
		tool.get().getPersistentData().putInt(SpellSockets.SELECTED_SPELL, slot);
		tool.get().getPersistentData().putInt(SpellSockets.TIMES_CAST, 0);
	}

	@Override
	public boolean shouldShow(IPlayerData data) {
		return tool.get().getVolatileData().getBoolean(SpellSockets.SHOW_PSI_BAR);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack bullet) {
		if (!isSocketSlotAvailable(slot) || !ISpellAcceptor.isContainer(bullet))
			return false;
		return canLoopcast() || !ISpellAcceptor.acceptor(bullet).isCADOnlyContainer();
	}

	@Override
	public boolean canLoopcast() {
		return tool.get().getVolatileData().getBoolean(SpellSockets.CAN_LOOPCAST);
	}

	@Override
	public void setSpell(Player player, Spell spell) {
		int slot = getSelectedSlot();
		ItemStack bullet = getBulletInSocket(slot);
		if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
			ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
			setBulletInSocket(slot, bullet);
		}
	}

	@Override
	public boolean castableFromSocket() {
		return false;
	}
}
