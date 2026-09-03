package com.materialiscontinuity.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.modifiers.ColorizedModifier;
import com.materialiscontinuity.modifiers.MaterialisModifiers;
import com.materialiscontinuity.modifiers.PsionizingRadiationModifierSensor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.client.model.ModModelLayers;
import vazkii.psi.client.model.ModelArmor;
import vazkii.psi.common.item.component.ItemCADColorizerPsi;

public class ExosuitModel extends Model {

	private static final Map<EquipmentSlot, ExosuitModel> MODELS = new HashMap<>();
	private static final Function<EquipmentSlot, ExosuitModel> CONSTRUCTOR = ExosuitModel::new;
	private static final ModifierId GOLDEN = new ModifierId("tconstruct", "golden");
	private static final ModifierId EMBELLISHMENT = new ModifierId("tconstruct", "embellishment");

	public static void clearCache() {
		MODELS.clear();
	}

	public static Model getModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> baseModel) {
		ExosuitModel model = MODELS.computeIfAbsent(slot, CONSTRUCTOR);
		model.setup(baseModel, living, stack, slot);
		return model;
	}

	private final Map<String, RenderType> ARMOR_RENDER_CACHE = new HashMap<>();
	private final Function<String, RenderType> ARMOR_GETTER = mat -> RenderType.entityCutoutNoCullZOffset(getArmorTexture(mat));
	private static final RenderType BASE_TEXTURE = RenderType.armorCutoutNoCull(new ResourceLocation(Materialis.MOD_ID, ModList.get().isLoaded("magipsi") ? "textures/models/armor/psimetal_exosuit_magical.png" : "textures/models/armor/psimetal_exosuit.png"));
	private static final RenderType OVERLAY_COLORIZER = RenderType.armorCutoutNoCull(new ResourceLocation(Materialis.MOD_ID, ModList.get().isLoaded("magipsi") ? "textures/models/armor/psimetal_exosuit_magical_colorizer.png" : "textures/models/armor/psimetal_exosuit_colorizer.png"));
	private static final RenderType OVERLAY_SENSOR = RenderType.armorCutoutNoCull(new ResourceLocation(Materialis.MOD_ID, ModList.get().isLoaded("magipsi") ? "textures/models/armor/psimetal_exosuit_magical_sensor.png" : "textures/models/armor/psimetal_exosuit_sensor.png"));

	@Nullable
	private ModelArmor exosuitModel;
	private String material = "";
	private ItemStack colorizer = ItemStack.EMPTY;
	private ItemStack sensor = ItemStack.EMPTY;
	private boolean hasGlint = false;
	private EquipmentSlot slot;

	public ExosuitModel(EquipmentSlot slot) {
		super(RenderType::entityCutoutNoCull);
		this.slot = slot;
	}

	private ModelArmor getExosuitModel() {
		if (exosuitModel == null) {
			exosuitModel = new ModelArmor(Minecraft.getInstance().getEntityModels().bakeLayer(slot == EquipmentSlot.LEGS ? ModModelLayers.PSIMETAL_EXOSUIT_INNER_ARMOR : ModModelLayers.PSIMETAL_EXOSUIT_OUTER_ARMOR), slot);
		}
		return exosuitModel;
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if (ArmorModelHelper.buffer != null) {
			VertexConsumer buffer = ArmorModelHelper.buffer.getBuffer(BASE_TEXTURE);
			getExosuitModel().renderToBuffer(matrices, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (!material.isEmpty()) {
				VertexConsumer overlayBuffer = ArmorModelHelper.buffer.getBuffer(ARMOR_RENDER_CACHE.computeIfAbsent(material, ARMOR_GETTER));
				getExosuitModel().renderToBuffer(matrices, overlayBuffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			}
			if (!colorizer.isEmpty()) {
				int color = getColorizerColor(colorizer);
				VertexConsumer overlayBuffer = ArmorModelHelper.buffer.getBuffer(OVERLAY_COLORIZER);
				getExosuitModel().renderToBuffer(matrices, overlayBuffer, 12583120, packedOverlayIn, (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha);
			}
			int color = getSensorColor(sensor);
			VertexConsumer overlayBuffer = ItemRenderer.getArmorFoilBuffer(ArmorModelHelper.buffer, OVERLAY_SENSOR, false, hasGlint);
			getExosuitModel().renderToBuffer(matrices, overlayBuffer, 12583120, packedOverlayIn, (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha);
		}
	}

	private ResourceLocation getArmorTexture(String material) {
		MaterialVariantId variantId = MaterialVariantId.tryParse(material);
		if (variantId == null) {
			variantId = MaterialIds.cobalt;
		}
		ResourceLocation location = variantId.getLocation('_');
		if (ModList.get().isLoaded("magipsi")) {
			return new ResourceLocation(Materialis.MOD_ID, String.format("textures/models/armor/exosuit_magical/accent_%s_%s.png", location.getNamespace(), location.getPath()));
		}
		return new ResourceLocation(Materialis.MOD_ID, String.format("textures/models/armor/exosuit/accent_%s_%s.png", location.getNamespace(), location.getPath()));
	}

	private void setup(HumanoidModel<?> base, LivingEntity living, ItemStack stack, EquipmentSlot slot) {
		if (ModifierUtil.getModifierLevel(stack, GOLDEN) > 0) {
			material = MaterialIds.gold.toString();
		} else {
			material = ModifierUtil.getPersistentString(stack, EMBELLISHMENT);
		}
		if (ModifierUtil.getModifierLevel(stack, MaterialisModifiers.colorizedModifier.getId()) > 0) {
			this.colorizer = ItemStack.of(ToolStack.from(stack).getPersistentData().getCompound(ColorizedModifier.COLORIZER));
		} else {
			this.colorizer = ItemStack.EMPTY;
		}
		sensor = ItemStack.of(ToolStack.copyFrom(stack).getVolatileData().getCompound(PsionizingRadiationModifierSensor.SENSOR));
		if (sensor.isEmpty()) {
			sensor = ItemStack.of(ToolStack.copyFrom(stack).getPersistentData().getCompound(PsionizingRadiationModifierSensor.SENSOR));
		}

		ModelArmor model = getExosuitModel();
		ArmorModelHelper.copyProperties(base, model);
		model.head.copyFrom(base.head);
		model.body.copyFrom(base.body);
		model.leftArm.copyFrom(base.leftArm);
		model.rightArm.copyFrom(base.rightArm);
		model.leftLeg.copyFrom(base.leftLeg);
		model.rightLeg.copyFrom(base.rightLeg);
		hasGlint = stack.hasFoil();
	}

	public int getColorizerColor(ItemStack stack) {
		if (stack.getItem() instanceof ItemCADColorizerPsi) {
			return ColorizedModifier.getPsiColor();
		}
		if (stack.getItem() instanceof ICADColorizer colorizerItem) {
			return colorizerItem.getColor(stack);
		}
		return 0xFFFFFF;
	}

	public int getSensorColor(ItemStack sensor) {
		if (!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor exosuitSensor) {
			return exosuitSensor.getColor(sensor);
		}
		return 0x82190A;
	}
}
