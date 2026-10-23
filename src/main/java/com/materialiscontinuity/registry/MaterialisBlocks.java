package com.materialiscontinuity.registry;

import com.materialiscontinuity.Materialis;
import com.materialiscontinuity.block.LightResidueBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MaterialisBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Materialis.MOD_ID);

	public static final RegistryObject<Block> LIGHT_RESIDUE = BLOCKS.register("light_residue", () -> new LightResidueBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.NONE)
			.strength(0.0F, 0.0F)
			.lightLevel(state -> 15)
			.randomTicks()
			.air()
			.noCollission()
			.noLootTable()
			.replaceable()
			.pushReaction(PushReaction.DESTROY)));

	public static final RegistryObject<Block> FAIRY_BLOCK = storage("fairy_block", MapColor.COLOR_PINK);
	public static final RegistryObject<Block> ALUMITE_BLOCK = storage("alumite_block", MapColor.COLOR_PINK);
	public static final RegistryObject<Block> DRULLOY_BLOCK = storage("drulloy_block", MapColor.TERRACOTTA_BROWN);
	public static final RegistryObject<Block> POKEFENNIUM_BLOCK = storage("pokefennium_block", MapColor.COLOR_CYAN);
	public static final RegistryObject<Block> RED_AURUM_BLOCK = storage("red_aurum_block", MapColor.COLOR_RED);

	private static RegistryObject<Block> storage(String name, MapColor color) {
		return BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.of()
				.mapColor(color)
				.strength(6.0F, 6.0F)
				.sound(SoundType.METAL)
				.requiresCorrectToolForDrops()));
	}
}
