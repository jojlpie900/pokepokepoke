package com.gildedgames.aether.common.dungeons.worldgen;

import net.minecraft.world.biome.BiomeGenBase;

public class DungeonsBiome extends BiomeGenBase
{
	public DungeonsBiome(int id)
	{
		super(id);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.setBiomeName("Dungeons");
		this.setDisableRain();
	}

	@Override
	public float getSpawningChance()
	{
		return 0.01F;
	}
}
