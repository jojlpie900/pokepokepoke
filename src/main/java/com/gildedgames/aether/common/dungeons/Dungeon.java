package com.gildedgames.aether.common.dungeons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.dungeons.worldgen.StructureBoundingBoxSerial;
import com.gildedgames.aether.common.dungeons.worldgen.rooms.DungeonRoom;
import com.gildedgames.aether.common.player.PlayerAether;
import com.gildedgames.util.core.nbt.NBT;
import com.gildedgames.util.instances.BlockPosDimension;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class Dungeon implements NBT
{

	public int posX, posZ;

	public LinkedList<DungeonRoom> rooms = new LinkedList<DungeonRoom>();

	public int dungeonId;

	public StructureBoundingBoxSerial boundingBox = new StructureBoundingBoxSerial();

	private int keyFragments = 0;

	private final List<EntityPlayer> members = new ArrayList<EntityPlayer>();

	private BlockPos entrance = new BlockPos(0, 0, 0);

	private BlockPosDimension outsideEntrance;

	public boolean conquered;

	public boolean startTime;

	public int dungeonTime;

	public Dungeon()
	{
	}

	public void generate(BlockPosDimension outsideEntrance)
	{
		this.outsideEntrance = outsideEntrance;

		World world = MinecraftServer.getServer().worldServerForDimension(Aether.getDungeonDimensionID());

		this.generateRooms(world, world.rand, 0, 0);

		for (DungeonRoom dungeonRoom : this.rooms)
		{
			if (dungeonRoom instanceof DungeonRoomLabyrinthEntrance)
			{
				this.entrance.x = dungeonRoom.getBoundingBox().minX + 3;
				this.entrance.y = dungeonRoom.getBoundingBox().maxY - 4;
				this.entrance.z = dungeonRoom.getBoundingBox().minZ + 3;

				break;
			}
		}
	}

	public void enter(EntityPlayerMP player)
	{
		this.members.add(player);
		this.startTime = true;

		PlayerAether playerInfo = PlayerAether.getPlayer(player);

		playerInfo.setCurrentDungeonId(this.dungeonId);
		playerInfo.setNonDungeonPos(new EntityPosition(player.posX, player.posY, player.posZ));

		player.triggerAchievement(AetherAchievements.enterDungeonBronze);

		this.teleportPlayerToDungeons(player);

		this.movePlayerToEntrance(player);
	}

	public void leave(EntityPlayerMP player)
	{
		this.members.remove(player);

		this.teleportPlayerOutside(player, this.outsideEntrance.dimId());

		PlayerAether playerInfo = PlayerAether.getPlayer(player);

		BlockPos nonDungeonPos = playerInfo.getNonDungeonPos();
		player.playerNetServerHandler.setPlayerLocation(this.outsideEntrance.getX() + 2D, this.outsideEntrance.getY() + 2D, this.outsideEntrance.getZ() + 2D, 0, 0);

		while (!player.worldObj.getCollidingBoundingBoxes(player, player.getBoundingBox()).isEmpty())
		{
			player.setPosition(player.posX + 1D, player.posY + 1.0D, player.posZ + 1D);
		}

		playerInfo.setCurrentDungeonId(-1);
	}

	public void finish()
	{
		this.conquered = true;
		this.startTime = false;

		for (EntityPlayer player : this.members)
		{
			PlayerAether playerInfo = PlayerAether.getPlayer(player);

			playerInfo.addBannedEntrance(this.outsideEntrance);
		}

		this.sendPacketToMembers(new PacketDungeonInfo(this));
	}

	public void teleportPlayerToDungeons(EntityPlayerMP player)
	{
		TeleporterDungeons teleporter = new TeleporterDungeons(MinecraftServer.getServer().worldServerForDimension(Aether.getDungeonDimensionID()));
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		scm.transferPlayerToDimension(player, AetherCore.dungeonDimId(), teleporter);
		player.timeUntilPortal = player.getPortalCooldown();
	}

	public void teleportPlayerOutside(EntityPlayerMP player, int dimID)
	{
		TeleporterDungeons teleporter = new TeleporterDungeons(MinecraftServer.getServer().worldServerForDimension(dimID));
		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		scm.transferPlayerToDimension(player, dimID, teleporter);
		player.timeUntilPortal = player.getPortalCooldown();
	}

	public void movePlayerToEntrance(EntityPlayerMP player)
	{
		AetherCore.print("moving player to entrance");
		player.posX = this.entrance.x;
		player.posY = this.entrance.y;
		player.posZ = this.entrance.z;

		WorldServer world = MinecraftServer.getServer().worldServerForDimension(AetherCore.dungeonDimId());

		world.theChunkProviderServer.loadChunk((int) player.posX >> 4, (int) player.posZ >> 4);

		/*while (!world.getCollidingBoundingBoxes(player, player.boundingBox).isEmpty())
		{
			player.setPosition(player.posX, player.posY + 1.0D, player.posZ);
		}*/

		player.playerNetServerHandler.setPlayerLocation(this.entrance.x, this.entrance.y, this.entrance.z, 0, 0);

		AetherCore.print("moved played to entrance: " + AetherCore.dungeonDimId() + " " + this.entrance.getX() + " " + this.entrance.getY() + " " + this.entrance.getZ());
		//player.playerNetServerHandler.sendPacketToPlayer(new Packet6SpawnPosition((int) this.entrance.x, (int) this.entrance.y, (int) this.entrance.z));
	}

	public void sendPacketToMembers(AetherPacket packet)
	{
		for (EntityPlayer entityPlayer : this.members)
		{
			AetherPacketHandler.sendTo(packet, (EntityPlayerMP) entityPlayer);
		}
	}

	public void updateBoundingBox()
	{
		this.boundingBox = StructureBoundingBoxSerial.getNewBoundingBox();
		Iterator componentsIt = this.rooms.iterator();

		while (componentsIt.hasNext())
		{
			DungeonRoom component = (DungeonRoom) componentsIt.next();

			StructureBoundingBoxSerial serialBox = new StructureBoundingBoxSerial(component.getBoundingBox());

			this.boundingBox.expandTo(serialBox);
		}
	}

	public void addKeyFragment()
	{
		this.keyFragments++;
		this.sendPacketToMembers(new PacketDungeonInfo(this));
	}

	public void setKeyFragments(int keyFragments)
	{
		this.keyFragments = keyFragments;
		this.sendPacketToMembers(new PacketDungeonInfo(this));
	}

	public int getKeyFragments()
	{
		return this.keyFragments;
	}

	public void readData(ByteBuf dataInput)
	{
		this.dungeonId = dataInput.readInt();
		this.boundingBox.read(dataInput);
		this.keyFragments = dataInput.readInt();
		this.conquered = dataInput.readBoolean();
	}

	public void writeData(ByteBuf dataOutput)
	{
		dataOutput.writeInt(this.dungeonId);
		this.boundingBox.write(dataOutput);
		dataOutput.writeInt(this.keyFragments);
		dataOutput.writeBoolean(this.conquered);
	}

	@Override
	public void read(NBTTagCompound input)
	{
		this.dungeonId = input.getInteger("dungeonId");
		this.boundingBox = (StructureBoundingBoxSerial) ByteDecoder.decode(StructureBoundingBoxSerial.class, input.getByteArray("boundingBox"));
		this.keyFragments = input.getInteger("keys");
		this.entrance = (EntityPosition) ByteDecoder.decode(EntityPosition.class, input.getByteArray("entrance"));
		this.conquered = input.getBoolean("conquered");
		this.outsideEntrance = (DungeonPosition) ByteDecoder.decode(DungeonPosition.class, input.getByteArray("outsideEntrance"));
	}

	@Override
	public void write(NBTTagCompound output)
	{
		output.setInteger("dungeonId", this.dungeonId);
		output.setByteArray("boundingBox", ByteEncoder.encode(this.boundingBox));
		output.setInteger("keys", this.keyFragments);
		output.setByteArray("entrance", ByteEncoder.encode(this.entrance));
		output.setBoolean("conquered", this.conquered);
		output.setByteArray("outsideEntrance", ByteEncoder.encode(this.outsideEntrance));
	}

	public void generateStructure(World world, Random rand, StructureBoundingBox structureBoundingBox)
	{

	}

	protected StructureComponent getRandomRoom(DungeonRoom previousRoom, List components, Random rand, int i, int j, int k, int direction, int componentNumber)
	{
		return null;
	}

	public int numberOfCertainRoom(List rooms, Class room)
	{
		Iterator compsIter = rooms.iterator();
		StructureComponent component;

		int max = 0;

		do
		{
			if (!compsIter.hasNext())
			{
				return max;
			}

			component = (StructureComponent) compsIter.next();

			if (room.isInstance(component))
			{
				max += 1;
			}
		}
		while (true);
	}

	public boolean hasIntersectingEntrances(DungeonRoom room1, DungeonRoom room2)
	{
		Iterator iterMyEntrance = room1.entrances.iterator();

		while (iterMyEntrance.hasNext())
		{
			StructureBoundingBox myCube = (StructureBoundingBox) iterMyEntrance.next();
			Iterator iterRoomEntrance = room2.entrances.iterator();

			while (iterRoomEntrance.hasNext())
			{
				StructureBoundingBox cube = this.findIntersectingCube(myCube, (StructureBoundingBox) iterRoomEntrance.next());

				if (cube != null)
				{
					return true;
				}
			}
		}

		return false;
	}

	public StructureBoundingBox findIntersectingCube(StructureBoundingBox box1, StructureBoundingBox box2)
	{
		int minX = Math.max(box1.minX, box2.minX);
		int minY = Math.max(box1.minY, box2.minY);
		int minZ = Math.max(box1.minZ, box2.minZ);
		int maxX = Math.min(box1.maxX, box2.maxX);
		int maxY = Math.min(box1.maxY, box2.maxY);
		int maxZ = Math.min(box1.maxZ, box2.maxZ);

		if (minX < maxX && minY < maxY && minZ < maxZ)
		{
			return new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		}

		return null;
	}

	public StructureComponent getNextDungeonRoom(DungeonRoom previousRoom, List<StructureComponent> rooms, Random rand, int i, int j, int k, int direction, int roomNumber)
	{
		return null;
	}

	public String getDungeonName()
	{
		return "";
	}

	public void createRooms(int x, int z, Random rand, int xOffset, int zOffset)
	{
		this.posX = (x << 4) + 2 + xOffset;
		this.posZ = (z << 4) + 2 + zOffset;

		DungeonRoomSliderBoss bossRoom = new DungeonRoomSliderBoss(this, 0, null, rand, xOffset, zOffset);

		this.rooms.add(bossRoom);

		bossRoom.buildComponent(this.rooms, rand);

		DungeonRoom room;

		for (int numb = 1; numb < 8; numb++)
		{
			List roomList = (List) this.rooms.clone();
			Iterator roomIter = roomList.iterator();

			do
			{
				room = (DungeonRoom) roomIter.next();

				if (room.getComponentType() == numb)
				{
					room.buildComponent(room, this.rooms, rand);
				}
			}
			while (roomIter.hasNext());
		}

		this.updateBoundingBox();

		this.dungeonId = DungeonHandler.instance().getInstancesSize();
	}

	protected void fillWithBlocks(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Block edgeBlockID, Block fillBlockID, boolean replaceBlocks)
	{
		for (int y = minY; y <= maxY; ++y)
		{
			for (int x = minX; x <= maxX; ++x)
			{
				for (int z = minZ; z <= maxZ; ++z)
				{
					if (replaceBlocks || world.isAirBlock(x, y, z))
					{
						if (y != minY && y != maxY && x != minX && x != maxX && z != minZ && z != maxZ)
						{
							// world.setBlock(x, y, z, fillBlockID);
						}
						else
						{
							world.setBlock(x, y, z, edgeBlockID);
						}
					}
				}
			}
		}
	}

	public boolean generateRooms(World world, Random rand, int x, int z)
	{
		Collection<Dungeon> instances = DungeonHandler.instance().getInstances();

		//Dungeon lastDungeon = instances != null && !instances.isEmpty() ? (Dungeon) instances.toArray()[instances.size() - 1] : null;
		int maxX = 0;

		for (Dungeon dungeon : instances)
		{
			if (dungeon.boundingBox.maxX > maxX)
			{
				maxX = dungeon.boundingBox.maxX;
			}
		}

		int xMaxOffset = maxX + 110;

		this.createRooms(x, z, rand, xMaxOffset, 0);

		/*
		 * Iterator componentsIt = this.rooms.iterator();
		 *
		 * while (componentsIt.hasNext()) { DungeonRoom component =
		 * (DungeonRoom) componentsIt.next();
		 *
		 * component.getBoundingBox().offset(xMaxOffset, 0, 0); }
		 */

		// this.updateBoundingBox();

		DungeonHandler.instance().addInstance(this, true);
		int dungeonChunkLength = (this.boundingBox.maxZ - this.boundingBox.minZ) >> 4;
		int dungeonChunkWidth = (this.boundingBox.maxX - this.boundingBox.minX) >> 4;

		int chunkMinX = this.boundingBox.minX >> 4;
		int chunkMinZ = this.boundingBox.minZ >> 4;

		boolean generatedRooms = false;

		for (int chunkX = chunkMinX - 2; chunkX <= chunkMinX + dungeonChunkWidth + 2; chunkX++)
		{
			for (int chunkZ = chunkMinZ - 2; chunkZ <= chunkMinZ + dungeonChunkLength + 2; chunkZ++)
			{
				MinecraftServer.getServer().worldServerForDimension(Aether.getDungeonDimensionID()).getChunkProvider().loadChunk(chunkX, chunkZ);

				int posX = (chunkX << 4) + 8;
				int posZ = (chunkZ << 4) + 8;

				if (this.boundingBox.intersectsWith(posX - 15, posZ - 15, posX + 31, posZ + 31))
				{
					this.generateStructure(world, rand, new StructureBoundingBox(posX, posZ, posX + 15, posZ + 15));
					generatedRooms = true;
				}
			}
		}

		this.fillWithBlocks(world, this.boundingBox.minX - 1, this.boundingBox.minY - 1, this.boundingBox.minZ - 1, this.boundingBox.maxX + 1, this.boundingBox.maxY + 1, this.boundingBox.maxZ + 1, AetherBlocks.BloodMossHolystone, Blocks.air, true);

		return generatedRooms;
	}

	public ArrayList<EntityPlayer> getMembers()
	{
		return this.members;
	}
}
