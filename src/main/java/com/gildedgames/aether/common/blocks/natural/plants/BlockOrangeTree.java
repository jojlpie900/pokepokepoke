package com.gildedgames.aether.common.blocks.natural.plants;

import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.blocks.natural.BlockAetherGrass;
import com.gildedgames.aether.common.items.ItemsAether;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockOrangeTree extends BlockAetherPlant implements IGrowable
{
	private static final int STAGE_COUNT = 5;

	public static final PropertyBool PROPERTY_IS_TOP_BLOCK = PropertyBool.create("is_top_block");

	public static final PropertyInteger PROPERTY_STAGE = PropertyInteger.create("stage", 1, STAGE_COUNT);

	public BlockOrangeTree()
	{
		super(Material.leaves);

		this.setHardness(1f);

		this.setTickRandomly(true);

		this.setBlockBounds(0.3f, 0.0F, 0.3f, 0.7f, 0.6f, 0.7f);

		this.setStepSound(Block.soundTypeGrass);

		this.setDefaultState(this.getBlockState().getBaseState().withProperty(PROPERTY_IS_TOP_BLOCK, false).withProperty(PROPERTY_STAGE, 1));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		BlockPos topBlock = state.getValue(PROPERTY_IS_TOP_BLOCK) ? pos : pos.up();
		BlockPos bottomBlock = state.getValue(PROPERTY_IS_TOP_BLOCK) ? pos.down() : pos;

		IBlockState soilState = world.getBlockState(bottomBlock.down());

		int chance = 10;

		if (soilState.getBlock() == BlocksAether.aether_grass && soilState.getValue(BlockAetherGrass.PROPERTY_VARIANT) == BlockAetherGrass.ENCHANTED_AETHER_GRASS)
		{
			chance /= 2;
		}

		if (random.nextInt(chance) == 0)
		{
			this.growTree(world, topBlock, bottomBlock, 1);
		}
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
	{
		player.triggerAchievement(StatList.mineBlockStatArray[getIdFromBlock(this)]);
		player.addExhaustion(0.025F);
	}

	@Override
	protected void invalidateBlock(World world, BlockPos pos, IBlockState state)
	{
		BlockPos adjPos = state.getValue(PROPERTY_IS_TOP_BLOCK) ? pos.down() : pos.up();

		if (world.getBlockState(adjPos).getBlock() == this)
		{
			this.destroyTree(world, pos, adjPos, true);
		}
		else
		{
			world.setBlockToAir(pos);
		}
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		IBlockState state = world.getBlockState(pos);

		boolean isRoot = !state.getValue(PROPERTY_IS_TOP_BLOCK);

		BlockPos adjPos = isRoot ? pos.up() : pos.down();

		if (state.getValue(PROPERTY_STAGE) == STAGE_COUNT)
		{
			if (!player.capabilities.isCreativeMode)
			{
				this.dropOranges(world, pos);
			}

			IBlockState adjState = world.getBlockState(adjPos);

			if (adjState.getBlock() == this)
			{
				world.setBlockState(adjPos, adjState.withProperty(PROPERTY_STAGE, 4));
			}

			world.setBlockState(pos, world.getBlockState(pos).withProperty(PROPERTY_STAGE, 4));

			return false;
		}

		this.destroyTree(world, pos, adjPos, !player.capabilities.isCreativeMode);

		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		int isTop = (meta / STAGE_COUNT);
		int stage = ((meta - (STAGE_COUNT * isTop)) % STAGE_COUNT) + 1;

		return this.getDefaultState().withProperty(PROPERTY_IS_TOP_BLOCK, isTop > 0).withProperty(PROPERTY_STAGE, stage);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int top = state.getValue(PROPERTY_IS_TOP_BLOCK) ? STAGE_COUNT : 0;
		int stage = state.getValue(PROPERTY_STAGE);

		return top + stage;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, PROPERTY_IS_TOP_BLOCK, PROPERTY_STAGE);
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		int stage = state.getValue(PROPERTY_STAGE);

		return stage < 5;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		int stage = state.getValue(PROPERTY_STAGE);

		return stage < 5;
	}

	@Override
	public boolean isSuitableSoilBlock(Block soilBlock)
	{
		return soilBlock == this || super.isSuitableSoilBlock(soilBlock);
	}

	private void dropOranges(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);

		int count = world.rand.nextInt(3) + 1;

		if (state.getBlock() == BlocksAether.aether_grass && state.getValue(BlockAetherGrass.PROPERTY_VARIANT) == BlockAetherGrass.ENCHANTED_AETHER_GRASS)
		{
			count += 1;
		}

		Block.spawnAsEntity(world, pos, new ItemStack(ItemsAether.orange, count));
	}

	private void destroyTree(World world, BlockPos pos, BlockPos adjPos, boolean doDrops)
	{
		if (doDrops)
		{
			Block.spawnAsEntity(world, pos, new ItemStack(BlocksAether.orange_tree));

			if (world.getBlockState(pos).getValue(PROPERTY_STAGE) >= STAGE_COUNT)
			{
				this.dropOranges(world, pos);
			}
		}

		world.setBlockToAir(pos);
		world.setBlockToAir(adjPos);
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		BlockPos topBlock = state.getValue(PROPERTY_IS_TOP_BLOCK) ? pos : pos.up();
		BlockPos bottomBlock = state.getValue(PROPERTY_IS_TOP_BLOCK) ? pos.down() : pos;

		this.growTree(world, topBlock, bottomBlock, rand.nextInt(3));
	}

	private void growTree(World world, BlockPos topPos, BlockPos bottomPos, int amount)
	{
		IBlockState topState = world.getBlockState(bottomPos);
		IBlockState bottomState = world.getBlockState(bottomPos);

		if (topState.getBlock() == this && bottomState.getBlock() == this)
		{
			int nextStage = topState.getValue(PROPERTY_STAGE) + amount;

			if (nextStage <= STAGE_COUNT)
			{
				if (nextStage >= 3)
				{
					if (!world.isAirBlock(topPos) && world.getBlockState(topPos).getBlock() != this)
					{
						return;
					}

					world.setBlockState(topPos, topState.withProperty(PROPERTY_STAGE, nextStage).withProperty(PROPERTY_IS_TOP_BLOCK, true));
				}

				world.setBlockState(bottomPos, topState.withProperty(PROPERTY_STAGE, nextStage).withProperty(PROPERTY_IS_TOP_BLOCK, false));
			}
		}
	}
}
