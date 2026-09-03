package com.materialiscontinuity.util;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.item.ManaShotItem.ManashotEntity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class ManashotRenderer extends ArrowRenderer<ManashotEntity> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Materialis.MOD_ID, "textures/models/manashot.png");

	public ManashotRenderer(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(ManashotEntity arrow) {
		return TEXTURE;
	}
}
