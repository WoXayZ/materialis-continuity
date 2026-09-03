package com.materialiscontinuity.util;

import java.util.Locale;
import java.util.function.Supplier;

import com.materialiscontinuity.registry.MaterialisFluids;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IByproduct;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer.OreRateType;

public enum MaterialisByproduct implements IByproduct {
	COPPER(true, TinkerFluids.moltenCopper),
	IRON(true, TinkerFluids.moltenIron),
	GOLD(true, TinkerFluids.moltenGold),
	SMALL_GOLD("gold", true, TinkerFluids.moltenGold, FluidValues.NUGGET),
	COBALT(true, TinkerFluids.moltenCobalt),
	TIN(false, TinkerFluids.moltenTin),
	ZINC(false, TinkerFluids.moltenZinc),
	SILVER(false, TinkerFluids.moltenSilver),
	NICKEL(false, TinkerFluids.moltenNickel),
	LEAD(false, TinkerFluids.moltenLead),
	PLATINUM(false, TinkerFluids.moltenPlatinum),
	SMALL_PLATINUM("platinum", false, TinkerFluids.moltenPlatinum, FluidValues.NUGGET),
	CLOGGRUM(true, MaterialisFluids.MOLTEN_CLOGGRUM),
	FROSTSTEEL(true, MaterialisFluids.MOLTEN_FROSTSTEEL),
	UTHERIUM("utherium", true, MaterialisFluids.MOLTEN_UTHERIUM, FluidValues.NUGGET),
	REGALIUM(true, MaterialisFluids.MOLTEN_REGALIUM);

	private final String name;
	private final boolean alwaysPresent;
	private final Supplier<? extends Fluid> fluidSupplier;
	private final int amount;

	MaterialisByproduct(boolean alwaysPresent, Supplier<? extends Fluid> fluidSupplier) {
		this.name = name().toLowerCase(Locale.ROOT);
		this.alwaysPresent = alwaysPresent;
		this.fluidSupplier = fluidSupplier;
		this.amount = FluidValues.NUGGET * 3;
	}

	MaterialisByproduct(String name, boolean alwaysPresent, Supplier<? extends Fluid> fluidSupplier, int amount) {
		this.name = name;
		this.alwaysPresent = alwaysPresent;
		this.fluidSupplier = fluidSupplier;
		this.amount = amount;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isAlwaysPresent() {
		return alwaysPresent;
	}

	@Override
	public FluidOutput getFluid(float scale) {
		return FluidOutput.fromStack(new FluidStack(fluidSupplier.get(), Math.max(1, Math.round(amount * scale))));
	}

	@Override
	public OreRateType getOreRate() {
		return OreRateType.METAL;
	}
}
