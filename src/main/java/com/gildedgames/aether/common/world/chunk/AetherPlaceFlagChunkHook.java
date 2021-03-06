package com.gildedgames.aether.common.world.chunk;

import com.gildedgames.util.modules.chunk.api.hook.ExtendedBlockStateChunkHook;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

import java.util.List;

public class AetherPlaceFlagChunkHook extends ExtendedBlockStateChunkHook
{
	public static PropertyBool PROPERTY_BLOCK_PLACED = PropertyBool.create("blockPlaced");

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PROPERTY_BLOCK_PLACED) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getBaseState().withProperty(PROPERTY_BLOCK_PLACED, meta == 1);
	}

	@Override
	public BlockState createBlockState()
	{
		return new BlockState(null, PROPERTY_BLOCK_PLACED);
	}

	@Override
	public String getName()
	{
		return "aether:placeFlags";
	}
}
