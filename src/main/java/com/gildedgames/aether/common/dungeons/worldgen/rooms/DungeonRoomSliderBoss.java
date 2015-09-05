package com.gildedgames.aether.common.dungeons.worldgen.rooms;

import java.util.Random;

import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.dungeons.Dungeon;
import com.gildedgames.util.GGHelper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class DungeonRoomSliderBoss extends DungeonRoom
{

	int yOffset = 3;

	public DungeonRoomSliderBoss(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, int maxX, int maxZ)
	{
		super(dungeon, componentType, previousStructure, rand, maxX, maxZ);

		this.boundingBox = new StructureBoundingBox(maxX, 30 - this.yOffset, maxZ, maxX + 24, 40, maxZ + 15);

		this.entrances.clear();
		this.addEntranceToAllFourWalls();
	}

	@Override
	public void addEntranceToAllFourWalls()
	{
		this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.minZ - 2, this.boundingBox.minX + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.minZ));
		this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.maxZ, this.boundingBox.minX + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ + 2));

		this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
	}

	int entranceOffset = 8;

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox chunkBox)
	{
		for (int var12 = this.boundingBox.minY; var12 <= this.boundingBox.maxY; ++var12)
		{
			for (int var13 = this.boundingBox.minX; var13 <= this.boundingBox.maxX; ++var13)
			{
				for (int var14 = this.boundingBox.minZ; var14 <= this.boundingBox.maxZ; ++var14)
				{
					this.placeBlockAtCurrentPosition(world, rand.nextInt(20) == 1 ? AetherBlocks.DivineLightDungeonStone : BlocksAether.carved_stone.getDivineState(), 0, var13, var12, var14, chunkBox);
				}
			}
		}

		int minX = this.boundingBox.minX + this.entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2) - 1;
		int minY = this.boundingBox.minY + this.yOffset + 1;
		int minZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) - 1;
		int maxX = this.boundingBox.minX + this.entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2) + 2;
		int maxY = this.boundingBox.minY + this.yOffset + 1;
		int maxZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 2;

		this.fillWithBlocksWithNotify(world, chunkBox, this.boundingBox.minX + 1 + this.entranceOffset, minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), false);
		this.fillWithBlocksWithNotify(world, chunkBox, this.boundingBox.minX + 1, minY, this.boundingBox.minZ + 1, this.boundingBox.minX - 1 + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), false);

		this.fillWithBlocksWithNotify(world, chunkBox, minX, minY - 3, minZ, maxX, maxY, maxZ, BlocksAether.carved_stone.getDivineState(), false);

		if (GGHelper.contains(chunkBox, this.boundingBox.minX + this.entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2) + 1, maxY + 2, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 1))
		{
			EntitySlider slider = new EntitySlider(world, this.boundingBox.minX + this.entranceOffset + ((this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2) + 1, maxY + 2, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 1);

			world.spawnEntityInWorld(slider);

		}

		this.fillWithBlocksWithNotify(world, chunkBox, minX + 1, minY - 2, minZ + 1, maxX - 1, maxY - 1, maxZ - 1, GGHelper.getAirState(), false);

		/*if (chunkBox.isVecInside(minX + 1, minY - 2, minZ + 1))
		{
			world.setBlock(minX + 1, minY - 2, minZ + 1, AetherBlocks.TreasureChest);
			TileEntityChest chest = (TileEntityChest) (world.getTileEntity(minX + 1, minY - 2, minZ + 1));
		}*/

		this.fillWithBlocksWithNotify(world, chunkBox, this.boundingBox.minX + this.entranceOffset, minY, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) - 2, this.boundingBox.minX + this.entranceOffset, minY + 3, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 2, GGHelper.getAirState(), false);

		this.placeBlockAtCurrentPosition(world, AetherBlocks.SliderLabyrinthDoor, 0, this.boundingBox.minX + this.entranceOffset, minY, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2), chunkBox);

		TileEntity tile = world.getTileEntity(new BlockPos(this.boundingBox.minX + this.entranceOffset, minY, this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2)));

		int tileX = this.boundingBox.minX + this.entranceOffset;
		int tileY = minY;
		int tileZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2);

		if (tile instanceof TileEntityMultiBlock)
		{
			TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) tile;

			if (tileEntity.getCurrentRotation(world, tileX, tileY, tileZ) != Rotation.EAST)
			{
				tileEntity.setRotationAndRefresh(Rotation.EAST);
			}
		}

		//THIS CUTS THE HOLES BETWEEN ROOMS
		this.cutHolesForEntrances(world, rand, chunkBox);

		return true;
	}

}
