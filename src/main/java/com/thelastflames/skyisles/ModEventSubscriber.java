package com.thelastflames.skyisles;

import com.thelastflames.skyisles.Dimensions.testDim;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static com.thelastflames.skyisles.SkyIsles.ModID;

@Mod.EventBusSubscriber(modid = ModID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {
	public static DimensionType DIMENSION = null;
	
	static ResourceLocation location=new ResourceLocation(ModID,"testdimension");
	
	@ObjectHolder(ModID+":testdimension")
	public static final ModDimension DIMHOLDER = new testDim().setRegistryName(location);
	
	@SubscribeEvent
	public static void onDimensionRegistryEvent(final RegistryEvent.Register<ModDimension> event) {
		System.out.println("hello from mod dimension registry");
		event.getRegistry().register(DIMHOLDER);
		if (DimensionType.byName(location) == null) {
			System.out.println("register dimension");
			DIMENSION = DimensionManager.registerDimension(location, ModEventSubscriber.DIMHOLDER, new PacketBuffer(Unpooled.buffer()), true);
		}
	}
}
