package com.materialiscontinuity;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class MaterialisConfig {

	public static ForgeConfigSpec.BooleanValue ENABLE_CHROMATIC_MATERIALS;
	public static ForgeConfigSpec.BooleanValue ADD_CHROMATIC_RECIPE;

	public static void register() {
		registerCommonConfigs();
	}

	public static void registerCommonConfigs() {
		ForgeConfigSpec.Builder COMMON = new ForgeConfigSpec.Builder();
		COMMON.comment("Create compatibility options").push("create");

		ENABLE_CHROMATIC_MATERIALS = COMMON.comment("Whether refined radiance and shadow steel materials should be enabled").define("chromaticMaterials", true);
		ADD_CHROMATIC_RECIPE = COMMON.comment("Whether the recipe for chromatic compound should be added back").define("chromaticRecipe", true);

		COMMON.pop();
		ModLoadingContext.get().registerConfig(Type.COMMON, COMMON.build());
	}
}
