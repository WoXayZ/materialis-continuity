package com.materialiscontinuity.item;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.item.armor.MultilayerArmorItem;

public class ExosuitModelArmorItem extends MultilayerArmorItem {

	public ExosuitModelArmorItem(ModifiableArmorMaterial material, ArmorItem.Type slotType, Properties properties) {
		super(material, slotType, properties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Nonnull
			@Override
			public Model getGenericArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> original) {
				if (ModList.get().isLoaded("psi")) {
					try {
						return (Model) Class.forName("com.materialiscontinuity.util.ExosuitModel")
								.getMethod("getModel", LivingEntity.class, ItemStack.class, EquipmentSlot.class, HumanoidModel.class)
								.invoke(null, entityLiving, itemStack, armorSlot, original);
					} catch (ReflectiveOperationException ignored) {
					}
				}
				return original;
			}
		});
	}
}
