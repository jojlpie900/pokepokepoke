package com.gildedgames.aether.common.dungeons.worldgen;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderDungeons extends WorldProviderSurface
{
	public WorldProviderDungeons()
	{
		super();
		this.hasNoSky = true;
	}

	@Override
	public float[] calcSunriseSunsetColors(float f, float f1)
	{
		return null;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	@Override
	public boolean canCoordinateBeSpawn(int i, int j)
	{
		return true;
	}

	@Override
	public boolean canRespawnHere()
	{
		return true;
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkProviderDungeons(this.worldObj, this.worldObj.getSeed());
	}

	@Override
	public String getDepartMessage()
	{
		return "Escaping from the Dungeons";
	}

	@Override
	public String getDimensionName()
	{
		return "Dungeons";
	}

	@Override
	public Vec3 getFogColor(float f, float f1)
	{
		return new Vec3(0.0D, 0.0D, 0.0D);
	}

	@Override
	public String getSaveFolder()
	{
		return "DUNGEONS";
	}

	@Override
	public double getVoidFogYFactor()
	{
		return 100;
	}

	@Override
	public String getWelcomeMessage()
	{
		return "Descending to the Dungeons";
	}

	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}

	@Override
	public boolean isSkyColored()
	{
		return false;
	}

	@Override
	public Vec3 getSkyColor(Entity par1Entity, float par2)
	{
		return new Vec3(0.0D, 0.0D, 0.0D);
	}

	@Override
	protected void registerWorldChunkManager()
	{
		this.worldChunkMgr = new WorldChunkManagerDungeons();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer()
	{
		return null;
	}

	@Override
	public double getHorizon()
	{
		return 0.0;
	}
}
