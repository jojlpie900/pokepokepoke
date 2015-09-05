package com.gildedgames.aether.common.dungeons.worldgen.rooms;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.dungeons.Dungeon;
import com.gildedgames.util.GGHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class DungeonRoom extends StructureComponent
{
	public List<StructureComponent> linkedRooms = new LinkedList<StructureComponent>();

	protected Dungeon dungeonInstance;

	public List<StructureBoundingBox> entrances = new LinkedList<StructureBoundingBox>();

	int YOffset = 0;

	public DungeonRoom(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, int boxWidth, int boxLength)
	{
		super(componentType);

		this.dungeonInstance = dungeon;

		if (previousStructure != null)
		{
			this.linkedRooms.add(previousStructure);
		}

		this.boundingBox = new StructureBoundingBox(boxWidth, 50, boxLength, boxWidth + 7 + rand.nextInt(6), 60, boxLength + 7 + rand.nextInt(6));
		this.addEntranceToAllFourWalls();
	}

	public DungeonRoom(Dungeon dungeon, int par1, StructureComponent previousStructor, Random par2Random)
	{
		super(par1);

		this.dungeonInstance = dungeon;
		this.linkedRooms.add(previousStructor);
	}

	public DungeonRoom(Dungeon dungeon, int par1, StructureComponent previousStructor, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
	{
		super(par1);
		this.dungeonInstance = dungeon;
		this.linkedRooms.add(previousStructor);
		this.boundingBox = structureBoundingBox;
		this.addEntranceToAllFourWalls();
	}

	public void addEntranceToAllFourWalls()
	{
		this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.minZ));
		this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ + 2));
		this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
		this.entrances.add(new StructureBoundingBox(this.boundingBox.maxX, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
	}

	public void addStructure(List<StructureComponent> components, Random random, int i, int j, int k, int direction, int componentType)
	{
		StructureComponent var7 = this.dungeonInstance.getNextDungeonRoom(this, components, random, i, j, k, direction, componentType);

		if (var7 != null)
		{
			this.linkedRooms.add(var7);
		}
	}

	protected void placeBlockAtCurrentPosition(World world, IBlockState state, int x, int y, int z, StructureBoundingBox boundingBox)
	{
		this.placeBlockAtCurrentPosition(world, state, new BlockPos(x, y, z), boundingBox);
	}

	protected void placeBlockAtCurrentPosition(World world, IBlockState state, BlockPos pos, StructureBoundingBox boundingBox)
	{
		if (GGHelper.contains(boundingBox, pos))
		{
			world.setBlockState(pos, state, 2);
		}
	}

	@Override
	public void buildComponent(StructureComponent previousStructor, List par2List, Random par3Random)
	{
		this.buildComponent(par2List, par3Random);
	}

	//THIS ADDS THE COMPONENTS THAT ARE CONNECTED TO THIS COMPONENT
	public void buildComponent(List<StructureComponent> par2List, Random par3Random)
	{
		int componentType = this.getComponentType();
		int var6 = 2;

		if (this.boundingBox.getYSize() - var6 <= 0)
		{
			var6 = 1;
		}

		int var5;

		for (var5 = 0; var5 < this.boundingBox.getXSize(); var5 += 1)
		{
			//var5 += par3Random.nextInt(this.boundingBox.getXSize() - 2) ;

			if (var5 + 3 > this.boundingBox.getXSize())
			{
				break;
			}

			this.addStructure(par2List, par3Random, this.boundingBox.minX + var5, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.minZ - 1, 2, componentType);

		}

		for (var5 = 0; var5 < this.boundingBox.getXSize(); var5 += 1)
		{
			//var5 += par3Random.nextInt(this.boundingBox.getXSize() - 2) ;

			if (var5 + 3 > this.boundingBox.getXSize())
			{
				break;
			}

			this.addStructure(par2List, par3Random, this.boundingBox.minX + var5, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.maxZ + 1, 0, componentType);

		}

		for (var5 = 0; var5 < this.boundingBox.getZSize(); var5 += 1)
		{
			//var5 += par3Random.nextInt(this.boundingBox.getZSize() - 2) ;

			if (var5 + 3 > this.boundingBox.getZSize())
			{
				break;
			}

			this.addStructure(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.minZ + var5, 1, componentType);

		}

		for (var5 = 0; var5 < this.boundingBox.getZSize(); var5 += 1)
		{
			//var5 += par3Random.nextInt(this.boundingBox.getZSize() - 2) ;

			if (var5 + 3 < this.boundingBox.getZSize())
			{
				break;
			}

			this.addStructure(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.minZ + var5, 3, componentType);

		}
	}

	public void cutHolesForEntrances(World objWorld, Random random, StructureBoundingBox par3StructureBoundingBox)
	{
		for (Object entrance : this.entrances)
		{
			StructureBoundingBox myCube = (StructureBoundingBox) entrance;

			for (Object linkedRoom : this.linkedRooms)
			{
				for (Object entrance1 : ((DungeonRoom) linkedRoom).entrances)
				{
					StructureBoundingBox cube = this.findIntersectingCube(myCube, (StructureBoundingBox) entrance1);
					if (cube != null)
					{
						this.fillWithBlocks(objWorld, par3StructureBoundingBox, cube.minX, cube.minY, cube.minZ, cube.maxX, cube.maxY, cube.maxZ, GGHelper.getAirState(), GGHelper.getAirState(), false);
					}
				}
			}
		}
	}

	protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState placeBlock, IBlockState replaceBlock, boolean alwaysReplace)
	{
		this.func_175804_a(worldIn, boundingBox, minX, minY, minZ, maxX, maxY, maxZ, placeBlock, replaceBlock, alwaysReplace);
	}

	protected void fillWithBlocksWithNotify(World par1World, StructureBoundingBox boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState state, boolean alwaysReplace)
	{
		for (int y = minY; y <= maxY; ++y)
		{
			for (int x = minX; x <= maxX; ++x)
			{
				for (int z = minZ; z <= maxZ; ++z)
				{
					if (!alwaysReplace || this.getBlockStateFromPos(par1World, x, y, z, boundingBox) != GGHelper.getAirState())
					{
						if (y != minY && y != maxY && x != minX && x != maxX && z != minZ && z != maxZ)
						{
							this.placeBlockAtCurrentPosition(par1World, state, x, y, z, boundingBox);
						}
						else
						{
							this.placeBlockAtCurrentPosition(par1World, state, x, y, z, boundingBox);
						}
					}
				}
			}
		}
	}

	public void fillVariedBlocks(World world, StructureBoundingBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState block1, IBlockState block2, int rarity, Random rand, boolean replaceBlocks)
	{
		for (int y = minY; y <= maxY; ++y)
		{
			for (int x = minX; x <= maxX; ++x)
			{
				for (int z = minZ; z <= maxZ; ++z)
				{
					if (this.getBlockStateFromPos(world, x, y, z, box) == Blocks.air || replaceBlocks)
					{
						IBlockState blockID = block2;

						if (rand.nextInt(rarity) == 1)
						{
							blockID = block1;
						}

						this.placeBlockAtCurrentPosition(world, blockID, x, y, z, box);
					}
				}
			}
		}
	}

	public StructureBoundingBox findIntersectingCube(StructureBoundingBox b1, StructureBoundingBox b2)
	{
		int minX = Math.max(b1.minX, b2.minX);
		int minY = Math.max(b1.minY, b2.minY);
		int minZ = Math.max(b1.minZ, b2.minZ);
		int maxX = Math.min(b1.maxX, b2.maxX);
		int maxY = Math.min(b1.maxY, b2.maxY);
		int maxZ = Math.min(b1.maxZ, b2.maxZ);

		if (minX < maxX && minY < maxY && minZ < maxZ)
		{
			return new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		}

		return null;
	}

	public StructureBoundingBox findValidPlacement(List components, Random random, int i, int j, int k, int direction, int roomWidth, int randomWidth, int roomHeight)
	{
		int roomX = roomWidth + random.nextInt(randomWidth);
		int roomZ = roomWidth + random.nextInt(randomWidth);
		int roomY = roomHeight;
		StructureBoundingBox var6 = new StructureBoundingBox(i, j, k, i, j + roomY, k);
		/*
		if (par1Random.nextInt(4) == 0)
		{
		    var6.maxY += 4;
		}
		 */
		switch (direction)
		{
		case 0:
			var6.minX = i - 1;
			var6.maxX = i + roomX - 1;
			var6.maxZ = k + roomZ;
			break;
		case 1:
			var6.minX = i - roomX;
			var6.minZ = k - 1;
			var6.maxZ = k + roomZ - 1;
			break;
		case 2:
			var6.minX = i - 1;
			var6.maxX = i + roomX - 1;
			var6.minZ = k - roomZ;
			break;
		case 3:
			var6.maxX = i + roomX;
			var6.minZ = k - 1;
			var6.maxZ = k + roomZ - 1;
		}

		return StructureComponent.findIntersecting(components, var6) != null ? null : var6;
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound)
	{
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound)
	{
	}

	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
	{
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, BlocksAether.carved_stone.getDivineState(), GGHelper.getAirState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), GGHelper.getAirState(), false);

		//THIS CUTS THE HOLES BETWEEN ROOMS
		this.cutHolesForEntrances(worldIn, randomIn, structureBoundingBoxIn);

		return true;
	}

}
