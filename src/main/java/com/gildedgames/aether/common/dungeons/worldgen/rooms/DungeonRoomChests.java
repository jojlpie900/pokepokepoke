package com.gildedgames.aether.common.dungeons.worldgen.rooms;

import java.util.Random;

import com.gildedgames.aether.common.dungeons.Dungeon;
import com.gildedgames.aether.common.world.ChunkProviderAether;
import com.gildedgames.util.GGHelper;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class DungeonRoomChests extends DungeonRoom
{

	public DungeonRoomChests(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, StructureBoundingBox structureBoundingBox, int direction)
	{
		super(dungeon, componentType, previousStructure, rand, structureBoundingBox, direction);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox chunkBox)
	{
		for (int var12 = this.boundingBox.minY; var12 <= this.boundingBox.maxY; ++var12)
		{
			for (int var13 = this.boundingBox.minX; var13 <= this.boundingBox.maxX; ++var13)
			{
				for (int var14 = this.boundingBox.minZ; var14 <= this.boundingBox.maxZ; ++var14)
				{
					this.placeBlockAtCurrentPosition(world, rand.nextInt(40) == 1 ? AetherBlocks.Trap : AetherBlocks.DungeonStone, 0, var13, var12, var14, chunkBox);
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
		this.fillWithBlocksWithNotify(world, chunkBox, minX, minY, minZ, maxX, maxY, maxZ, AetherBlocks.DungeonStone, Blocks.air, false);
		//fillWithBlocksWithNotify(par1World, ChunkBox, minX, boundingBox.maxY - 1, minZ, maxX, boundingBox.maxY - 1, maxZ, AetherBlocks.DungeonStone, 0, false);
		//fillVariedBlocks(par1World, ChunkBox, minX, boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) / 2), minZ, maxX,  boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) / 2), maxZ, AetherBlocks.LightDungeonStone, 0, AetherBlocks.DungeonStone, 0, 2, par2Random, false);
		//fillVariedBlocks(par1World, ChunkBox, minX + 1, minY, minZ + 1, maxX - 1, boundingBox.maxY - 1, maxZ - 1, AetherBlocks.CarvedDungeonWall, 0, AetherBlocks.CarvedDungeonWall, 0, 1, par2Random, true);

		//fillVariedBlocks(par1World, ChunkBox, minX, minY, minZ, minX, boundingBox.maxY - 1, minZ, AetherBlocks.CarvedDungeonWall, 0, AetherBlocks.CarvedDungeonWall, 0, 1, par2Random, false);
		//fillVariedBlocks(par1World, ChunkBox, maxX, minY, maxZ, maxX, boundingBox.maxY - 1, maxZ, AetherBlocks.CarvedDungeonWall, 0, AetherBlocks.CarvedDungeonWall, 0, 1, par2Random, false);

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

			int i = this.getXWithOffset(x + xOffset, z + zOffset);
			int j = this.getYWithOffset(y);
			int k = this.getZWithOffset(x + xOffset, z + zOffset);

			BlockPos pos = new BlockPos(i, j, k);

			if (GGHelper.contains(chunkBox, pos) && world.getBlockState(pos) != AetherBlocks.SkyrootChest && rand.nextInt(3) == 0)
			{
				TileEntitySkyrootChest chestEntity = null;

				world.setBlock(i, j, k, AetherBlocks.SkyrootChest, chestDirection, ChunkProviderAether.PLACEMENT_FLAG_TYPE);
				world.setBlockMetadataWithNotify(i, j, k, chestDirection, ChunkProviderAether.PLACEMENT_FLAG_TYPE);

				//generate content
				Side side = FMLCommonHandler.instance().getEffectiveSide();

				if (side.isServer() && !world.isRemote)
				{
					chestEntity = (TileEntitySkyrootChest) world.getTileEntity(i, j, k);

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
			else if (GGHelper.contains(chunkBox, pos) && world.getBlockState(pos) != AetherBlocks.SkyrootChestMimic && rand.nextInt(3) == 0)
			{
				world.setBlock(i, j, k, AetherBlocks.SkyrootChestMimic, chestDirection, ChunkProviderAether.PLACEMENT_FLAG_TYPE);
			}

		}

		BlockPos pos = new BlockPos(x, y, z);
		if (GGHelper.contains(chunkBox, pos))
		{
			this.placeBlockAtCurrentPosition(world, AetherBlocks.AetherMobSpawner, 0, x, y, z, chunkBox);
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(pos);

			if (spawner != null)
			{
				String mobID = rand.nextInt(3) == 1 ? "aether.trackingGolem" : "aether.sentry";
				spawner.getSpawnerBaseLogic().setEntityName(mobID);
			}
		}

		//THIS CUTS THE HOLES BETWEEN ROOMS
		this.cutHolesForEntrances(world, rand, chunkBox);

		return true;
	}
}
