package com.gildedgames.aether.common.dungeons;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.gildedgames.aether.common.dungeons.worldgen.rooms.DungeonRoom;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class DungeonSliderLabyrinth extends Dungeon
{

	public final static int KEY_FRAGMENTS_REQUIRED = 3;

	public final static int DUNGEON_AREA = 5;

	public DungeonSliderLabyrinth()
	{
		super();
	}

	@Override
	protected DungeonRoom getRandomRoom(DungeonRoom previousRoom, List rooms, Random rand, int i, int j, int k, int direction, int roomNumber)
	{
		int chance = rand.nextInt(100);
		StructureBoundingBox boundingBox;

		if (this.numberOfCertainRoom(rooms, DungeonRoomLabyrinthEntrance.class) < 1)
		{
			boundingBox = DungeonRoomLabyrinthEntrance.findValidPlacement(rooms, rand, i, j, k, direction);

			if (boundingBox != null)
			{
				DungeonRoom room = new DungeonRoomLabyrinthEntrance(this, roomNumber, previousRoom, rand, boundingBox, direction);

				return room;
			}
		}

		if (this.numberOfCertainRoom(rooms, DungeonRoomLabyrinthEye.class) < 1 && (rand.nextInt(10) == 0 || roomNumber >= DUNGEON_AREA))
		{
			boundingBox = DungeonRoomLabyrinthEye.findValidPlacement(rooms, rand, i, j, k, direction);

			if (boundingBox != null)
			{
				DungeonRoomLabyrinthEye room = new DungeonRoomLabyrinthEye(this, roomNumber, previousRoom, rand, boundingBox, direction);

				if (this.hasIntersectingEntrances(previousRoom, room))
				{
					return room;
				}
			}
		}

		if (this.numberOfCertainRoom(rooms, DungeonRoomSentryGuardian.class) < 1 && (rand.nextInt(10) == 0 || roomNumber >= DUNGEON_AREA))
		{
			boundingBox = DungeonRoomSentryGuardian.findValidPlacement(rooms, rand, i, j, k, direction);

			if (boundingBox != null)
			{
				DungeonRoomSentryGuardian room = new DungeonRoomSentryGuardian(this, roomNumber, previousRoom, rand, boundingBox, direction);

				if (this.hasIntersectingEntrances(previousRoom, room))
				{
					return room;
				}
			}
		}

		if (this.numberOfCertainRoom(rooms, DungeonRoomSliderMimic.class) < 1 && (rand.nextInt(10) == 0 || roomNumber >= DUNGEON_AREA))
		{
			boundingBox = DungeonRoomSliderMimic.findValidPlacement(rooms, rand, i, j, k, direction);

			if (boundingBox != null)
			{
				DungeonRoomSliderMimic room = new DungeonRoomSliderMimic(this, roomNumber, previousRoom, rand, boundingBox, direction);

				if (this.hasIntersectingEntrances(previousRoom, room))
				{
					return room;
				}
			}
		}

		if (chance < 30)
		{
			boundingBox = DungeonRoomCorridor.findValidPlacement(rooms, rand, i, j, k, direction);

			if (boundingBox != null)
			{
				DungeonRoom room = new DungeonRoomCorridor(this, roomNumber, previousRoom, rand, boundingBox, direction);

				if (this.hasIntersectingEntrances(previousRoom, room))
				{
					return room;
				}
			}
		}
		else if (chance < 50)
		{
			boundingBox = DungeonRoomChests.findValidPlacement(rooms, rand, i, j, k, direction);

			if (boundingBox != null)
			{
				DungeonRoom room = new DungeonRoomChests(this, roomNumber, previousRoom, rand, boundingBox, direction);

				if (this.hasIntersectingEntrances(previousRoom, room))
				{
					return room;
				}
			}
		}
		else if (chance < 70)
		{
			if (!(previousRoom instanceof DungeonRoomStairs))
			{
				boundingBox = DungeonRoomStairs.findValidPlacement(rooms, rand, i, j, k, direction);

				if (boundingBox != null)
				{
					DungeonRoom room = new DungeonRoomStairs(this, roomNumber, previousRoom, rand, boundingBox, direction);

					if (this.hasIntersectingEntrances(previousRoom, room))
					{
						return room;
					}
				}
			}
		}
		else if (chance >= 70)
		{
			boundingBox = DungeonRoomChestPillar.findValidPlacement(rooms, rand, i, j, k, direction);

			if (boundingBox != null)
			{
				DungeonRoom room = new DungeonRoomChestPillar(this, roomNumber, previousRoom, rand, boundingBox, direction);

				if (this.hasIntersectingEntrances(previousRoom, room))
				{
					return room;
				}
			}
		}

		return null;
	}

	@Override
	public DungeonRoom getNextDungeonRoom(DungeonRoom previousRoom, List rooms, Random rand, int i, int j, int k, int direction, int roomNumber)
	{
		if (roomNumber > DUNGEON_AREA) //More than 8 components away from first
		{
			return null;
		}

		if (Math.abs(i - this.posX) <= 80 && Math.abs(k - this.posZ) <= 80)
		{
			DungeonRoom dungeonRoom = this.getRandomRoom(previousRoom, rooms, rand, i, j, k, direction, roomNumber + 1);

			if (dungeonRoom != null)
			{
				rooms.add(dungeonRoom);
			}

			return dungeonRoom;
		}

		return null;
	}

	@Override
	public String getDungeonName()
	{
		return "Slider's Labyrinth";
	}

	@Override
	public void generateStructure(World world, Random rand, StructureBoundingBox structureBoundingBox)
	{
		Iterator roomIter = this.rooms.iterator();

		while (roomIter.hasNext())
		{
			DungeonRoom room = (DungeonRoom) roomIter.next();

			if (room.getBoundingBox().intersectsWith(structureBoundingBox) && !room.addComponentParts(world, rand, structureBoundingBox))
			{
				roomIter.remove();
			}
		}
	}
}
