package com.gildedgames.aether.common.dungeons.worldgen;

import java.util.List;
import java.util.Random;

import com.gildedgames.aether.common.blocks.BlocksAether;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class ChunkProviderDungeons implements IChunkProvider
{
	private Random rand;

	private World worldObj;

	private NoiseGeneratorOctaves noiseGenerator;

	public ChunkProviderDungeons(World world, long seed)
	{
		this.worldObj = world;
		this.rand = new Random(seed);

		this.noiseGenerator = new NoiseGeneratorOctaves(this.rand, 4);
	}

	public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer chunkData)
	{
		for (int index = 0; index < 65536; index++)
		{
			chunkData.setBlockState(index, BlocksAether.holystone.getDefaultState());
		}

		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				for (int y = 127; y >= 0; --y)
				{
					if (y >= 127 - this.rand.nextInt(5) || y <= 0 + this.rand.nextInt(5))
					{
						chunkData.setBlockState(x, z, y, Blocks.bedrock.getDefaultState());
					}
				}
			}
		}
	}

	@Override
	public boolean canSave()
	{
		return true;
	}

	@Override
	public boolean chunkExists(int i, int j)
	{
		return true;
	}

	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunk, int i, int j)
	{

	}

	@Override
	public int getLoadedChunkCount()
	{
		return 0;
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		return null;
	}

	@Override
	public String makeString()
	{
		return "RandomLevelSource";
	}

	@Override
	public void populate(IChunkProvider ichunkprovider, int i, int j)
	{
		int k = i * 16;
		int l = j * 16;

		//Icestone Generator
		for (int n = 0; n < 5; n++)
		{
			int x = k + this.rand.nextInt(16);
			int y = this.rand.nextInt(128);
			int z = l + this.rand.nextInt(16);
			(new WorldGenMinable(BlocksAether.icestone.getDefaultState(), 10)).generate(this.worldObj, this.rand, new BlockPos(x, y, z));
		}

		//Ambrosium Generator
		for (int n = 0; n < 10; n++)
		{
			int x = k + this.rand.nextInt(16);
			int y = this.rand.nextInt(128);
			int z = l + this.rand.nextInt(16);
			(new WorldGenMinable(BlocksAether.ambrosium_ore.getDefaultState(), 16)).generate(this.worldObj, this.rand, new BlockPos(x, y, z));
		}

		//Zanite Generator
		for (int n = 0; n < 7; n++)
		{
			int x = k + this.rand.nextInt(16);
			int y = this.rand.nextInt(64);
			int z = l + this.rand.nextInt(16);
			(new WorldGenMinable(BlocksAether.zanite_ore.getDefaultState(), 8)).generate(this.worldObj, this.rand, new BlockPos(x, y, z));
		}

		//Gravitite Generator
		for (int n = 0; n < 3; n++)
		{
			int x = k + this.rand.nextInt(16);
			int y = this.rand.nextInt(32);
			int z = l + this.rand.nextInt(16);
			(new WorldGenMinable(BlocksAether.gravitite_ore.getDefaultState(), 4)).generate(this.worldObj, this.rand, new BlockPos(x, y, z));
		}

		//Continuum Generator
		for (int n = 0; n < 2; n++)
		{
			int x = k + this.rand.nextInt(16);
			int y = this.rand.nextInt(128);
			int z = l + this.rand.nextInt(16);
			(new WorldGenMinable(BlocksAether.continuum_ore.getDefaultState(), 4)).generate(this.worldObj, this.rand, new BlockPos(x, y, z));
		}
	}

	@Override
	public Chunk provideChunk(int i, int j)
	{
		this.rand.setSeed(i * 0x4f9939f508L + j * 0x1ef1565bd5L);
		ChunkPrimer primer = new ChunkPrimer();

		this.generateTerrain(i, j, primer);

		Chunk chunk = new Chunk(this.worldObj, primer, i, j);
		chunk.generateSkylightMap();

		return chunk;
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
	{
		return true;
	}

	@Override
	public boolean unloadQueuedChunks()
	{
		return false;
	}

	@Override
	public void saveExtraData()
	{

	}

	@Override
	public Chunk provideChunk(BlockPos blockPosIn)
	{
		return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
	}

	@Override
	public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
	{
		return false;
	}
}
