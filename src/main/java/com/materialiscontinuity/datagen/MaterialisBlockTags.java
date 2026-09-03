package com.materialiscontinuity.datagen;

import com.materialiscontinuity.Materialis;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MaterialisBlockTags {

	public static final TagKey<Block> WRENCH_BLACKLIST = BlockTags.create(new ResourceLocation(Materialis.MOD_ID, "wrench_blacklist"));
	public static final TagKey<Block> MINEABLE_WITH_WRENCH = BlockTags.create(new ResourceLocation(Materialis.MOD_ID, "mineable/wrench"));
}
