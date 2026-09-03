package com.materialiscontinuity.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class InfinityTier implements Tier {

	public static final Tier instance = new InfinityTier();

	@Override
	public int getUses() {
		return Integer.MAX_VALUE;
	}

	@Override
	public float getSpeed() {
		return Float.MAX_VALUE;
	}

	@Override
	public float getAttackDamageBonus() {
		return Float.MAX_VALUE;
	}

	@Override
	public TagKey<Block> getTag() {
		return BlockTags.NEEDS_DIAMOND_TOOL;
	}

	@Override
	public int getLevel() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getEnchantmentValue() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return null;
	}

}
