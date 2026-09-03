package com.materialiscontinuity;

import com.materialiscontinuity.registry.MaterialisBlocks;
import com.materialiscontinuity.registry.MaterialisFluids;
import com.materialiscontinuity.registry.MaterialisItems;

import net.minecraft.world.item.Item;

import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

/** Compatibility aliases for code still referencing the 1.18 MaterialisResources layout. */
public class MaterialisResources {

	public static final ItemObject<?> FAIRY_INGOT = MaterialisItems.FAIRY_INGOT;
	public static final ItemObject<?> FAIRY_NUGGET = MaterialisItems.FAIRY_NUGGET;
	public static final ItemObject<?> FAIRY_BLOCK_ITEM = MaterialisItems.FAIRY_BLOCK_ITEM;

	public static final CastItemObject INLAY_CAST = MaterialisItems.INLAY_CAST;
	public static final CastItemObject WRENCH_HEAD_CAST = MaterialisItems.WRENCH_HEAD_CAST;
	public static final ItemObject<ToolPartItem> WRENCH_HEAD = MaterialisItems.WRENCH_HEAD;
	public static final ItemObject<ModifiableItem> WRENCH = MaterialisItems.WRENCH;
	public static final ItemObject<ModifiableItem> BATTLEWRENCH = MaterialisItems.BATTLEWRENCH;
	public static final ItemObject<Item> MANASHOT = MaterialisItems.MANASHOT;

	public static final FlowingFluidObject<?> FAIRY_FLUID = MaterialisFluids.MOLTEN_FAIRY;
	public static final FlowingFluidObject<?> REFINED_RADIANCE_FLUID = MaterialisFluids.MOLTEN_REFINED_RADIANCE;
	public static final FlowingFluidObject<?> SHADOW_STEEL_FLUID = MaterialisFluids.MOLTEN_SHADOW_STEEL;
}
