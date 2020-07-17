package com.thelastflames.skyisles.utils.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;

public class UserInterfaceUtils {
	public static void renderRecipeItem(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {
		itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getInstance().player, stack, x,y);
		RenderSystem.depthFunc(516);
		AbstractGui.fill(x,y, x + 16, y + 16, 822083583);
		RenderSystem.depthFunc(515);
		itemRenderer.renderItemOverlays(Minecraft.getInstance().fontRenderer, stack, x,y);
	}
}
