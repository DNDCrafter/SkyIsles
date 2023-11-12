package com.thelastflames.skyisles;

import com.thelastflames.skyisles.API.SkyIslesAPI;
import com.thelastflames.skyisles.API.events.blocks.GetChestBlocksEvent;
import com.thelastflames.skyisles.blocks.ChestBlock;
import com.thelastflames.skyisles.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
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
	
	public static final String ModID = "skyisles";
	
	private static SkyIsles INSTANCE;
	
	public static SkyIsles getInstance() {
		return INSTANCE;
	}
	
	final IEventBus bus;
	
	public SkyIsles() {
		INSTANCE = this;
		
		bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		bus.addListener(this::clientSetupEvent);
		bus.addListener(ClientSetup::registerTERS);
//		bus.addGenericListener(ContainerType.class, this::registerContainers);
//		bus.addListener(this::commonSetup);
//		SkyIslesAPI.INSTANCE.addListener(this::setupForgeRecipes);
//		SkyIslesAPI.INSTANCE.addListener(this::collectSkyboxBlocks);
		SkyIslesAPI.INSTANCE.addListener(this::collectChestBlocks);
		
//		ITSERLookup.setupLookup();
		
		SkyBlocks.BLOCKS.register(bus);
		SkyItems.ITEMS.register(bus);
//		SkyDimensions.MOD_DIMENSIONS.register(bus);
		SkyTileEntities.TILE_ENTITIES.register(bus);
//		SkyBiomes.BIOMES.register(bus);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
//	public static void teleportPlayer(ServerPlayerEntity player, DimensionType destinationType, BlockPos destinationPos) {
//		ServerWorld nextWorld = player.getServer().getWorld(destinationType);
//		nextWorld.getChunk(destinationPos);    // make sure the chunk is loaded
//		player.teleport(nextWorld, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), player.rotationYaw, player.rotationPitch);
//	}
	
//	public void commonSetup(FMLCommonSetupEvent event) {
//		ForgeRecipesEvent eventRecipes = new ForgeRecipesEvent();
//		postEvent(eventRecipes);
//		eventRecipes.finish();
//		new StatsHelper().setup();
//	}
	
	public void clientSetupEvent(FMLClientSetupEvent event) {
		ClientSetup.run(event);
	}
	
//	public void collectSkyboxBlocks(GetSkyboxBlocksEvent event) {
//		event.blocks.add((SkyboxBlock) SkyBlocks.SKYBOX_BLOCK_PURPLE.getObject1().get());
//		event.blocks.add((SkyboxBlock) SkyBlocks.SKYBOX_BLOCK.getObject1().get());
//	}

	public void collectChestBlocks(GetChestBlocksEvent event) {
		event.blocks.add((ChestBlock) SkyBlocks.CHEST_BLOCK.get());
	}

//	public void setupForgeRecipes(ForgeRecipesEvent event) {
//		event.register(new ResourceLocation("skyisles", "metal_pickaxe"), new ToolForgeRecipe() {
//			@Override
//			public boolean checkMatch(ArrayList<ItemStack> stacks) {
//				return stacks.get(0).isEmpty() &&
//						checkMetalHead(stacks.get(1)) &&
//						checkMetalHead(stacks.get(2)) &&
//						stacks.get(3).isEmpty() &&
//						(
//								stacks.get(4).getItem().getTags().contains(new ResourceLocation("minecraft:planks")) ||
//										stacks.get(4).getItem().getTags().contains(Tags.Items.RODS.getId()) ||
//										stacks.get(4).getItem().getTags().contains(Tags.Items.BONES.getId()) ||
//										stacks.get(4).getItem().equals(Items.BAMBOO)
//						) &&
//						(stacks.get(5).getItem().equals(stacks.get(1).getItem())) &&
//						(
//								stacks.get(6).getItem().getTags().contains(Tags.Items.LEATHER.getId()) ||
//										stacks.get(6).getItem().getRegistryName().equals(Items.RABBIT_HIDE.getRegistryName())
//						) &&
//						stacks.get(7).isEmpty() &&
//						stacks.get(8).isEmpty();
//			}
//
//			@Override
//			public ItemStack getOutput(ArrayList<ItemStack> stacks) {
//				if (stacks != null) {
//					ItemStack stack = new ItemStack(SkyItems.METAL_PICKAXE.get());
//					CompoundNBT nbt = stack.getOrCreateTag();
//					MaterialList list = new MaterialList();
//
//					nbt.putString("mat_stick_leather", stacks.get(6).getItem().getRegistryName().toString());
//					nbt.putString("mat_head_metal1", stacks.get(1).getItem().getRegistryName().toString());
//					nbt.putString("mat_head_metal2", stacks.get(2).getItem().getRegistryName().toString());
//					nbt.putString("mat_stick_wood", stacks.get(4).getItem().getRegistryName().toString());
//
//					nbt.putString("materials", list.toString());
//					return stack;
//				} else {
//					return new ItemStack(SkyItems.METAL_PICKAXE.get());
//				}
//			}
//
//			public boolean checkMetalHead(ItemStack stack) {
//				return
////						stack.isBeaconPayment();
//						SkyIsles.checkMetalHead(stack);
//			}
//
//			int indexIngot = 0;
//			int indexGem = 1;
//			int indexStick = 0;
//			int indexLeather = 0;
//			int renderCalls = 0;
//
//			@Override
//			public void renderRecipe() {
//				renderCalls++;
//
//				//Collect usable materials
//				ArrayList<Item> leather = new ArrayList<>();
//				ArrayList<Item> sticks = new ArrayList<>();
//				ArrayList<Item> head = new ArrayList<>();
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.INGOTS.getEntries()) {
//					itemITagEntry.populate(head);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.GEMS.getEntries()) {
//					itemITagEntry.populate(head);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.COBBLESTONE.getEntries()) {
//					itemITagEntry.populate(head);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.STONE.getEntries()) {
//					itemITagEntry.populate(head);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : ItemTags.PLANKS.getEntries()) {
//					itemITagEntry.populate(head);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.BEACON_PAYMENT.getEntries()) {
//					itemITagEntry.populate(head);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.LEATHER.getEntries()) {
//					itemITagEntry.populate(leather);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.BONES.getEntries()) {
//					itemITagEntry.populate(sticks);
//				}
//
//				for (Tag.ITagEntry<Item> itemITagEntry : Tags.Items.RODS.getEntries()) {
//					itemITagEntry.populate(sticks);
//				}
//
//				sticks.add(Items.BAMBOO);
//				leather.add(Items.RABBIT_HIDE);
//
//				//Cycle through materials
//				if (renderCalls >= 30) {
//					renderCalls = 0;
//					indexIngot++;
//					if (indexIngot >= head.size()) {
//						indexIngot = 0;
//					}
//
//					indexLeather++;
//					if (indexLeather >= leather.size()) {
//						indexLeather = 0;
//					}
//
//					indexStick++;
//					if (indexStick >= sticks.size()) {
//						indexStick = 0;
//					}
//
//					indexGem++;
//					if (indexGem >= head.size()) {
//						indexGem = 0;
//					}
//				}
//
//				//Render input
//				ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//
//				UserInterfaceUtils.renderRecipeItemWithTool(new ItemStack(head.get(indexGem)), 18 * 1, 18 * 0, itemRenderer);
//				UserInterfaceUtils.renderRecipeItemWithTool(new ItemStack(head.get(indexIngot)), 18 * 2, 18 * 0, itemRenderer);
//				UserInterfaceUtils.renderRecipeItemWithTool(new ItemStack(sticks.get(indexStick)), 18 * 1, 18 * 1, itemRenderer);
//				UserInterfaceUtils.renderRecipeItemWithTool(new ItemStack(head.get(indexGem)), 18 * 2, 18 * 1, itemRenderer);
//				UserInterfaceUtils.renderRecipeItemWithTool(new ItemStack(leather.get(indexLeather)), 18 * 0, 18 * 2, itemRenderer);
//
//				//Render output
//				ArrayList<ItemStack> stacks = new ArrayList<>();
//				stacks.add(null);
//				stacks.add(new ItemStack(head.get(indexGem)));
//				stacks.add(new ItemStack(head.get(indexIngot)));
//				stacks.add(null);
//				stacks.add(new ItemStack(sticks.get(indexStick)));
//				stacks.add(null);
//				stacks.add(new ItemStack(leather.get(indexLeather)));
//				UserInterfaceUtils.renderRecipeItem(this.getOutput(stacks), 18 * 4 + 10, 18, itemRenderer);
//			}
//		});
//	}

//	public static boolean checkMetalHead(ItemStack stack) {
//		return stack.isBeaconPayment() ||
//				stack.getItem().getTags().contains(new ResourceLocation("minecraft:planks")) ||
//				stack.getItem().getTags().contains(Tags.Items.COBBLESTONE.getId()) ||
//				stack.getItem().getTags().contains(Tags.Items.GEMS.getId()) ||
//				stack.getItem().getTags().contains(Tags.Items.INGOTS.getId()) ||
//				stack.getItem().getTags().contains(Tags.Items.STONE.getId());
//	}
	
	public static void postEvent(Event event) {
		SkyIslesAPI.INSTANCE.post(event);
	}

//	public void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
////		ToolForgeContainer.TYPE = (ContainerType<ToolForgeContainer>) new ContainerType<>(ToolForgeContainer::new).setRegistryName("skyisles", "toolforge");
////		event.getRegistry().register(ToolForgeContainer.TYPE);
//		event.getRegistry().register(
//				(ForgeContainer.TYPE = new ContainerType<>(ForgeContainer::new)).setRegistryName("skyisles", "toolforge")
//		);
////		ToolForgeContainer.TYPE=register("skyislestoolforge",ToolForgeContainer::new);
////		System.out.println(ToolForgeContainer.TYPE.toString());
//	}
	
//	private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory) {
//		return Registry.register(Registry.MENU, key, new ContainerType<>(factory));
//	}
}
