package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.Materialis;

import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.RawDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.utils.RestrictedCompoundTag;

public class OtherworldlyModifier extends Modifier implements RawDataModifierHook {

	public static final String OTHERWORLD_TIER = "occultism:otherworldToolTier";

	public final int tier;

	public OtherworldlyModifier(int tier) {
		this.tier = tier;
	}

	@Override
	protected void registerHooks(Builder hookBuilder) {
		hookBuilder.addHook(this, ModifierHooks.RAW_DATA);
	}

	@Override
	public void addRawData(IToolStackView tool, ModifierEntry modifier, RestrictedCompoundTag tag) {
		if (tag.contains(OTHERWORLD_TIER, Tag.TAG_INT)) {
			tag.putInt(OTHERWORLD_TIER, Math.max(tag.getInt(OTHERWORLD_TIER), tier));
		} else {
			tag.putInt(OTHERWORLD_TIER, tier);
		}
	}

	@Override
	public void removeRawData(IToolStackView tool, Modifier modifier, RestrictedCompoundTag tag) {
		tag.remove(OTHERWORLD_TIER);
	}

	@Override
	public Component getDisplayName(int level) {
		return super.getDisplayName(tier);
	}

	@Override
	protected String makeTranslationKey() {
		return "modifier." + Materialis.MOD_ID + ".otherworldly";
	}
}
