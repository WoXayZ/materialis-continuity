package com.materialiscontinuity.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class OptionalItem extends Item {

	public final ICondition condition;

	public OptionalItem(Properties prop, ICondition condition) {
		super(prop);
		this.condition = condition;
	}
}
