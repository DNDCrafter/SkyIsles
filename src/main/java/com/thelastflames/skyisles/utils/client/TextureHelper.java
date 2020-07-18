package com.thelastflames.skyisles.utils.client;

import com.thelastflames.skyisles.API.events.utils.FindBlockEvent;
import com.thelastflames.skyisles.SkyIsles;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.ForgeRegistries;
import tfc.dynamic_rendering.Color;

public class TextureHelper {
	public static ResourceLocation extractTexture(ItemStack stack) {
		return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack).getParticleTexture(EmptyModelData.INSTANCE).getName();
	}
	
	public static ResourceLocation extractTexture(Item item) {
		return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(item).getParticleTexture(EmptyModelData.INSTANCE).getName();
	}
	
	public static ResourceLocation getTextureFromID(String id) {
		return extractTexture(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)));
	}
	
	public static ResourceLocation getTextureFromBlockOfID(String id) {
		return extractTexture(swapForBlock(true,ForgeRegistries.ITEMS.getValue(new ResourceLocation(id))));
	}
	
	public static ItemStack swapForBlock(boolean useEvent,ItemStack stack) {
		ItemStack newStack=stack.copy();
		if (stack.getItem().equals(Items.IRON_INGOT)) {
			newStack=new ItemStack(Blocks.IRON_BLOCK);
		} else if (stack.getItem().equals(Items.GOLD_INGOT)) {
			newStack=new ItemStack(Blocks.GOLD_BLOCK);
		} else if (stack.getItem().equals(Items.EMERALD)) {
			newStack=new ItemStack(Blocks.EMERALD_BLOCK);
		} else if (stack.getItem().equals(Items.DIAMOND)) {
			newStack=new ItemStack(Blocks.DIAMOND_BLOCK);
		} else if (stack.getItem().equals(Items.NETHER_BRICK)) {
			newStack=new ItemStack(Blocks.NETHER_BRICKS);
		} else if (stack.getItem().equals(Items.BRICK)) {
			newStack=new ItemStack(Blocks.BRICKS);
		} else if (stack.getItem().equals(Items.QUARTZ)) {
			newStack=new ItemStack(Blocks.QUARTZ_BLOCK);
		} else if (stack.getItem().equals(Items.PRISMARINE_SHARD)) {
			newStack=new ItemStack(Blocks.PRISMARINE);
		} else if (stack.getItem().equals(Items.PRISMARINE_CRYSTALS)) {
			newStack=new ItemStack(Blocks.SEA_LANTERN);
		} else if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(stack.getItem().getRegistryName().toString().replace("ingot","block")))) {
			newStack=new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(stack.getItem().getRegistryName().toString().replace("ingot","block"))));
		} else if (useEvent) {
			FindBlockEvent event=new FindBlockEvent(stack.getItem(),newStack.getItem());
			SkyIsles.postEvent(event);
			newStack=new ItemStack(event.output);
		}
		return newStack;
	}
	
	public static ItemStack swapForBlock(boolean useEvent,Item item) {
		return swapForBlock(useEvent,new ItemStack(item));
	}
	
	public static int getFirstColor(ResourceLocation location) {
		TextureAtlasSprite sprite2 = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(location);
		
		for(int x = 0; x < sprite2.getWidth(); ++x) {
			for(int y = 0; y < sprite2.getHeight(); ++y) {
				Color c1 = new Color(sprite2.getPixelRGBA(0, x, y));
				if (c1.getAlpha() >= 128) {
					return c1.getRGB();
				}
			}
		}
		
		return 0;
	}
}
