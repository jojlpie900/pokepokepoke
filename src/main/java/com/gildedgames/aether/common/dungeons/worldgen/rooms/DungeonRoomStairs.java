package net.aetherteam.aether.dungeons.worldgen.rooms;

import java.util.List;
import java.util.Random;

import com.gildedgames.aether.common.dungeons.Dungeon;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class DungeonRoomStairs extends DungeonRoom
{
	public DungeonRoomStairs(Dungeon dungeon, int componentType, StructureComponent previousStructure, Random rand, StructureBoundingBox structureBoundingBox, int direction)
	{
		super(dungeon, componentType, previousStructure, rand);

		this.boundingBox = structureBoundingBox;

		int floornumb = (this.boundingBox.maxY - this.boundingBox.minY);

		for (int floors = 0; floors < floornumb; floors += 5)
		{
			this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + floors, this.boundingBox.minZ));
			this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ + 2));
			this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ - 1));
			this.entrances.add(new StructureBoundingBox(this.boundingBox.maxX, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ - 1));
		}
	}

	@Override
	public void buildComponent(List par2List, Random par3Random)
	{
		int floornumb = (this.boundingBox.maxY - this.boundingBox.minY);

		for (int offset = 1; offset < 3; offset++)
		{
			for (int floors = 0; floors < floornumb; floors += 5)
			{
				this.addStructure(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + floornumb + 1, this.boundingBox.maxZ + 1, 0, this.componentType);
				this.addStructure(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ + offset, 1, this.componentType);
				this.addStructure(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ - 1, 2, this.componentType);
				this.addStructure(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ + offset, 3, this.componentType);
			}

			this.addStructure(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + 5, this.boundingBox.maxZ + 1, 0, this.componentType);
			this.addStructure(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + 5, this.boundingBox.minZ + offset, 1, this.componentType);
			this.addStructure(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + 5, this.boundingBox.minZ - 1, 2, this.componentType);
			this.addStructure(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + 5, this.boundingBox.minZ + offset, 3, this.componentType);
		}
	}

	@Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
	{

		this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.DungeonStone, Blocks.air, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);
		int floornumb = (this.boundingBox.maxY - this.boundingBox.minY) / 5;
		for (int floors = 1; floors < floornumb; floors++)
		{
			this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY + 5 * floors, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY + 5 * floors, this.boundingBox.maxZ, AetherBlocks.DungeonStone, Blocks.air, false);
			this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 2, this.boundingBox.minY + 5 * floors, this.boundingBox.minZ + 2, this.boundingBox.maxX - 2, this.boundingBox.minY + 5 * floors, this.boundingBox.maxZ - 2, Blocks.air, Blocks.air, false);

			int offset = 5 * floors - 5;

			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 1 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + offset, this.boundingBox.minZ + 3, par3StructureBoundingBox);

			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + offset, this.boundingBox.minZ + 4, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 2 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);

			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 3 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + offset, this.boundingBox.minZ + 4, par3StructureBoundingBox);

			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + offset, this.boundingBox.minZ + 3, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 4 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);

			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 5 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonStone, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + offset, this.boundingBox.minZ + 3, par3StructureBoundingBox);

		}
		this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 3, this.boundingBox.minY + 1, this.boundingBox.minZ + 3, this.boundingBox.maxX - 3, this.boundingBox.maxY - 2, this.boundingBox.maxZ - 3, AetherBlocks.DungeonStone, Blocks.air, false);

		this.cutHolesForEntrances(par1World, par2Random, par3StructureBoundingBox);

		return true;

	}

}
