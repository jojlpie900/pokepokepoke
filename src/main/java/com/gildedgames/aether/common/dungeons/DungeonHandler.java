package com.gildedgames.aether.common.dungeons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.util.GGHelper;
import com.gildedgames.util.core.nbt.NBT;
import com.gildedgames.util.core.nbt.NBTFactory;
import com.gildedgames.util.io_manager.util.IOUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class DungeonHandler implements NBT
{

	private final Map<Integer, Dungeon> instances = new HashMap<Integer, Dungeon>();

	private final List<Integer> sentRequest = new ArrayList<Integer>();

	private static DungeonHandler clientHandler = new DungeonHandler();

	private static DungeonHandler serverHandler = new DungeonHandler();

	public static void clear()
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (side.isClient())
		{
			clientHandler = new DungeonHandler();
		}
		else
		{
			serverHandler = new DungeonHandler();
		}
	}

	public static DungeonHandler instance()
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (side.isClient())
		{
			return clientHandler;
		}
		else
		{
			return serverHandler;
		}
	}

	public void addInstance(Dungeon newDungeon, boolean saveData)
	{
		this.instances.put(newDungeon.dungeonId, newDungeon);

		if (saveData)
		{
			AetherCore.flushData();
		}
	}

	public Dungeon getDungeon(int dungeonId)
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (side.isClient())
		{
			Dungeon dungeon = this.instances.get(dungeonId);

			if (dungeon == null)
			{
				if (!this.sentRequest.contains(dungeonId))
				{
					AetherPacketHandler.sendToServer(new PacketDungeonInfoRequest(dungeonId));
					this.sentRequest.add(dungeonId);
				}
			}

			return dungeon;
		}
		else
		{
			return this.instances.get(dungeonId);
		}
	}

	public Dungeon getDungeonAt(BlockPos pos)
	{
		for (Dungeon dungeon : this.instances.values())
		{
			if (GGHelper.contains(dungeon.boundingBox, pos))
			{
				return dungeon;
			}
		}

		return null;
	}

	public int getInstancesSize()
	{
		return this.instances.size();
	}

	public Collection<Dungeon> getInstances()
	{
		return this.instances.values();
	}

	private void setDungeons(List<Dungeon> dungeons)
	{
		for (Dungeon dungeon : dungeons)
		{
			this.addInstance(dungeon, false);
		}
	}

	@Override
	public void write(NBTTagCompound output)
	{
		NBTFactory factory = new NBTFactory();

		IOUtil.setIOList("dungeons", new ArrayList(this.getInstances()), factory, output);
	}

	@Override
	public void read(NBTTagCompound input)
	{
		NBTFactory factory = new NBTFactory();

		List<Dungeon> parties = (List<Dungeon>) IOUtil.getIOList("dungeons", factory, input);

		this.setDungeons(parties);
	}
}
