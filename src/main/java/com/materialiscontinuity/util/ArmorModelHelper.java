package com.materialiscontinuity.util;

import javax.annotation.Nullable;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;

public class ArmorModelHelper {
	@Nullable
	static MultiBufferSource buffer;

	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, RenderLivingEvent.Pre.class, event -> buffer = event.getMultiBufferSource());
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, RenderLivingEvent.Post.class, event -> buffer = null);
	}

	@SuppressWarnings("unchecked")
	static <T extends LivingEntity> void copyProperties(EntityModel<T> base, EntityModel<?> other) {
		base.copyPropertiesTo((EntityModel<T>) other);
	}
}
