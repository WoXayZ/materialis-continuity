package com.materialiscontinuity.modifiers;

import com.materialiscontinuity.Materialis;

import net.minecraft.data.PackOutput;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class MaterialisModifiers extends AbstractModifierProvider {

	public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Materialis.MOD_ID);
	private static final String MODIFIERS_PACKAGE = "com.materialiscontinuity.modifiers.";

	/**
	 * Instantiates optional-mod modifiers by name so this class never has a constant-pool
	 * reference to them. A method reference like {@code SpellshotModifier::new} loads that
	 * class (and its Ars/Psi/Botania types) even when the mod is absent.
	 */
	private static StaticModifier<Modifier> optionalMod(String name, String modId, String className) {
		return MODIFIERS.register(name, () -> {
			if (!ModList.get().isLoaded(modId)) {
				return new Modifier();
			}
			try {
				return (Modifier) Class.forName(MODIFIERS_PACKAGE + className).getDeclaredConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				Materialis.LOGGER.error("Failed to construct optional modifier {}", name, e);
				return new Modifier();
			}
		});
	}

	private static StaticModifier<Modifier> psi(String name, String className) {
		return optionalMod(name, "psi", className);
	}

	private static StaticModifier<Modifier> botania(String name, String className) {
		return optionalMod(name, "botania", className);
	}

	private static StaticModifier<Modifier> create(String name, String className) {
		return optionalMod(name, "create", className);
	}

	private static StaticModifier<Modifier> ie(String name, String className) {
		return optionalMod(name, "immersiveengineering", className);
	}

	private static StaticModifier<Modifier> ars(String name, String className) {
		return optionalMod(name, "ars_nouveau", className);
	}

	public static final StaticModifier<Modifier> voidingModifier = MODIFIERS.register("voiding", VoidingModifier::new);
	public static final StaticModifier<Modifier> residualLightModifier = MODIFIERS.register("residual_light", ResidualLightModifier::new);
	public static final StaticModifier<Modifier> inertiaModifier = MODIFIERS.register("inertia", InertiaModifier::new);
	public static final StaticModifier<Modifier> featherweightModifier = MODIFIERS.register("featherweight", FeatherweightModifier::new);
	public static final StaticModifier<Modifier> arcaneModifier = MODIFIERS.register("arcane", ArcaneModifier::new);
	public static final StaticModifier<Modifier> neptunesBlessingModifier = MODIFIERS.register("neptunes_blessing", NeptunesBlessingModifier::new);
	public static final StaticModifier<Modifier> halfLifeModifier = MODIFIERS.register("half_life", HalfLifeModifier::new);
	public static final StaticModifier<Modifier> workHardenedModifier = MODIFIERS.register("work_hardened", WorkHardenedModifier::new);
	public static final StaticModifier<Modifier> polishedModifier = MODIFIERS.register("polished", PolishedModifier::new);
	public static final StaticModifier<Modifier> daredevilModifier = MODIFIERS.register("daredevil", DaredevilModifier::new);
	public static final StaticModifier<Modifier> decayModifier = MODIFIERS.register("decay", DecayModifier::new);
	public static final StaticModifier<Modifier> nocturnalModifier = MODIFIERS.register("nocturnal", NocturnalModifier::new);
	public static final StaticModifier<Modifier> feebleModifier = MODIFIERS.register("feeble", FeebleModifier::new);
	public static final StaticModifier<Modifier> overweightModifier = MODIFIERS.register("overweight", OverweightModifier::new);
	public static final StaticModifier<Modifier> economicalModifier = MODIFIERS.register("economical", EconomicalModifier::new);
	public static final StaticModifier<Modifier> oldTimerModifier = MODIFIERS.register("old_timer", OldTimerModifier::new);
	public static final StaticModifier<Modifier> freezingModifier = MODIFIERS.register("freezing", FreezingModifier::new);
	public static final StaticModifier<Modifier> cleansingModifier = MODIFIERS.register("cleansing", CleansingModifier::new);
	public static final StaticModifier<Modifier> underlordModifier = MODIFIERS.register("underlord", UnderlordModifier::new);
	public static final StaticModifier<Modifier> shortSightedModifier = MODIFIERS.register("short_sighted", ShortSightedModifier::new);
	public static final StaticModifier<Modifier> auxiliaryPowerModifier = MODIFIERS.register("auxiliary_power", AuxiliaryPowerModifier::new);
	public static final StaticModifier<Modifier> adrenalineModifier = MODIFIERS.register("adrenaline", AdrenalineModifier::new);
	public static final StaticModifier<Modifier> psionizingRadiationModifier = psi("psionizing_radiation", "PsionizingRadiationModifier");
	public static final StaticModifier<Modifier> lesserPsionizingRadiationModifier = psi("lesser_psionizing_radiation", "PsionizingRadiationModifierLesser");
	public static final StaticModifier<Modifier> psionizingRadiationModifierBreakBlock = psi("psionizing_radiation_break_block", "PsionizingRadiationModifierBreakBlock");
	public static final StaticModifier<Modifier> psionizingRadiationModifierAttack = psi("psionizing_radiation_attack", "PsionizingRadiationModifierAttack");
	public static final StaticModifier<Modifier> psionizingRadiationModifierDamage = psi("psionizing_radiation_damage", "PsionizingRadiationModifierDamage");
	public static final StaticModifier<Modifier> psionizingRadiationModifierTick = psi("psionizing_radiation_tick", "PsionizingRadiationModifierTick");
	public static final StaticModifier<Modifier> psionizingRadiationModifierJump = psi("psionizing_radiation_jump", "PsionizingRadiationModifierJump");
	public static final StaticModifier<Modifier> psionizingRadiationModifierFire = psi("psionizing_radiation_fire", "PsionizingRadiationModifierFire");
	public static final StaticModifier<Modifier> psionizingRadiationModifierHealth = psi("psionizing_radiation_health", "PsionizingRadiationModifierHealth");
	public static final StaticModifier<Modifier> psionizingRadiationModifierLight = psi("psionizing_radiation_light", "PsionizingRadiationModifierLight");
	public static final StaticModifier<Modifier> psionizingRadiationModifierWater = psi("psionizing_radiation_water", "PsionizingRadiationModifierWater");
	public static final StaticModifier<Modifier> psionizingRadiationModifierDetonate = psi("psionizing_radiation_detonate", "PsionizingRadiationModifierDetonate");
	public static final StaticModifier<Modifier> psichoKillerModifier = psi("psicho_killer", "PsichoKillerModifier");
	public static final StaticModifier<Modifier> psichoDiggerModifier = psi("psicho_digger", "PsichoDiggerModifier");
	public static final StaticModifier<Modifier> brittleModifier = MODIFIERS.register("brittle", BrittleModifier::new);
	public static final StaticModifier<Modifier> refuelingModifier = MODIFIERS.register("refueling", RefuelingModifier::new);
	public static final StaticModifier<Modifier> quenchingModifier = MODIFIERS.register("quenching", QuenchingModifier::new);
	public static final StaticModifier<Modifier> overeatingModifier = MODIFIERS.register("overeating", OvereatingModifier::new);
	public static final StaticModifier<Modifier> psishieldModifier = psi("psishield", "PsishieldModifier");
	public static final StaticModifier<Modifier> manaripperModifier = botania("manaripper", "ManaripperModifier");
	public static final StaticModifier<Modifier> manaburnerModifier = botania("manaburner", "ManaburnerModifier");
	public static final StaticModifier<Modifier> manashieldModifier = botania("manashield", "ManashieldModifier");
	public static final StaticModifier<Modifier> manadrawModifier = botania("manadraw", "ManadrawModifier");
	public static final StaticModifier<Modifier> manaboltModifier = botania("manabolt", "ManaboltModifier");
	public static final StaticModifier<Modifier> manashotModifier = botania("manashot", "ManashotModifier");
	public static final StaticModifier<Modifier> pixiecallerModifier = botania("pixiecaller", "PixiecallerModifier");
	public static final StaticModifier<Modifier> terrabeamModifier = botania("terrabeam", "TerrabeamModifier");
	public static final StaticModifier<Modifier> elvenBeamModifier = botania("elven_beam", "ElvenBeamModifier");
	public static final StaticModifier<Modifier> fluxripperModifier = MODIFIERS.register("fluxripper", FluxripperModifier::new);
	public static final StaticModifier<Modifier> fluxburnerModifier = MODIFIERS.register("fluxburner", FluxburnerModifier::new);
	public static final StaticModifier<Modifier> fluxshieldModifier = MODIFIERS.register("fluxshield", FluxshieldModifier::new);
	public static final StaticModifier<Modifier> fluxdrawModifier = MODIFIERS.register("fluxdraw", FluxdrawModifier::new);
	public static final StaticModifier<Modifier> fluxboltModifier = MODIFIERS.register("fluxbolt", FluxboltModifier::new);
	public static final StaticModifier<Modifier> powerHungryModifier = MODIFIERS.register("power_hungry", PowerHungryModifier::new);
	public static final StaticModifier<Modifier> crystallineModifier = MODIFIERS.register("crystalline", CrystallineModifier::new);
	public static final StaticModifier<Modifier> supermassiveModifier = MODIFIERS.register("supermassive", SupermassiveModifier::new);
	public static final StaticModifier<Modifier> cosmicUnbreakableModifier = MODIFIERS.register("cosmic_unbreakable", CosmicUnbreakableModifier::new);
	public static final StaticModifier<Modifier> instamineModifier = MODIFIERS.register("instamine", InstamineModifier::new);
	public static final StaticModifier<Modifier> instakillModifier = MODIFIERS.register("instakill", InstakillModifier::new);
	public static final StaticModifier<Modifier> bedrockBreakerModifier = MODIFIERS.register("bedrock_breaker", BedrockBreakerModifier::new);
	public static final StaticModifier<Modifier> cosmicLuckModifier = MODIFIERS.register("cosmic_luck", CosmicLuckModifier::new);
	public static final StaticModifier<Modifier> heavenshotModifier = MODIFIERS.register("heavenshot", HeavenshotModifier::new);
	public static final StaticModifier<Modifier> reapingModifier = MODIFIERS.register("reaping", ReapingModifier::new);
	public static final StaticModifier<Modifier> wrenchingModifier = MODIFIERS.register("wrenching", WrenchingModifier::new);
	public static final StaticModifier<Modifier> thermalWrenchingModifier = MODIFIERS.register("thermal_wrenching", ThermalWrenchingModifier::new);
	public static final StaticModifier<Modifier> createWrenchingModifier = create("create_wrenching", "CreateWrenchingModifier");
	public static final StaticModifier<Modifier> immersiveWrenchingModifier = ie("immersive_wrenching", "ImmersiveWrenchingModifier");
	public static final StaticModifier<Modifier> pipeWrenchingModifier = MODIFIERS.register("pipe_wrenching", PipeWrenchingModifier::new);
	public static final StaticModifier<Modifier> spellSocketModifier = MODIFIERS.register("spell_socket", SpellSocketModifier::new);
	public static final StaticModifier<Modifier> spellCastingModifier = psi("spell_casting", "SpellCastingModifier");
	public static final StaticModifier<Modifier> lesserSpellCastingModifier = psi("lesser_spell_casting", "SpellCastingModifierLesser");
	public static final StaticModifier<Modifier> spellshotModifier = ars("spellshot", "SpellshotModifier");
	public static final StaticModifier<Modifier> colorizedModifier = psi("colorized", "ColorizedModifier");
	public static final StaticModifier<Modifier> psionizingRadiationModifierSensor = psi("psionizing_radiation_sensor", "PsionizingRadiationModifierSensor");
	public static final StaticModifier<Modifier> capacitorModifier = MODIFIERS.register("capacitor", CapacitorModifier::new);
	public static final StaticModifier<Modifier> engineersGogglesModifier = create("engineers_goggles", "EngineersGogglesModifier");
	public static final StaticModifier<Modifier> otherworldGogglesModifier = MODIFIERS.register("otherworld_goggles", OtherworldGogglesModifier::new);
	public static final StaticModifier<Modifier> reactiveModifier = ars("reactive", "ReactiveModifier");
	public static final StaticModifier<Modifier> cataclysmicModifier = MODIFIERS.register("cataclysmic", CataclysmicModifier::new);
	public static final StaticModifier<Modifier> skullfireModifire = MODIFIERS.register("skullfire", SkullfireModifire::new);

	public static final ModifierId enhancedQuartzModifier = new ModifierId(Materialis.MOD_ID, "enhanced_quartz");
	public static final ModifierId galvanizedModifier = new ModifierId(Materialis.MOD_ID, "galvanized");
	public static final StaticModifier<Modifier> otherworldly1Modifier = MODIFIERS.register("otherworldly_1", () -> new OtherworldlyModifier(1));
	public static final StaticModifier<Modifier> otherworldly2Modifier = MODIFIERS.register("otherworldly_2", () -> new OtherworldlyModifier(2));

	public MaterialisModifiers(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void addModifiers() {
	}

	@Override
	public String getName() {
		return "Materialis Continuity Modifiers";
	}
}
