package com.materialiscontinuity.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeavenShotItem extends ArrowItem {

	public HeavenShotItem(Properties prop) {
		super(prop);
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
		Arrow arrow = new Arrow(level, shooter);
		arrow.setBaseDamage(Float.MAX_VALUE);
		arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
		return arrow;
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
		return true;
	}
}
