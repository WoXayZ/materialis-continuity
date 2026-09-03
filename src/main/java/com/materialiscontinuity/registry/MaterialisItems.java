package com.materialiscontinuity.registry;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.datagen.MaterialisToolDefinitions;
import com.materialiscontinuity.item.ExosuitModelArmorItem;
import com.materialiscontinuity.item.HeavenShotItem;
import com.materialiscontinuity.item.ManaShotItem;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

public class MaterialisItems {

	public static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(Materialis.MOD_ID);
	private static final Item.Properties ITEM_PROPS = new Item.Properties();
	private static final Item.Properties UNSTACKABLE_PROPS = new Item.Properties().stacksTo(1);

	public static final SlotType SENSOR_SLOT = SlotType.getOrCreate("sensor");
	public static final ModifiableArmorMaterial EXOSUIT = ModifiableArmorMaterial.create(Materialis.getResource("psimetal_exosuit"), SoundEvents.ARMOR_EQUIP_IRON);

	public static final ItemObject<Item> FAIRY_INGOT = ITEMS.register("fairy_ingot", () -> new Item(ITEM_PROPS));
	public static final ItemObject<Item> FAIRY_NUGGET = ITEMS.register("fairy_nugget", () -> new Item(ITEM_PROPS));
	public static final ItemObject<BlockItem> FAIRY_BLOCK_ITEM = ITEMS.register("fairy_block", () -> new BlockItem(MaterialisBlocks.FAIRY_BLOCK.get(), ITEM_PROPS));

	public static final CastItemObject INLAY_CAST = ITEMS.registerCast("inlay", ITEM_PROPS);
	public static final CastItemObject WRENCH_HEAD_CAST = ITEMS.registerCast("wrench_head", ITEM_PROPS);

	public static final ItemObject<ToolPartItem> WRENCH_HEAD = ITEMS.register("wrench_head", () -> new ToolPartItem(ITEM_PROPS, HeadMaterialStats.ID));
	public static final ItemObject<ModifiableItem> WRENCH = ITEMS.register("wrench", () -> new ModifiableItem(UNSTACKABLE_PROPS, MaterialisToolDefinitions.WRENCH));
	public static final ItemObject<ModifiableItem> BATTLEWRENCH = ITEMS.register("battlewrench", () -> new ModifiableItem(UNSTACKABLE_PROPS, MaterialisToolDefinitions.BATTLEWRENCH));
	public static final ItemObject<Item> MANASHOT = ITEMS.register("manashot", () -> new ManaShotItem(ITEM_PROPS));
	public static final ItemObject<Item> HEAVENSHOT = ITEMS.register("heavenshot", () -> new HeavenShotItem(ITEM_PROPS));

	public static final EnumObject<ArmorItem.Type, ModifiableArmorItem> PSIMETAL_EXOSUIT = ITEMS.registerEnum("psimetal_exosuit", ArmorItem.Type.values(), type -> new ExosuitModelArmorItem(EXOSUIT, type, UNSTACKABLE_PROPS));
}
