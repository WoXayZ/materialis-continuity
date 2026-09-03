package com.materialiscontinuity.registry;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.util.ColorizerModifierRecipe;
import com.materialiscontinuity.util.SensorModifierRecipe;
import com.materialiscontinuity.util.TinkerSpellWriteRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;

public class MaterialisRecipeSerializers {

	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Materialis.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Materialis.MOD_ID);

	public static final RegistryObject<RecipeSerializer<SensorModifierRecipe>> SENSOR_MODIFIER = RECIPE_SERIALIZERS.register("sensor_modifier", () -> LoadableRecipeSerializer.of(SensorModifierRecipe.LOADER));
	public static final RegistryObject<RecipeSerializer<ColorizerModifierRecipe>> COLORIZER_MODIFIER = RECIPE_SERIALIZERS.register("colorizer_modifier", () -> LoadableRecipeSerializer.of(ColorizerModifierRecipe.LOADER));
	public static final RegistryObject<RecipeType<TinkerSpellWriteRecipe>> TINKER_SPELL_WRITE_TYPE;
	public static final RegistryObject<RecipeSerializer<TinkerSpellWriteRecipe>> TINKER_SPELL_WRITE;

	static {
		if (ModList.get().isLoaded("ars_nouveau")) {
			TINKER_SPELL_WRITE_TYPE = RECIPE_TYPES.register("tinker_spell_write", () -> RecipeType.simple(Materialis.getResource("tinker_spell_write")));
			TINKER_SPELL_WRITE = RECIPE_SERIALIZERS.register("tinker_spell_write", TinkerSpellWriteRecipe.Serializer::new);
		} else {
			TINKER_SPELL_WRITE_TYPE = null;
			TINKER_SPELL_WRITE = null;
		}
	}
}
