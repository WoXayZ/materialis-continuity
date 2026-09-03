package com.materialiscontinuity.item;

import com.materialiscontinuity.registry.MaterialisEntities;
import com.materialiscontinuity.registry.MaterialisItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ManaShotItem extends ArrowItem {

	public ManaShotItem(Properties prop) {
		super(prop);
	}

	@Override
	public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
		return new ManashotEntity(pLevel, pShooter);
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
		return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
	}

	public static class ManashotEntity extends AbstractArrow {

		public ManashotEntity(EntityType<? extends ManashotEntity> type, Level level) {
			super(type, level);
			pickup = Pickup.CREATIVE_ONLY;
		}

		public ManashotEntity(Level level, LivingEntity shooter) {
			super(MaterialisEntities.MANASHOT.get(), shooter, level);
			pickup = Pickup.CREATIVE_ONLY;
		}

		@Override
		public ItemStack getPickupItem() {
			return new ItemStack(MaterialisItems.MANASHOT.get());
		}
	}
}
