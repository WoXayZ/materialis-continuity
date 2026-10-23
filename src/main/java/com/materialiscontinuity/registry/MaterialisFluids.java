package com.materialiscontinuity.registry;

import java.util.List;

import com.materialiscontinuity.Materialis;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.common.registration.FluidDeferredRegisterExtension;

import static slimeknights.tconstruct.fluids.block.BurningLiquidBlock.createBurning;

public class MaterialisFluids {

	public static final FluidDeferredRegisterExtension FLUIDS = new FluidDeferredRegisterExtension(Materialis.MOD_ID);

	public static final FlowingFluidObject<?> MOLTEN_FAIRY = metal("molten_fairy", 1050, 15, MapColor.COLOR_PINK);
	public static final FlowingFluidObject<?> MOLTEN_ALUMITE = metal("molten_alumite", 1050, 15, MapColor.COLOR_PINK);
	public static final FlowingFluidObject<?> MOLTEN_DRULLOY = metal("molten_drulloy", 800, 15, MapColor.TERRACOTTA_BROWN);
	public static final FlowingFluidObject<?> MOLTEN_POKEFENNIUM = metal("molten_pokefennium", 1100, 15, MapColor.COLOR_CYAN);
	public static final FlowingFluidObject<?> MOLTEN_RED_AURUM = metal("molten_red_aurum", 950, 15, MapColor.COLOR_RED);

	public static final FlowingFluidObject<?> MOLTEN_REFINED_RADIANCE = metal("molten_refined_radiance", 1600, 15, MapColor.SNOW);
	public static final FlowingFluidObject<?> MOLTEN_SHADOW_STEEL = metal("molten_shadow_steel", 1600, 0, MapColor.COLOR_BLACK);

	public static final FlowingFluidObject<?> MOLTEN_ARCANE_GOLD = metal("molten_arcane_gold", 970, 13, MapColor.GOLD);

	public static final FlowingFluidObject<?> MOLTEN_NEPTUNIUM = metal("molten_neptunium", 1700, 13, MapColor.COLOR_CYAN);
	public static final FlowingFluidObject<?> MOLTEN_STARMETAL = metal("molten_starmetal", 1050, 11, MapColor.COLOR_BLUE);

	public static final FlowingFluidObject<?> MOLTEN_PINK_SLIME = metal("molten_pink_slime", 1260, 12, MapColor.COLOR_PINK);

	public static final FlowingFluidObject<?> MOLTEN_CLOGGRUM = metal("molten_cloggrum", 950, 12, MapColor.WOOD);
	public static final FlowingFluidObject<?> MOLTEN_FROSTSTEEL = metal("molten_froststeel", 1160, 12, MapColor.COLOR_LIGHT_BLUE);
	public static final FlowingFluidObject<?> MOLTEN_UTHERIUM = metal("molten_utherium", 1370, 12, MapColor.COLOR_RED);
	public static final FlowingFluidObject<?> MOLTEN_FORGOTTEN_METAL = metal("molten_forgotten_metal", 1530, 12, MapColor.COLOR_GREEN);
	public static final FlowingFluidObject<?> MOLTEN_REGALIUM = metal("molten_regalium", 920, 12, MapColor.GOLD);

	public static final FlowingFluidObject<?> MOLTEN_PSIMETAL = metal("molten_psimetal", 1050, 12, MapColor.COLOR_CYAN);
	public static final FlowingFluidObject<?> MOLTEN_EBONY_PSIMETAL = metal("molten_ebony_psimetal", 1430, 12, MapColor.COLOR_BLACK);
	public static final FlowingFluidObject<?> MOLTEN_IVORY_PSIMETAL = metal("molten_ivory_psimetal", 1430, 12, MapColor.SNOW);

	public static final FlowingFluidObject<?> MOLTEN_IESNIUM = metal("molten_iesnium", 1390, 12, MapColor.COLOR_LIGHT_BLUE);

	public static final FlowingFluidObject<?> MOLTEN_MANASTEEL = metal("molten_manasteel", 1090, 12, MapColor.COLOR_BLUE);
	public static final FlowingFluidObject<?> MOLTEN_ELEMENTIUM = metal("molten_elementium", 1460, 12, MapColor.COLOR_PINK);
	public static final FlowingFluidObject<?> MOLTEN_TERRASTEEL = metal("molten_terrasteel", 1580, 12, MapColor.COLOR_GREEN);
	public static final FlowingFluidObject<?> MOLTEN_ALFSTEEL = metal("molten_alfsteel", 1740, 12, MapColor.GOLD);

	public static final FlowingFluidObject<?> MOLTEN_DRACONIUM = metal("molten_draconium", 1350, 12, MapColor.COLOR_PURPLE);
	public static final FlowingFluidObject<?> MOLTEN_DRACONIUM_AWAKENED = metal("molten_draconium_awakened", 1770, 12, MapColor.COLOR_ORANGE);

	public static final FlowingFluidObject<?> MOLTEN_FLUX_INFUSED = metal("molten_flux_infused", 1180, 12, MapColor.COLOR_RED);

	public static final FlowingFluidObject<?> MOLTEN_DESH = metal("molten_desh", 800, 10, MapColor.TERRACOTTA_ORANGE);
	public static final FlowingFluidObject<?> MOLTEN_OSTRUM = metal("molten_ostrum", 900, 8, MapColor.COLOR_PINK);
	public static final FlowingFluidObject<?> MOLTEN_CALORITE = metal("molten_calorite", 1050, 12, MapColor.COLOR_RED);
	public static final FlowingFluidObject<?> MOLTEN_JUPERIUM = metal("molten_juperium", 1100, 12, MapColor.ICE);
	public static final FlowingFluidObject<?> MOLTEN_SATURLYTE = metal("molten_saturlyte", 1150, 12, MapColor.COLOR_PURPLE);
	public static final FlowingFluidObject<?> MOLTEN_HALLOWED_GOLD = metal("molten_hallowed_gold", 700, 12, MapColor.GOLD);
	public static final FlowingFluidObject<?> MOLTEN_SOUL_STAINED_STEEL = metal("molten_soul_stained_steel", 950, 8, MapColor.COLOR_PURPLE);

	public static final List<FlowingFluidObject<?>> COMPAT_FLUIDS = List.of(
			MOLTEN_FAIRY, MOLTEN_REFINED_RADIANCE, MOLTEN_SHADOW_STEEL, MOLTEN_ARCANE_GOLD,
			MOLTEN_NEPTUNIUM, MOLTEN_STARMETAL, MOLTEN_PINK_SLIME,
			MOLTEN_CLOGGRUM, MOLTEN_FROSTSTEEL, MOLTEN_UTHERIUM, MOLTEN_FORGOTTEN_METAL, MOLTEN_REGALIUM,
			MOLTEN_PSIMETAL, MOLTEN_EBONY_PSIMETAL, MOLTEN_IVORY_PSIMETAL, MOLTEN_IESNIUM,
			MOLTEN_MANASTEEL, MOLTEN_ELEMENTIUM, MOLTEN_TERRASTEEL, MOLTEN_ALFSTEEL,
			MOLTEN_DRACONIUM, MOLTEN_DRACONIUM_AWAKENED, MOLTEN_FLUX_INFUSED,
			MOLTEN_DESH, MOLTEN_OSTRUM, MOLTEN_CALORITE, MOLTEN_JUPERIUM, MOLTEN_SATURLYTE, MOLTEN_HALLOWED_GOLD,
			MOLTEN_SOUL_STAINED_STEEL);

	private static FlowingFluidObject<?> metal(String name, int temperature, int light, MapColor color) {
		return FLUIDS.registerMetal(name)
				.type(hot(name).temperature(temperature).lightLevel(light))
				.block(createBurning(color, light, 10, 6f))
				.bucket()
				.flowing();
	}

	private static FluidType.Properties hot(String name) {
		return FluidType.Properties.create()
				.density(2000)
				.viscosity(10000)
				.temperature(1000)
				.descriptionId(Materialis.makeDescriptionId("fluid", name))
				.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
				.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
				.motionScale(0.0023333333333333335D)
				.canSwim(false)
				.canDrown(false)
				.pathType(BlockPathTypes.LAVA)
				.adjacentPathType(null);
	}
}
