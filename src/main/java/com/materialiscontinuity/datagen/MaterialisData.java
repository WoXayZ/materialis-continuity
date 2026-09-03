package com.materialiscontinuity.datagen;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.modifiers.MaterialisModifiers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class MaterialisData {

	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		boolean server = event.includeServer();
		generator.addProvider(server, new MaterialisModifiers(packOutput));
		generator.addProvider(server, new MaterialisBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
	}
}
