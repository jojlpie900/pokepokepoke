package com.gildedgames.aether.common.dungeons;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterDungeon extends Teleporter
{

	public TeleporterDungeon(WorldServer par1WorldServer)
	{
		super(par1WorldServer);
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw)
	{
	}

	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
	{
		return false;
	}

	@Override
	public boolean makePortal(Entity par1Entity)
	{
		return false;
	}

	@Override
	public void removeStalePortalLocations(long par1)
	{

	}

}
