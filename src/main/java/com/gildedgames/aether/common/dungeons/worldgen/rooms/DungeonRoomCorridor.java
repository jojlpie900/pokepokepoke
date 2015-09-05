package com.gildedgames.aether.common.dungeons.worldgen.rooms;

import java.util.Random;

import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.dungeons.Dungeon;
import com.gildedgames.util.GGHelper;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class DungeonRoomCorridor extends DungeonRoom
{

	private int sectionCount;

	public DungeonRoomCorridor(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, StructureBoundingBox structureBoundingBox)
	{
		super(dungeon, componentType, previousStructure, rand);

		this.boundingBox = structureBoundingBox;

		if (this.coordBaseMode != EnumFacing.WEST && this.coordBaseMode != EnumFacing.EAST) //TODO: Test
		{
			this.sectionCount = this.boundingBox.getXSize() / 5;
		}
		else
		{
			this.sectionCount = this.boundingBox.getZSize() / 5;
		}
		this.addEntranceToAllFourWalls();
	}

	@Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
	{
		this.fillVariedBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, BlocksAether.holystone.getBloodMoss(), BlocksAether.holystone.getDefaultState(), 2, par2Random, true);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), GGHelper.getAirState(), false);

		this.cutHolesForEntrances(par1World, par2Random, par3StructureBoundingBox);

		return true;

	}

}
