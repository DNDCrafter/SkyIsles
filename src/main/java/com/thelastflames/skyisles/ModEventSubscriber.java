//package com.thelastflames.skyisles;
//
//import com.thelastflames.skyisles.dimensions.testDim;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraftforge.common.ModDimension;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.registries.ObjectHolder;
//
//@Mod.EventBusSubscriber(modid = "skyisles",bus = Mod.EventBusSubscriber.Bus.MOD)
//public class ModEventSubscriber {
//	public static DimensionType DIMENSION = null;
//
//	static ResourceLocation location = new ResourceLocation("skyisles" + ":testdimension");
//
//	@ObjectHolder("skyisles" + ":testdimension")
//	public static final ModDimension DIMHOLDER = null;
//
//	@SubscribeEvent
//	public static void onDimensionRegistryEvent(final RegistryEvent.Register<ModDimension> event) {
//		System.out.println("hello from mod dimension registry");
//		event.getRegistry().register(new testDim().setRegistryName(location));
//	}
//}
