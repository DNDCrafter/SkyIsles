//package com.thelastflames.skyisles.utils.client;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.thelastflames.skyisles.registry.SkyItems;
//import com.thelastflames.skyisles.utils.StatsHelper;
//import com.thelastflames.skyisles.utils.ToolHelper;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.AbstractGui;
//import net.minecraft.client.renderer.ItemRenderer;
//import net.minecraft.item.ItemStack;
//
//public class UserInterfaceUtils {
//	public static void renderRecipeItem(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {
//		itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getInstance().player, stack, x, y);
//		RenderSystem.depthFunc(516);
//		AbstractGui.fill(x, y, x + 16, y + 16, 822083583);
//		RenderSystem.depthFunc(515);
//		itemRenderer.renderItemOverlays(Minecraft.getInstance().fontRenderer, stack, x, y);
//	}
//
//	public static void renderRecipeItemWithTool(ItemStack stack, int x, int y, ItemRenderer itemRenderer) {
//		renderRecipeItem(stack, x, y, itemRenderer);
//		ItemStack stack1 = ToolHelper.swapForTool(stack);
//		RenderSystem.pushMatrix();
//		RenderSystem.translatef(x + 8, y + 8, 0);
//		RenderSystem.scalef(0.5f, 0.5f, 1);
//		if (!stack1.isItemEqual(stack)) {
//			renderRecipeItem(stack1, 0, 0, itemRenderer);
//		} else if (StatsHelper.get(stack1.getItem()) != null) {
//			renderRecipeItem(new ItemStack(SkyItems.SKYISLES_ICON.get()), 0, 0, itemRenderer);
//		}
//		RenderSystem.popMatrix();
//	}
//}
