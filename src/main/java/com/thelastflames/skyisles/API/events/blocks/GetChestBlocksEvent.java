package com.thelastflames.skyisles.API.events.blocks;

import com.thelastflames.skyisles.blocks.ChestBlock;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;

public class GetChestBlocksEvent extends Event {
	public final ArrayList<ChestBlock> blocks = new ArrayList<>();
}
