package com.gildedgames.aether.common.dungeons.worldgen.rooms;

import java.util.Random;

import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.dungeons.Dungeon;
import com.gildedgames.util.GGHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class DungeonRoomSentryGuardian extends DungeonRoom
{

	public DungeonRoomSentryGuardian(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, StructureBoundingBox structureBoundingBox, int direction)
	{
		super(dungeon, componentType, previousStructure, rand, structureBoundingBox, direction);
	}

	@Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox chunkBox)
	{
		IBlockState dungeonStone = BlocksAether.carved_stone.getDefaultState();

		for (int var12 = this.boundingBox.minY; var12 <= this.boundingBox.maxY; ++var12)
		{
			for (int var13 = this.boundingBox.minX; var13 <= this.boundingBox.maxX; ++var13)
			{
				for (int var14 = this.boundingBox.minZ; var14 <= this.boundingBox.maxZ; ++var14)
				{
					this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LightDungeonStone : dungeonStone, 0, var13, var12, var14, chunkBox);
				}
			}
		}

		int entranceOffset = 8;

		int minX = this.boundingBox.minX + entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2) - 1;
		int minY = this.boundingBox.minY + 1;
		int minZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) - 1;
		int maxX = this.boundingBox.minX + entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2) + 2;
		int maxY = this.boundingBox.minY + 1;
		int maxZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 2;

		this.fillWithBlocksWithNotify(par1World, chunkBox, this.boundingBox.minX + 1 + entranceOffset, minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), false);
		this.fillWithBlocksWithNotify(par1World, chunkBox, this.boundingBox.minX + 1, minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1 + entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), false);

		//fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + entranceOffset, minY, boundingBox.minZ + ((boundingBox.maxZ - boundingBox.minZ) / 2) - 1, this.boundingBox.minX + entranceOffset,  minY + 3, boundingBox.minZ + ((boundingBox.maxZ - boundingBox.minZ) / 2) + 2 , 0, 0, false);

		this.fillWithBlocksWithNotify(par1World, chunkBox, minX, minY + 2, minZ, maxX, maxY + 2, maxZ, dungeonStone, false);

		this.fillWithBlocksWithNotify(par1World, chunkBox, this.boundingBox.minX + 4, minY, this.boundingBox.minZ + 4, this.boundingBox.maxX - 4, minY + 1, this.boundingBox.maxZ - 4, dungeonStone, false);

		for (int j = 1; j <= 2; j++)
		{
			int i = 0;
			int y = this.boundingBox.minY + j;

			for (i = j; i <= (this.boundingBox.maxX - this.boundingBox.minX - 2) - j; i++)
			{
				this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs, 3, this.boundingBox.minX + i + 1, y, this.boundingBox.maxZ - j - 1, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs, 2, this.boundingBox.minX + i + 1, y, this.boundingBox.minZ + j + 1, chunkBox);

				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.minX + i, y + 2, this.boundingBox.minZ + j, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.minX + i, y + 2, this.boundingBox.maxZ - j, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.minX + i, y + 1, this.boundingBox.minZ + j, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.minX + i, y + 1, this.boundingBox.maxZ - j, chunkBox);
			}

			for (i = j; i <= (this.boundingBox.maxZ - this.boundingBox.minZ - 2) - j; i++)
			{
				this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs, 0, this.boundingBox.minX + j + 1, y, this.boundingBox.minZ + i + 1, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs, 1, this.boundingBox.maxX - j - 1, y, this.boundingBox.minZ + i + 1, chunkBox);

				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.maxX - j, y + 2, this.boundingBox.minZ + i, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.minX + j, y + 2, this.boundingBox.minZ + i, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.maxX - j, y + 1, this.boundingBox.minZ + i, chunkBox);
				this.placeBlockAtCurrentPosition(par1World, GGHelper.getAirState(), this.boundingBox.minX + j, y + 1, this.boundingBox.minZ + i, chunkBox);
			}
		}

		if (GGHelper.contains(chunkBox, this.boundingBox.minX + entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2) + 1, maxY + 2, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 1))
		{
			EntitySentryGuardian golem = new EntitySentryGuardian(par1World);

			int x = this.boundingBox.minX + entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2) + 1;
			int y = maxY + 4;
			int z = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 1;

			golem.setPosition(x, y, z);

			par1World.spawnEntityInWorld(golem);
		}

		this.cutHolesForEntrances(par1World, par2Random, chunkBox);

		return true;
	}

}
