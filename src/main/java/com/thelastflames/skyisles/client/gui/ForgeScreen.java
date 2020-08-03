package com.thelastflames.skyisles.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thelastflames.skyisles.API.utils.ForgeRecipeRegistry;
import com.thelastflames.skyisles.API.utils.ToolForgeRecipe;
import com.thelastflames.skyisles.containers.ForgeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public class ForgeScreen extends ContainerScreen<ForgeContainer> implements IHasContainer<ForgeContainer> {
	public ForgeScreen(ForgeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, new StringTextComponent("Test"));
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground();
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);
	}
	
	@Override
	protected void init() {
		super.init();
	}
	
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("skyisles:textures/gui/smithing_screen.png");
	private static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
	private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
	
	ToolForgeRecipe recipeShown = null;
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		if (minecraft != null) {
			this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
			
			int i = (this.width - this.xSize) / 2;
			int j = (this.height - this.ySize) / 2;
			
			this.blit(i, j, 0, 0, 176, 166);
			
			this.minecraft.getTextureManager().bindTexture(RECIPE_BUTTON_TEXTURE);
			
			int x = 103;
			int y = 52;
			int width = 20;
			int height = 18;
			if (
					mouseX >= i + x && mouseX <= i + x + width &&
							mouseY >= j + y && mouseY <= j + y + height
			) {
				this.blit(i + x, j + y, 0, 19, width, height);
				if (leftMouseDown) {
					recipeBookOpen = !recipeBookOpen;
					leftMouseDown = false;
				}
			} else {
				this.blit(i + x, j + y, 0, 0, width, height);
			}
			if (recipeBookOpen) {
				this.minecraft.getTextureManager().bindTexture(RECIPE_BOOK);
				int bookWidth = 150;
				int bookHeight = 167;
				this.blit(i - bookWidth, j - 1, 0, 0, bookWidth, bookHeight);
				
				ArrayList<ToolForgeRecipe> recipes = ForgeRecipeRegistry.getRecipesAsList();
				RenderSystem.pushMatrix();
				int slotX = 16;
				int slotY = 34;
				for (int index = 0; index < Math.min(20, recipes.size()); index++) {
					int renderSlotX = i - bookWidth + slotX - 4 + (index * 24);
					int renderSlotY = j + slotY - 4;
					
					RenderSystem.pushMatrix();
					ItemStack stack = recipes.get(index).getOutput(null);
					ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
					this.blit(renderSlotX, renderSlotY, 29, 206, 24, 24);
					itemRenderer.renderItemIntoGUI(stack, renderSlotX + 4, renderSlotY + 4);
					RenderSystem.popMatrix();
					if (mouseX >= renderSlotX && mouseX <= renderSlotX + 24) {
						if (mouseY >= renderSlotY && mouseY <= renderSlotY + 24) {
							RenderSystem.translatef(i + 48, j + 16, 0);
							recipes.get(index).renderRecipe();
						}
					}
				}
				RenderSystem.popMatrix();
			}
		}
	}
	
	static boolean recipeBookOpen = false;
	
	boolean leftMouseDown = false;
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			leftMouseDown = true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		if (p_mouseReleased_5_ == 0) {
			leftMouseDown = false;
		}
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}
}
