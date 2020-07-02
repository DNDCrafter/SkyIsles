package com.thelastflames.skyisles.client.Item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thelastflames.skyisles.utils.MaterialList;
import com.thelastflames.skyisles.utils.StringyHashMap;
import com.thelastflames.skyisles.utils.client.TextureHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import tfc.dynamic_rendering.API.ExtrudedTexture;
import tfc.dynamic_rendering.API.PreppedModel;
import tfc.dynamic_rendering.API.Renderer;

public class PickaxeISTER extends ItemStackTileEntityRenderer {
	
	StringyHashMap<CompoundNBT, PreppedModel> models=new StringyHashMap<>();
	StringyHashMap<CompoundNBT, int[]> tints=new StringyHashMap<>();
	
	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		tints.objects.clear();
		models.objects.clear();
		tints.keys.clear();
		models.keys.clear();
		if (!models.containsKey(itemStackIn.getOrCreateTag())) {
			CompoundNBT nbt=itemStackIn.getOrCreateTag();
			if (
					nbt.contains("mat_stick_leather")&&
					nbt.contains("mat_head_metal1")&&
					nbt.contains("mat_head_metal2")&&
					nbt.contains("mat_stick_wood")
			) {
				MaterialList list=MaterialList.fromString(itemStackIn.getOrCreateTag().getString("materials"));
				ResourceLocation loc1=new ResourceLocation("skyisles:item/metal_pickaxe_leather");
				ResourceLocation loc2=TextureHelper.getTextureFromBlockOfID(nbt.getString("mat_stick_leather"));
				ResourceLocation loc3=new ResourceLocation("skyisles:item/metal_pickaxe_metal");
				ResourceLocation loc4=TextureHelper.getTextureFromBlockOfID(nbt.getString("mat_head_metal1"));
				ResourceLocation loc5=new ResourceLocation("skyisles:item/metal_pickaxe_metal_o");
				ResourceLocation loc6=TextureHelper.getTextureFromBlockOfID(nbt.getString("mat_head_metal2"));
				ResourceLocation loc7=new ResourceLocation("skyisles:item/metal_pickaxe_wood");
				ResourceLocation loc8=TextureHelper.getTextureFromBlockOfID(nbt.getString("mat_stick_wood"));
				models.add(nbt,(Renderer.prepExtrudedTexture(false,
						 new ExtrudedTexture(loc1,loc2,1,true)
						,new ExtrudedTexture(loc3,loc4,0,true)
						,new ExtrudedTexture(loc7,loc8,0,true)
						,new ExtrudedTexture(loc5,loc6,1,true)
				)));
				tints.add(nbt,new int[]{
						TextureHelper.getFirstColor(loc2),
						TextureHelper.getFirstColor(loc4),
						TextureHelper.getFirstColor(loc8),
						TextureHelper.getFirstColor(loc6),
				});
			} else {
				ResourceLocation loc1=new ResourceLocation("skyisles:item/metal_pickaxe_leather");
				ResourceLocation loc2=new ResourceLocation("minecraft:item/leather");
				ResourceLocation loc3=new ResourceLocation("skyisles:item/metal_pickaxe_metal");
				ResourceLocation loc4=new ResourceLocation("minecraft:block/gold_block");
				ResourceLocation loc5=new ResourceLocation("skyisles:item/metal_pickaxe_metal_o");
				ResourceLocation loc6=new ResourceLocation("minecraft:block/diamond_block");
				ResourceLocation loc7=new ResourceLocation("skyisles:item/metal_pickaxe_wood");
				ResourceLocation loc8=new ResourceLocation("minecraft:block/oak_planks");
				models.add(nbt,(Renderer.prepExtrudedTexture(false,
						new ExtrudedTexture(loc1,loc2,1,true)
						,new ExtrudedTexture(loc3,loc4,0,true)
						,new ExtrudedTexture(loc7,loc8,0,true)
						,new ExtrudedTexture(loc5,loc6,1,true)
				)));
				tints.add(nbt,new int[]{
						TextureHelper.getFirstColor(loc2),
						TextureHelper.getFirstColor(loc4),
						TextureHelper.getFirstColor(loc8),
						TextureHelper.getFirstColor(loc6),
				});
			}
		}
		
		matrixStackIn.push();
		matrixStackIn.scale(0.06f,0.06f,0.06f);
		matrixStackIn.translate(16,0,8);
		matrixStackIn.rotate(new Quaternion(0,0,90,true));
		Renderer.renderPreparedModel(models.get(itemStackIn.getOrCreateTag()),bufferIn,matrixStackIn, combinedLightIn, OverlayTexture.NO_OVERLAY,tints.get(itemStackIn.getOrCreateTag()));
		matrixStackIn.pop();
		
		super.render(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}
}
