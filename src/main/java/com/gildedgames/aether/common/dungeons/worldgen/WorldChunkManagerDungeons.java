package com.gildedgames.aether.common.dungeons.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.gildedgames.aether.common.AetherConfig;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerDungeons extends WorldChunkManager
{
	private BiomeGenBase biomeGenerator;

	public WorldChunkManagerDungeons()
	{
		this.biomeGenerator = new DungeonsBiome(AetherConfig.DungeonBiomeID);
	}

	@Override
	public boolean areBiomesViable(int i, int j, int k, List list)
	{
		return list.contains(this.biomeGenerator);
	}

	@Override
	public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random)
	{
		if (biomes.contains(this.biomeGenerator))
		{
			return new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1));
		}

		return null;
	}

	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase abiomegenbase[], int i, int j, int k, int l, boolean flag)
	{
		return this.loadBlockGeneratorData(abiomegenbase, i, j, k, l);
	}

	@Override
	public float[] getRainfall(float list[], int i, int j, int k, int l)
	{
		if ((list == null) || (list.length < (k * l)))
		{
			list = new float[k * l];
		}

		Arrays.fill(list, 0, k * l, 0);
		return list;
	}

	@Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase abiomegenbase[], int i, int j, int k, int l)
	{
		if ((abiomegenbase == null) || (abiomegenbase.length < (k * l)))
		{
			abiomegenbase = new BiomeGenBase[k * l];
		}

		Arrays.fill(abiomegenbase, 0, k * l, this.biomeGenerator);
		return abiomegenbase;
	}
}
