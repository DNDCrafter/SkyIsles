package com.thelastflames.skyisles.API.events.utils;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.Event;

public class FindBlockEvent extends Event {
	public final Item input;
	public Item output;
	
	public FindBlockEvent(Item input, Item output) {
		this.input = input;
		this.output = output;
	}
	
	@Override
	public boolean isCancelable() {
		return false;
	}
}
