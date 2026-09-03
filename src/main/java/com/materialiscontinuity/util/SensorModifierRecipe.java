package com.materialiscontinuity.util;

import java.util.List;

import javax.annotation.Nullable;

import com.materialiscontinuity.modifiers.PsionizingRadiationModifierSensor;
import com.materialiscontinuity.registry.MaterialisRecipeSerializers;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.ModList;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.primitive.StringLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.json.field.MergingField;
import slimeknights.tconstruct.library.json.field.MergingField.MissingMode;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.modifiers.adding.SwappableModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.tools.SlotType.SlotCount;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class SensorModifierRecipe extends SwappableModifierRecipe {

	public static final RecordLoadable<SensorModifierRecipe> LOADER = RecordLoadable.create(
			ContextKey.ID.requiredField(),
			INPUTS_FIELD, TOOLS_FIELD, MAX_TOOL_SIZE_FIELD,
			new MergingField<>(ModifierId.PARSER.requiredField("name", r -> r.result.getId()), "result", MissingMode.DISALLOWED),
			new MergingField<>(StringLoadable.DEFAULT.requiredField("value", r -> r.variantValue), "result", MissingMode.DISALLOWED),
			VariantFormatter.LOADER.defaultField("variant_formatter", VariantFormatter.DEFAULT, r -> VariantFormatter.DEFAULT),
			SLOTS_FIELD, ALLOW_CRYSTAL_FIELD,
			SensorModifierRecipe::new);

	private static final boolean PSI_LOADED = ModList.get().isLoaded("psi");
	private final String variantValue;

	public SensorModifierRecipe(ResourceLocation id, List<SizedIngredient> inputs, Ingredient toolRequirement, int maxToolSize, ModifierId result, String value, VariantFormatter variantFormatter, @Nullable SlotCount slots, boolean allowCrystal) {
		super(id, inputs, toolRequirement, maxToolSize, result, value, variantFormatter, slots, allowCrystal);
		this.variantValue = value;
	}

	@Override
	public RecipeResult<LazyToolStack> getValidatedResult(ITinkerStationContainer inv, RegistryAccess access) {
		ToolStack tool = inv.getTinkerable();
		ModifierId modifier = result.getId();
		boolean needsModifier;
		int level = tool.getUpgrades().getLevel(modifier);
		if (level == 0) {
			needsModifier = true;
			Component commonError = validatePrerequisites(tool);
			if (commonError != null) {
				return RecipeResult.failure(commonError);
			}
		} else {
			needsModifier = false;
		}

		tool = tool.copy();
		ToolDataNBT persistentData = tool.getPersistentData();
		if (needsModifier) {
			SlotCount slots = getSlots();
			if (slots != null) {
				persistentData.addSlots(slots.type(), -slots.count());
			}
		}
		persistentData.putString(modifier, variantValue);
		if (needsModifier) {
			tool.addModifier(result.getId(), 1);
		} else {
			tool.rebuildStats();
		}

		Component toolValidation = tool.tryValidate();
		if (toolValidation != null) {
			return RecipeResult.failure(toolValidation);
		}

		if (PSI_LOADED) {
			for (int i = 0; i < inv.getInputCount(); i++) {
				Item item = inv.getInput(i).getItem();
				if (isExosuitSensor(item)) {
					persistentData.put(PsionizingRadiationModifierSensor.SENSOR, inv.getInput(i).serializeNBT());
					break;
				}
			}
		}
		return success(tool, inv);
	}

	private static boolean isExosuitSensor(Item item) {
		try {
			return Class.forName("vazkii.psi.api.exosuit.IExosuitSensor").isInstance(item);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MaterialisRecipeSerializers.SENSOR_MODIFIER.get();
	}
}
