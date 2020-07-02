package com.thelastflames.skyisles;

import com.thelastflames.skyisles.API.events.recipe.ForgeRecipesEvent;
import com.thelastflames.skyisles.API.utils.ToolForgeRecipe;
import com.thelastflames.skyisles.Containers.ForgeContainer;
import com.thelastflames.skyisles.registry.SkyBlocks;
import com.thelastflames.skyisles.registry.SkyDimensions;
import com.thelastflames.skyisles.registry.SkyItems;
import com.thelastflames.skyisles.registry.SkyTileEntities;
import com.thelastflames.skyisles.utils.MaterialList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SkyIsles.ModID)
public class SkyIsles {
	
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static final String ModID="skyisles";
	
	private static SkyIsles INSTANCE;
	
	public static SkyIsles getInstance() {
		return INSTANCE;
	}
	
	final IEventBus bus;
	public SkyIsles() {
		INSTANCE=this;
		
		bus=FMLJavaModLoadingContext.get().getModEventBus();
		
		bus.addListener(this::clientSetupEvent);
		bus.addGenericListener(ContainerType.class,this::registerContainers);
		bus.addListener(this::commonSetup);
		bus.addListener(this::setupForgeRecipes);
		
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
	
	public void commonSetup(FMLCommonSetupEvent event) {
		ForgeRecipesEvent eventRecipes = new ForgeRecipesEvent();
		postEvent(eventRecipes);
		eventRecipes.finish();
	}
	
	public void clientSetupEvent(FMLClientSetupEvent event) {
		ClientSetup.run(event);
	}
	
	public void setupForgeRecipes(ForgeRecipesEvent event) {
		event.register(new ResourceLocation("skyisles", "metal_pickaxe"), new ToolForgeRecipe() {
			@Override
			public boolean checkMatch(ArrayList<ItemStack> stacks) {
				return  stacks.get(0).isEmpty()&&
						checkMetalHead(stacks.get(1))&&
						checkMetalHead(stacks.get(2))&&
						stacks.get(3).isEmpty()&&
						(
								stacks.get(4).getItem().getTags().contains(new ResourceLocation("minecraft:planks"))||
								stacks.get(4).getItem().getTags().contains(Tags.Items.RODS.getId())||
								stacks.get(4).getItem().equals(Items.BONE)||
								stacks.get(4).getItem().equals(Items.BAMBOO)
						)&&
						(stacks.get(5).getItem().equals(stacks.get(1).getItem()))&&
						(stacks.get(6).getItem().getTags().contains(Tags.Items.LEATHER.getId()))&&
						stacks.get(7).isEmpty()&&
						stacks.get(8).isEmpty();
			}
			
			@Override
			public ItemStack getOutput(ArrayList<ItemStack> stacks) {
				ItemStack stack=new ItemStack(SkyItems.METAL_PICKAXE.get());
				CompoundNBT nbt=stack.getOrCreateTag();
				MaterialList list=new MaterialList();
				
				nbt.putString("mat_stick_leather",stacks.get(6).getItem().getRegistryName().toString());
				nbt.putString("mat_head_metal1",stacks.get(1).getItem().getRegistryName().toString());
				nbt.putString("mat_head_metal2",stacks.get(2).getItem().getRegistryName().toString());
				nbt.putString("mat_stick_wood",stacks.get(4).getItem().getRegistryName().toString());
				
				nbt.putString("materials",list.toString());
				return stack;
			}
			
			public boolean checkMetalHead(ItemStack stack) {
				return
//						stack.isBeaconPayment();
						SkyIsles.checkMetalHead(stack);
			}
			
			@Override
			public void renderRecipe() {
			}
		});
	}
	
	public static boolean checkMetalHead(ItemStack stack) {
		return  stack.isBeaconPayment()||
				stack.getItem().getTags().contains(new ResourceLocation("minecraft:planks"))||
				stack.getItem().getTags().contains(Tags.Items.COBBLESTONE.getId())||
				stack.getItem().getTags().contains(Tags.Items.GEMS.getId())||
				stack.getItem().getTags().contains(Tags.Items.INGOTS.getId())||
				stack.getItem().getTags().contains(Tags.Items.STONE.getId());
	}
	
	public static void postEvent(Event event) {
		getInstance().bus.post(event);
	}
	
//	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//	public static class CommonRegistry {
//		@SubscribeEvent
//		public void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
//			event.getRegistry().register(
//					(ToolForgeContainer.TYPE = new ContainerType<>(ToolForgeContainer::new)).setRegistryName("skyisles", "toolforge")
//			);
////			ToolForgeContainer.TYPE=register("skyislestoolforge",ToolForgeContainer::new);
////			System.out.println(ToolForgeContainer.TYPE.toString());
//		}
//	}
	
	
	public void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
//		ToolForgeContainer.TYPE = (ContainerType<ToolForgeContainer>) new ContainerType<>(ToolForgeContainer::new).setRegistryName("skyisles", "toolforge");
//		event.getRegistry().register(ToolForgeContainer.TYPE);
		event.getRegistry().register(
				(ForgeContainer.TYPE = new ContainerType<>(ForgeContainer::new)).setRegistryName("skyisles", "toolforge")
		);
//		ToolForgeContainer.TYPE=register("skyislestoolforge",ToolForgeContainer::new);
//		System.out.println(ToolForgeContainer.TYPE.toString());
	}
	
	private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory) {
		return Registry.register(Registry.MENU, key, new ContainerType<>(factory));
	}
}
