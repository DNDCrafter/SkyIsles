package com.thelastflames.skyisles.client.gui;

import com.thelastflames.skyisles.Containers.ForgeContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

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
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
		
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		
		this.blit(i,j,0,0,176,166);
	}
}
