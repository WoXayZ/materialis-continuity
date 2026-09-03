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

	public static final RegistryObject<Block> FAIRY_BLOCK = BLOCKS.register("fairy_block", () -> new Block(BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_PINK)
			.strength(6.0F, 6.0F)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()));
}
