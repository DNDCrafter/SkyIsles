package com.thelastflames.skyisles.Utils;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Objects;

public class MaterialList {
	public ArrayList<String> names=new ArrayList<>();
	
	@Override
	public String toString() {
		StringBuilder val= new StringBuilder();
		for (String s:names) {
			val.append(s).append(";");
		}
		return val.toString();
	}
	
	public static MaterialList fromString(String s) {
		MaterialList list=new MaterialList();
		for (String s1:s.split(";")) {
			try {
//				System.out.println(s1);
				if (ForgeRegistries.ITEMS.getValue(new ResourceLocation(s1))!=Items.AIR) {
					list.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(s1)));
				} else {
					list.add(s1);
				}
			} catch (Exception err) {
				list.add(s1);
			}
//			System.out.println(list.names.get(list.names.size()-1));
		}
		return list;
	}
	
	public void add(String s) {
		this.names.add(s);
	}
	
	public void add(Item item) {
		String returnval=ConfigLookup.lookupImage(item);
		if (returnval.equals("")) {
			returnval= Objects.requireNonNull(item.getRegistryName()).getNamespace()+":block/"+item.getRegistryName().getPath();
			if (!(item instanceof BlockItem)) {
				if (returnval.contains("_ingot")) {
					returnval=returnval.replace("_ingot","_block");
				}
			}
		}
		this.names.add(returnval);
	}
}
