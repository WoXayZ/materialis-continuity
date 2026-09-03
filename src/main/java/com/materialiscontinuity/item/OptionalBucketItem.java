package com.materialiscontinuity.item;

import java.util.function.Supplier;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class OptionalBucketItem extends BucketItem {

	public final ICondition condition;

	public OptionalBucketItem(Supplier<? extends Fluid> supplier, Properties builder, ICondition condition) {
		super(supplier, builder);
		this.condition = condition;
	}

	public OptionalBucketItem(Fluid fluid, Properties builder, ICondition condition) {
		super(fluid, builder);
		this.condition = condition;
	}
}
