package com.thelastflames.skyisles;

import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.registry.SkyDimensions;
import com.thelastflames.skyisles.registry.SkyItems;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SkyIsles.ModID)
public class SkyIsles {
	
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static final String ModID="skyisles";
	
	public SkyIsles() {
		IEventBus bus=FMLJavaModLoadingContext.get().getModEventBus();
		
		bus.addListener(this::clientSetupEvent);
		
		ITSERLookup.setupLookup();
		
		SkyBlocks.BLOCKS.register(bus);
		SkyItems.ITEMS.register(bus);
		SkyDimensions.MOD_DIMENSIONS.register(bus);
		SkyTileEntities.TILE_ENTITIES.register(bus);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static void teleportPlayer(ServerPlayerEntity player, DimensionType destinationType, BlockPos destinationPos) {
		ServerWorld nextWorld = player.getServer().getWorld(destinationType);
		nextWorld.getChunk(destinationPos);    // make sure the chunk is loaded
		player.teleport(nextWorld, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), player.rotationYaw, player.rotationPitch);
	}
	
	public void clientSetupEvent(FMLClientSetupEvent event) {
		ClientSetup.run(event);
	}
}
