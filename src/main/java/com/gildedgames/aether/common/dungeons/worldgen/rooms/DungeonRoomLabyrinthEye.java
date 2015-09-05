package com.gildedgames.aether.common.dungeons.worldgen.rooms;

import java.util.Random;

import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.dungeons.Dungeon;
import com.gildedgames.util.GGHelper;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class DungeonRoomLabyrinthEye extends DungeonRoom
{

	public DungeonRoomLabyrinthEye(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, StructureBoundingBox structureBoundingBox, int direction)
	{
		super(dungeon, componentType, previousStructure, rand, structureBoundingBox, direction);
	}

	@Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox ChunkBox)
	{
		for (int var12 = this.boundingBox.minY; var12 <= this.boundingBox.maxY; ++var12)
		{
			for (int var13 = this.boundingBox.minX; var13 <= this.boundingBox.maxX; ++var13)
			{
				for (int var14 = this.boundingBox.minZ; var14 <= this.boundingBox.maxZ; ++var14)
				{
					this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LightDungeonStone : BlocksAether.carved_stone.getDefaultState(), 0, var13, var12, var14, ChunkBox);
				}
			}
		}

		int minX = this.boundingBox.minX + ((this.boundingBox.maxX - this.boundingBox.minX) / 2) - 1;
		int minY = this.boundingBox.minY + 1;
		int minZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) - 1;
		int maxX = this.boundingBox.minX + ((this.boundingBox.maxX - this.boundingBox.minX) / 2) + 2;
		int maxY = this.boundingBox.minY + 1;
		int maxZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 2;

		this.fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), false);
		this.fillWithBlocksWithNotify(par1World, ChunkBox, minX, minY, minZ, maxX, maxY, maxZ, BlocksAether.carved_stone.getDefaultState(), false);

		if (GGHelper.contains(ChunkBox, this.boundingBox.minX + ((this.boundingBox.maxX - (this.boundingBox.minX)) / 2) + 1, maxY + 2, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 1))
		{
			EntityLabyrinthEye eye = new EntityLabyrinthEye(par1World);

			int x = this.boundingBox.minX + ((this.boundingBox.maxX - this.boundingBox.minX) / 2);
			int y = this.boundingBox.minY + 2;
			int z = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2);

			eye.setPosition(x + 1F, y + 2, z + 1F);

			par1World.spawnEntityInWorld(eye);
		}

		this.cutHolesForEntrances(par1World, par2Random, ChunkBox);

		return true;
	}

}
