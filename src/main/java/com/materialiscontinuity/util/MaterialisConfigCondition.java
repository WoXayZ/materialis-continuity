package com.materialiscontinuity.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.MaterialisConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class MaterialisConfigCondition implements ICondition {

	public static final ResourceLocation ID = new ResourceLocation(Materialis.MOD_ID, "config");
	public static final ConfigSerializer SERIALIZER = new ConfigSerializer();
	private static final Map<String, MaterialisConfigCondition> PROPS = new HashMap<>();

	private final String configName;
	private final BooleanSupplier supplier;

	private MaterialisConfigCondition(String configName, BooleanSupplier supplier) {
		this.configName = configName;
		this.supplier = supplier;
	}

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		return supplier.getAsBoolean();
	}

	public static class ConfigSerializer implements IConditionSerializer<MaterialisConfigCondition> {
		@Override
		public ResourceLocation getID() {
			return ID;
		}

		@Override
		public void write(JsonObject json, MaterialisConfigCondition value) {
			json.addProperty("prop", value.configName);
		}

		@Override
		public MaterialisConfigCondition read(JsonObject json) {
			String prop = GsonHelper.getAsString(json, "prop");
			MaterialisConfigCondition config = PROPS.get(prop.toLowerCase(Locale.ROOT));
			if (config == null) {
				throw new JsonSyntaxException("Invalid property name '" + prop + "'");
			}
			return config;
		}
	}

	private static MaterialisConfigCondition add(String prop, BooleanSupplier supplier) {
		MaterialisConfigCondition conf = new MaterialisConfigCondition(prop, supplier);
		PROPS.put(prop.toLowerCase(Locale.ROOT), conf);
		return conf;
	}

	private static MaterialisConfigCondition add(String prop, BooleanValue supplier) {
		return add(prop, supplier::get);
	}

	@Override
	public String toString() {
		return "config_setting_enabled(\"" + this.configName + "\")";
	}

	public static final MaterialisConfigCondition CHROMATIC_MATERIALS = add("chromatic_materials", MaterialisConfig.ENABLE_CHROMATIC_MATERIALS);
	public static final MaterialisConfigCondition CHROMATIC_RECIPE = add("chromatic_recipe", MaterialisConfig.ADD_CHROMATIC_RECIPE);
}
