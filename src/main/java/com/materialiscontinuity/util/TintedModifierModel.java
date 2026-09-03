package com.materialiscontinuity.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.mojang.math.Transformation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import slimeknights.mantle.client.model.util.MantleItemLayerModel;
import slimeknights.mantle.data.loadable.common.ColorLoadable;
import slimeknights.mantle.util.ItemLayerPixels;
import slimeknights.tconstruct.library.client.modifiers.IBakedModifierModel;
import slimeknights.tconstruct.library.client.modifiers.IUnbakedModifierModel;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

/**
 * Model for modifiers that can tint their own texture and/or have a glowing texture
 */
public class TintedModifierModel implements IBakedModifierModel {

	/** Constant unbaked model instance, as they are all the same */
	public static final IUnbakedModifierModel UNBAKED_INSTANCE = new Unbaked(-1);

	/** Textures to show */
	private final Material[] textures;
	/** Color to apply to the texture */
	private final int color;

	public TintedModifierModel(@Nullable Material smallTexture, @Nullable Material largeTexture, int color) {
		this.color = color;
		this.textures = new Material[]{ smallTexture, largeTexture };
	}

	public TintedModifierModel(@Nullable Material smallTexture, @Nullable Material largeTexture) {
		this(smallTexture, largeTexture, -1);
	}

	@Nullable
	@Override
	public Object getCacheKey(IToolStackView tool, ModifierEntry entry) {
		if (entry.getModifier() instanceof ITintingModifier tinting) {
			return new GlowingModifierCacheKey(entry.getModifier(), tinting.getLuminosity(tool), tinting.getTint(tool));
		}
		return entry.getModifier();
	}

	@Override
	public void addQuads(IToolStackView tool, ModifierEntry modifier, Function<Material, TextureAtlasSprite> spriteGetter, Transformation transforms, boolean isLarge, int startTintIndex, Consumer<Collection<BakedQuad>> quadConsumer, @Nullable ItemLayerPixels pixels) {
		Material texture = textures[isLarge ? 1 : 0];
		if (texture != null) {
			int luminosity = 0;
			if (modifier.getModifier() instanceof ITintingModifier tinting) {
				luminosity = tinting.getLuminosity(tool);
			}
			quadConsumer.accept(MantleItemLayerModel.getQuadsForSprite(color, startTintIndex, spriteGetter.apply(texture), transforms, luminosity, pixels));
		}
	}

	@Override
	public int getTintIndexes() {
		return 1;
	}

	@Override
	public int getTint(IToolStackView tool, ModifierEntry entry, int index) {
		if (entry.getModifier() instanceof ITintingModifier tinting) {
			return tinting.getTint(tool);
		}
		return -1;
	}

	@Data
	private static class GlowingModifierCacheKey {
		private final Modifier modifier;
		private final int luminosity;
		private final int tint;
	}

	@RequiredArgsConstructor
	private static class Unbaked implements IUnbakedModifierModel {
		private final int color;

		@Nullable
		@Override
		public IBakedModifierModel forTool(Function<String, Material> smallGetter, Function<String, Material> largeGetter) {
			Material smallTexture = smallGetter.apply("");
			Material largeTexture = largeGetter.apply("");
			if (smallTexture != null || largeTexture != null) {
				return new TintedModifierModel(smallTexture, largeTexture, color);
			}
			return null;
		}

		@Override
		public IUnbakedModifierModel configure(JsonObject data) {
			int parsed = ColorLoadable.ALPHA.getOrDefault(data, "color", -1);
			if (parsed != this.color) {
				return new Unbaked(parsed);
			}
			return this;
		}
	}
}
