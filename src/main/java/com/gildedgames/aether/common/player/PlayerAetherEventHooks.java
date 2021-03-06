package com.gildedgames.aether.common.player;

import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.items.ItemsAether;
import com.gildedgames.aether.common.items.armor.ItemGravititeArmor;
import com.gildedgames.aether.common.util.PlayerUtil;
import com.gildedgames.aether.common.world.chunk.AetherPlaceFlagChunkHook;
import com.gildedgames.util.modules.chunk.ChunkModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerAetherEventHooks
{
	@SubscribeEvent
	public void onLivingEntityHurt(LivingHurtEvent event)
	{
		PlayerAether aePlayer = PlayerAether.get(event.entity);

		if (aePlayer != null)
		{
			aePlayer.onHurt(event);
		}
	}

	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;

			if (PlayerUtil.wearingArmor(player, 0, ItemsAether.sentry_boots) || PlayerUtil.isWearingFullSet(player, ItemGravititeArmor.class))
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onLivingEntityJumped(LivingJumpEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;

			Class<? extends Item> fullSet = PlayerUtil.findArmorSet(player);

			if (fullSet == ItemGravititeArmor.class)
			{
				if (player.isSneaking())
				{
					player.motionY += 0.55F;

					AetherCore.PROXY.spawnJumpParticles(player.worldObj, player.posX, player.posY, player.posZ, 1.2D, 12);
				}
			}
		}
	}

	@SubscribeEvent
	public void onCalculateBreakSpeed(BreakSpeed event)
	{
		PlayerAether aePlayer = PlayerAether.get(event.entity);

		if (aePlayer != null)
		{
			aePlayer.onCalculateBreakSpeed(event);
		}
	}

	@SubscribeEvent
	public void onPlaceBlockEvent(BlockEvent.PlaceEvent event)
	{
		AetherPlaceFlagChunkHook data = ChunkModule.api().getHook(event.world, event.pos, AetherPlaceFlagChunkHook.class);

		int x = event.pos.getX(), y = event.pos.getY(), z = event.pos.getZ();

		if (data != null)
		{
			data.setExtendedBlockState(x, y, z, data.getExtendedBlockState(x, y, z).withProperty(AetherPlaceFlagChunkHook.PROPERTY_BLOCK_PLACED, true));
		}
		else
		{
			/*
			 * TODO: FIX THIS SHIT FUCK
			 */
			//System.out.println("Chunk hook is null, something is going wrong!");
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event)
	{
		PlayerAether aePlayer = PlayerAether.get(event.entity);

		if (aePlayer != null)
		{
			aePlayer.onDeath();
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event)
	{
		PlayerAether aePlayer = PlayerAether.get(event.entity);

		if (aePlayer != null)
		{
			aePlayer.dropEquipment();
		}
	}
}
