package com.materialiscontinuity.datagen;

import java.util.concurrent.CompletableFuture;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.registry.MaterialisBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MaterialisBlockTagsProvider extends BlockTagsProvider {

	public MaterialisBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Materialis.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(BlockTags.NEEDS_IRON_TOOL).add(MaterialisBlocks.FAIRY_BLOCK.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(MaterialisBlocks.FAIRY_BLOCK.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).add(MaterialisBlocks.FAIRY_BLOCK.get());

		tag(MaterialisBlockTags.MINEABLE_WITH_WRENCH)
				.addTag(Tags.Blocks.STORAGE_BLOCKS)
				.addTag(Tags.Blocks.BARRELS)
				.addTag(Tags.Blocks.CHESTS)
				.addTag(Tags.Blocks.GLASS)
				.addTag(Tags.Blocks.GLASS_PANES)
				.addTag(BlockTags.BUTTONS)
				.addTag(BlockTags.DOORS)
				.addTag(BlockTags.TRAPDOORS)
				.addTag(BlockTags.PRESSURE_PLATES)
				.addTag(BlockTags.BANNERS)
				.addTag(BlockTags.ANVIL)
				.addTag(BlockTags.RAILS)
				.addTag(BlockTags.BEDS)
				.addTag(BlockTags.SIGNS)
				.addTag(BlockTags.BEACON_BASE_BLOCKS);

		tag(MaterialisBlockTags.WRENCH_BLACKLIST);
	}
}
