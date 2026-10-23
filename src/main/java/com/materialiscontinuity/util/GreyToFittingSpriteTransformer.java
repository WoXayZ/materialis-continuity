package com.materialiscontinuity.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.mojang.blaze3d.platform.NativeImage;
import com.materialiscontinuity.Materialis;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.mantle.data.loadable.common.ColorLoadable;
import slimeknights.mantle.util.JsonHelper;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping.Interpolate;
import slimeknights.tconstruct.library.client.data.spritetransformer.IRecolorSpriteTransformer;
import slimeknights.tconstruct.library.client.data.spritetransformer.ISpriteTransformer;
import slimeknights.tconstruct.library.client.data.util.AbstractSpriteReader;
import slimeknights.tconstruct.library.client.data.util.DataGenSpriteReader;
import slimeknights.tconstruct.library.client.data.util.ResourceManagerSpriteReader;
import slimeknights.tconstruct.library.utils.Util;

/**
 * Like Tinkers' grey-to-sprite transformer, but scales palette sprites to the destination part size
 * instead of tiling them. Needed for Infinity's looping textures.
 */
public class GreyToFittingSpriteTransformer implements IRecolorSpriteTransformer {

	public static final ResourceLocation NAME = Materialis.getResource("grey_to_fitting_sprite");
	public static final Deserializer DESERIALIZER = new Deserializer();

	private static final String TEXTURE_FOLDER = "textures";
	@Nullable
	private static AbstractSpriteReader READER = null;
	private static final List<SpriteMapping> MAPPINGS_TO_CLEAR = new ArrayList<>();

	private final List<SpriteMapping> sprites;
	private final SpriteRange[] foundSpriteCache = new SpriteRange[256];
	private static final Interpolate<SpriteMapping, SpriteRange> SPRITE_RANGE = (first, second, grey) -> new SpriteRange(first, second);
	private static final ToIntFunction<SpriteMapping> GET_GREY = SpriteMapping::grey;

	public GreyToFittingSpriteTransformer(List<SpriteMapping> sprites) {
		this.sprites = sprites;
	}

	private SpriteRange getSpriteRange(int grey) {
		if (foundSpriteCache[grey] == null) {
			foundSpriteCache[grey] = GreyToColorMapping.getNearestByGrey(sprites, GET_GREY, grey, SPRITE_RANGE);
		}
		return foundSpriteCache[grey];
	}

	private int recolor(int color, int x, int y, int width, int height) {
		if (FastColor.ABGR32.alpha(color) == 0) {
			return 0x00000000;
		}
		int grey = GreyToColorMapping.getGrey(color);
		int newColor = getSpriteRange(grey).getColor(x, y, width, height, grey);
		return GreyToColorMapping.scaleColor(color, newColor, grey);
	}

	@Override
	public int getNewColor(int color, int x, int y, int frame) {
		return recolor(color, x, y, 1, 1);
	}

	@Override
	public void transform(NativeImage image, boolean allowAnimated) {
		int width = image.getWidth();
		int height = image.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setPixelRGBA(x, y, recolor(image.getPixelRGBA(x, y), x, y, width, height));
			}
		}
	}

	@Override
	public int getFallbackColor() {
		return getSpriteRange(216).getColor(0, 0, 1, 1, 216);
	}

	@Override
	public JsonObject serialize(JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		object.addProperty("type", NAME.toString());
		JsonArray colors = new JsonArray();
		for (SpriteMapping mapping : sprites) {
			JsonObject pair = new JsonObject();
			pair.addProperty("grey", mapping.grey);
			if (mapping.color != -1 || mapping.path == null) {
				pair.addProperty("color", String.format("%08X", Util.translateColorBGR(mapping.color)));
			}
			if (mapping.path != null) {
				pair.addProperty("path", mapping.path.toString());
			}
			colors.add(pair);
		}
		object.add("palette", colors);
		return object;
	}

	protected static class Deserializer implements JsonDeserializer<GreyToFittingSpriteTransformer> {
		@Override
		public GreyToFittingSpriteTransformer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject object = json.getAsJsonObject();
			JsonArray palette = GsonHelper.getAsJsonArray(object, "palette");
			Builder paletteBuilder = builderFromBlack();
			for (int i = 0; i < palette.size(); i++) {
				JsonObject palettePair = GsonHelper.convertToJsonObject(palette.get(i), "palette[" + i + ']');
				int grey = GsonHelper.getAsInt(palettePair, "grey");
				if (i == 0 && grey != 0) {
					paletteBuilder.addABGR(0, 0xFF000000);
				}
				int color = -1;
				if (palettePair.has("color")) {
					color = ColorLoadable.ALPHA.parseString(GsonHelper.getAsString(palettePair, "color"), "color");
				}
				if (palettePair.has("path")) {
					paletteBuilder.addTexture(grey, JsonHelper.getResourceLocation(palettePair, "path"), color);
				} else {
					paletteBuilder.addARGB(grey, color);
				}
			}
			return paletteBuilder.build();
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builderFromBlack() {
		return builder().addABGR(0, 0xFF000000);
	}

	public static class Builder {
		private final ImmutableList.Builder<SpriteMapping> builder = ImmutableList.builder();
		private int lastGrey = -1;

		private void checkGrey(int grey) {
			if (grey < 0 || grey > 255) {
				throw new IllegalArgumentException("Invalid grey value, must be between 0 and 255, inclusive");
			}
			if (grey <= lastGrey) {
				throw new IllegalArgumentException("Grey value must be greater than the previous value");
			}
			lastGrey = grey;
		}

		public Builder addABGR(int grey, int color) {
			checkGrey(grey);
			builder.add(new SpriteMapping(grey, color, null));
			return this;
		}

		public Builder addARGB(int grey, int color) {
			return addABGR(grey, Util.translateColorBGR(color));
		}

		public Builder addTexture(int grey, ResourceLocation texture, int tint) {
			checkGrey(grey);
			builder.add(new SpriteMapping(grey, Util.translateColorBGR(tint), texture));
			return this;
		}

		public GreyToFittingSpriteTransformer build() {
			List<SpriteMapping> list = builder.build();
			if (list.size() < 2) {
				throw new IllegalStateException("Too few colors in palette, must have at least 2");
			}
			return new GreyToFittingSpriteTransformer(list);
		}
	}

	private static class SpriteMapping {
		private final int grey;
		private final int color;
		@Nullable
		private final ResourceLocation path;
		@Nullable
		private NativeImage image;

		SpriteMapping(int grey, int color, @Nullable ResourceLocation path) {
			this.grey = grey;
			this.color = color;
			this.path = path;
		}

		int grey() {
			return grey;
		}

		int getColor(int x, int y, int width, int height) {
			if (path != null) {
				NativeImage sprite = getImage();
				if (sprite != null) {
					int spriteColor = sprite.getPixelRGBA(
							(x * sprite.getWidth()) / Math.max(1, width),
							(y * sprite.getHeight()) / Math.max(1, height));
					if (color != -1) {
						spriteColor = GreyToColorMapping.scaleColor(spriteColor, color, 255);
					}
					return spriteColor;
				}
			}
			return color;
		}

		@Nullable
		private NativeImage getImage() {
			if (path != null && image == null) {
				if (READER == null) {
					throw new IllegalStateException("Cannot get image for a sprite without reader");
				}
				try {
					image = READER.read(path);
				} catch (IOException ex) {
					throw new IllegalStateException("Failed to load required image", ex);
				}
				MAPPINGS_TO_CLEAR.add(this);
			}
			return image;
		}
	}

	private record SpriteRange(@Nullable SpriteMapping before, @Nullable SpriteMapping after) {
		int getColor(int x, int y, int width, int height, int grey) {
			if (before == null) {
				assert after != null;
				return after.getColor(x, y, width, height);
			}
			if (after == null || before == after) {
				return before.getColor(x, y, width, height);
			}
			return GreyToColorMapping.interpolateColors(
					before.getColor(x, y, width, height), before.grey(),
					after.getColor(x, y, width, height), after.grey(),
					grey);
		}
	}

	private static boolean init;

	public static void init() {
		if (!init) {
			init = true;
			ISpriteTransformer.SERIALIZER.registerDeserializer(NAME, DESERIALIZER);
			MaterialPartTextureGenerator.registerCallback(GreyToFittingSpriteTransformer::textureCallback);
		}
	}

	private static void textureCallback(@Nullable ExistingFileHelper existingFileHelper, @Nullable ResourceManager manager) {
		if (READER != null) {
			MAPPINGS_TO_CLEAR.forEach(mapping -> mapping.image = null);
			MAPPINGS_TO_CLEAR.clear();
			READER.closeAll();
			READER = null;
		}
		if (existingFileHelper != null) {
			READER = new DataGenSpriteReader(existingFileHelper, TEXTURE_FOLDER);
		} else if (manager != null) {
			READER = new ResourceManagerSpriteReader(manager, TEXTURE_FOLDER);
		}
	}
}
