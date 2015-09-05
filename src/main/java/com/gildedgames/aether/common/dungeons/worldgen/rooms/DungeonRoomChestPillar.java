package com.gildedgames.aether.common.dungeons.worldgen.rooms;

import java.util.List;
import java.util.Random;

import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.dungeons.Dungeon;
import com.gildedgames.aether.common.world.ChunkProviderAether;
import com.gildedgames.util.GGHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class DungeonRoomChestPillar extends DungeonRoom
{

	public DungeonRoomChestPillar(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, StructureBoundingBox structureBoundingBox, int direction)
	{
		super(dungeon, componentType, previousStructure, rand, structureBoundingBox, direction);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox chunkBox)
	{
		// Clear Space :D
		for (int var12 = this.boundingBox.minY; var12 <= this.boundingBox.maxY; ++var12)
		{
			for (int var13 = this.boundingBox.minX; var13 <= this.boundingBox.maxX; ++var13)
			{
				for (int var14 = this.boundingBox.minZ; var14 <= this.boundingBox.maxZ; ++var14)
				{
					this.placeBlockAtCurrentPosition(world, rand.nextInt(40) == 1 ? AetherBlocks.Trap : BlocksAether.carved_stone.getDefaultState(), 0, var13, var12, var14, chunkBox);
				}
			}
		}

		int minX = this.boundingBox.minX + ((this.boundingBox.maxX - this.boundingBox.minX) / 2) - 1;
		int minY = this.boundingBox.minY + 1;
		int minZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) - 1;
		int maxX = this.boundingBox.minX + ((this.boundingBox.maxX - this.boundingBox.minX) / 2) + 1;
		int maxY = this.boundingBox.minY + 1;
		int maxZ = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2) + 1;

		this.fillWithBlocksWithNotify(world, chunkBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, GGHelper.getAirState(), false);
		this.fillWithBlocksWithNotify(world, chunkBox, minX, minY, minZ, maxX, maxY, maxZ, AetherBlocks.LabyrinthPillar, 2, Blocks.air, false);
		this.fillWithBlocksWithNotify(world, chunkBox, minX, this.boundingBox.maxY - 1, minZ, maxX, this.boundingBox.maxY - 1, maxZ, AetherBlocks.LabyrinthPillar, 2, Blocks.air, false);
		this.fillVariedBlocks(world, chunkBox, minX, this.boundingBox.minY + ((this.boundingBox.maxY - this.boundingBox.minY) / 2), minZ, maxX, this.boundingBox.minY + ((this.boundingBox.maxY - this.boundingBox.minY) / 2), maxZ, AetherBlocks.LabyrinthPillar, 2, AetherBlocks.LabyrinthPillar, 2, 2, rand, false);
		this.fillVariedBlocks(world, chunkBox, minX + 1, minY, minZ + 1, maxX - 1, this.boundingBox.maxY - 1, maxZ - 1, AetherBlocks.CarvedWall, 0, AetherBlocks.CarvedWall, 0, 1, rand, true);

		this.placeBlockAtCurrentPosition(world, AetherBlocks.LabyrinthPillar, 2, minX, minY, minZ, chunkBox);
		this.placeBlockAtCurrentPosition(world, AetherBlocks.LabyrinthPillar, 2, minX, this.boundingBox.maxY - 1, minZ, chunkBox);

		this.placeBlockAtCurrentPosition(world, AetherBlocks.LabyrinthPillar, 2, maxX, minY, maxZ, chunkBox);
		this.placeBlockAtCurrentPosition(world, AetherBlocks.LabyrinthPillar, 2, maxX, this.boundingBox.maxY - 1, maxZ, chunkBox);

		this.fillVariedBlocks(world, chunkBox, minX, minY, minZ, minX, this.boundingBox.maxY - 1, minZ, AetherBlocks.LabyrinthPillar, 0, AetherBlocks.LabyrinthPillar, 0, 1, rand, false);
		this.fillVariedBlocks(world, chunkBox, maxX, minY, maxZ, maxX, this.boundingBox.maxY - 1, maxZ, AetherBlocks.LabyrinthPillar, 0, AetherBlocks.LabyrinthPillar, 0, 1, rand, false);

		int x = this.boundingBox.minX + ((this.boundingBox.maxX - this.boundingBox.minX) / 2);
		int y = this.boundingBox.minY + 2;
		int z = this.boundingBox.minZ + ((this.boundingBox.maxZ - this.boundingBox.minZ) / 2);

		for (int chestCount = 1; chestCount <= 4; chestCount++)
		{
			if (rand.nextInt(2) != 0)
			{
				continue;
			}

			int xOffset = 0;
			int zOffset = 0;

			int chestDirection = 1;

			switch (chestCount)
			{
			case (1):
				xOffset = 1;
				chestDirection = 1;
				break;
			case (2):
				xOffset = -1;
				chestDirection = 2;
				break;
			case (3):
				zOffset = 1;
				chestDirection = 3;
				break;
			case (4):
				zOffset = -1;
				chestDirection = 4;
				break;
			}

			int newX = this.getXWithOffset(x + xOffset, z + zOffset);
			int newY = this.getYWithOffset(y);
			int newZ = this.getZWithOffset(x + xOffset, z + zOffset);
			
			BlockPos pos = new BlockPos(newX, newY, newZ);

			if (GGHelper.contains(chunkBox, pos) && world.getBlockState(pos) != BlocksAether.SkyrootChest. && rand.nextInt(3) == 0)
			{
				TileEntitySkyrootChest chestEntity = null;

				world.setBlock(newX, newY, newZ, AetherBlocks.SkyrootChest, chestDirection, ChunkProviderAether.PLACEMENT_FLAG_TYPE);
				world.setBlockMetadataWithNotify(pos, chestDirection, ChunkProviderAether.PLACEMENT_FLAG_TYPE);

				// generate content
				Side side = FMLCommonHandler.instance().getEffectiveSide();

				if (side.isServer() && !world.isRemote)
				{
					chestEntity = (TileEntitySkyrootChest) world.getTileEntity(newX, newY, newZ);

					if (chestEntity == null)
					{
						continue;
					}

					for (int count = 0; count < 2 + rand.nextInt(3); count++)
					{
						ItemStack stack = AetherLoot.NORMAL.getRandomItem(rand);

						if (!(stack.stackSize > 0))
						{
							stack.stackSize = 1;
						}

						chestEntity.setInventorySlotContents(rand.nextInt(chestEntity.getSizeInventory()), stack);
					}
				}
			}
		}

		this.placeBlockAtCurrentPosition(world, AetherBlocks.AetherMobSpawner, 0, x, y + 1, z, chunkBox);
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(new BlockPos(x, y + 1, z));

		if (spawner != null)
		{
			String mobID = rand.nextInt(3) == 1 ? "aether.sentryGolem" : "aether.battleSentry";
			spawner.getSpawnerBaseLogic().setEntityName(mobID);
		}

		// THIS CUTS THE HOLES BETWEEN ROOMS
		this.cutHolesForEntrances(world, rand, chunkBox);

		return true;
	}

	public static StructureBoundingBox findValidPlacement(List components, Random random, int i, int j, int k, int direction)
	{

		int roomX = 8 + random.nextInt(16);
		int roomZ = 8 + random.nextInt(16);
		int roomY = 10;
		StructureBoundingBox var6 = new StructureBoundingBox(i, j, k, i, j + roomY, k);
		/*
		 * if (par1Random.nextInt(4) == 0) { var6.maxY += 4; }
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
}
