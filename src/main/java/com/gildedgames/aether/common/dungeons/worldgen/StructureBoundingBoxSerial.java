package com.gildedgames.aether.common.dungeons.worldgen;

import com.gildedgames.util.io_manager.io.IO;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class StructureBoundingBoxSerial extends StructureBoundingBox implements IO<ByteBuf, ByteBuf>
{
	public StructureBoundingBoxSerial()
	{
	}

	public StructureBoundingBoxSerial(StructureBoundingBox boundingBox)
	{
		super(boundingBox);
	}

	public StructureBoundingBoxSerial(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		super(par1, par2, par3, par4, par5, par6);
	}

	public StructureBoundingBoxSerial(int par1, int par2, int par3, int par4)
	{
		super(par1, par2, par3, par4);
	}

	/**
	 * returns a new StructureBoundingBox with MAX values
	 */
	public static StructureBoundingBoxSerial getNewBoundingBox()
	{
		return new StructureBoundingBoxSerial(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	@Override
	public void write(ByteBuf output)
	{
		output.writeInt(this.minX);
		output.writeInt(this.minY);
		output.writeInt(this.minZ);

		output.writeInt(this.maxX);
		output.writeInt(this.maxY);
		output.writeInt(this.maxZ);
	}

	@Override
	public void read(ByteBuf input)
	{
		this.minX = input.readInt();
		this.minY = input.readInt();
		this.minZ = input.readInt();

		this.maxX = input.readInt();
		this.maxY = input.readInt();
		this.maxZ = input.readInt();
	}

}
