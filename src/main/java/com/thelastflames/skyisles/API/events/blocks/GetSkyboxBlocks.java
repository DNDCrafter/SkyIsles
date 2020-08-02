package com.thelastflames.skyisles.API.events.blocks;

import com.thelastflames.skyisles.blocks.bases.SkyboxBlock;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;

public class GetSkyboxBlocks extends Event {
	public final ArrayList<SkyboxBlock> blocks = new ArrayList<>();
}
