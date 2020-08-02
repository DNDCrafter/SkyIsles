package com.thelastflames.skyisles.client.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.thelastflames.skyisles.SkyIsles;
import com.thelastflames.skyisles.tile_entity.SkyboxTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Random;

public class SkyboxRenderer extends TileEntityRenderer<SkyboxTileEntity> {
	private static final Random RANDOM = new Random(31100L);
//	private static final List<RenderType> RENDER_TYPES = IntStream.range(0, 16).mapToObj((p_228882_0_) -> {
//		return RenderType.getEndPortal(p_228882_0_ + 1);
//	}).collect(ImmutableList.toImmutableList());
	
	private static final TexturingStateLookup lookup = new TexturingStateLookup();
	
	private static SkyboxRenderer INSTANCE;
	public SkyboxRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		INSTANCE=this;
	}
	
	public static SkyboxRenderer getInstance() {
		return INSTANCE;
	}
	
	public void render(SkyboxTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		RANDOM.setSeed(31100L);
		double d0 = tileEntityIn.getPos().distanceSq(this.renderDispatcher.renderInfo.getProjectedView(), true);
		int i = getPasses(d0);
		Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();

		//Draw BG
		this.renderCube(tileEntityIn, 1, 0.15F, matrix4f, bufferIn.getBuffer(getSkybox(0,tileEntityIn)));
		
//		//Draw Clouds
		for(int j = 1; j < i; ++j) {
			this.renderCube(tileEntityIn, 0, 2.0F / (float)(18 - j), matrix4f, bufferIn.getBuffer(getSkybox(j + 1,tileEntityIn)));
		}
		
		i = (13-getPasses(d0))*4;
		
		//Draw Stars
		for(int j = 1; j < Math.abs(i); ++j) {
			this.renderCube(tileEntityIn, 2, 2.0F / (float)(18 + j), matrix4f, bufferIn.getBuffer(getSkybox((-j - 1), tileEntityIn)));
		}
		SkyIsles.endBFPSGraphSection();
	}
	
	private void renderCube(SkyboxTileEntity tileEntityIn, float p_228883_2_, float p_228883_3_, Matrix4f matrix, IVertexBuilder bufferIn) {
		SkyIsles.createBFPSGraphSection("skyisles:Collect Skybox Cube Data", 1d,1d,0);
		float r;
		float g;
		float b;
		float a = 1;
		if (p_228883_2_ == 1) {
			r = 1;
			g = 1;
			b = 1;
		} else if (p_228883_2_ == 0) {
			float f4 = ((RANDOM.nextFloat() * 0.5F + 0.1F) * p_228883_3_) / 2;
			r = f4;
			g = f4;
			b = f4;
		} else {
			r = (RANDOM.nextFloat() * 0.5F + 0.1F) * p_228883_3_;
			g = (RANDOM.nextFloat() * 0.5F + 0.4F) * p_228883_3_;
			b = (RANDOM.nextFloat() * 0.5F + 0.5F) * p_228883_3_;
		}
		this.renderFace(tileEntityIn, matrix, bufferIn, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, r, g, b, a, Direction.SOUTH);
		this.renderFace(tileEntityIn, matrix, bufferIn, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, r, g, b, a, Direction.NORTH);
		this.renderFace(tileEntityIn, matrix, bufferIn, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, r, g, b, a, Direction.EAST);
		this.renderFace(tileEntityIn, matrix, bufferIn, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, r, g, b, a, Direction.WEST);
		this.renderFace(tileEntityIn, matrix, bufferIn, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, r, g, b, a, Direction.DOWN);
		this.renderFace(tileEntityIn, matrix, bufferIn, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, r, g, b, a, Direction.UP);
	}
	
	private void renderFace(SkyboxTileEntity tileEntityIn, Matrix4f p_228884_2_, IVertexBuilder p_228884_3_, float p_228884_4_, float p_228884_5_, float p_228884_6_, float p_228884_7_, float p_228884_8_, float p_228884_9_, float p_228884_10_, float p_228884_11_, float r, float g, float b, float a, Direction p_228884_15_) {
		SkyIsles.createBFPSGraphSection("skyisles:Render Skybox Cube Face", 1d,0,0);
		if (tileEntityIn.shouldRenderFace(p_228884_15_, tileEntityIn.getWorld(), tileEntityIn.getPos())) {
			p_228884_3_.pos(p_228884_2_, p_228884_4_, p_228884_6_, p_228884_8_).color(r, g, b, a).endVertex();
			p_228884_3_.pos(p_228884_2_, p_228884_5_, p_228884_6_, p_228884_9_).color(r, g, b, a).endVertex();
			p_228884_3_.pos(p_228884_2_, p_228884_5_, p_228884_7_, p_228884_10_).color(r, g, b, a).endVertex();
			p_228884_3_.pos(p_228884_2_, p_228884_4_, p_228884_7_, p_228884_11_).color(r, g, b, a).endVertex();
		}
	}
	
	protected int getPasses(double p_191286_1_) {
		SkyIsles.createBFPSGraphSection("skyisles:Get Passes", 0.5,0.5,0.5);
		if (p_191286_1_ > 36864.0D) {
			return 1;
		} else if (p_191286_1_ > 16384.0D) {
			return p_191286_1_ > 25600.0D ? 3 : 5;
		} else if (p_191286_1_ > 4096.0D) {
			return p_191286_1_ > 9216.0D ? 7 : 9;
		} else if (p_191286_1_ > 576.0D) {
			return p_191286_1_ > 1024.0D ? 11 : 13;
		} else {
			return p_191286_1_ > 256.0D ? 14 : 15;
		}
	}
	
	private static final RenderState.TexturingState stateBase = new RenderState.OffsetTexturingState(0,0);
	
	public static RenderType getSkybox(int iterationIn, SkyboxTileEntity tileEntity) {
		SkyIsles.createBFPSGraphSection("skyisles:Get Skybox Render State", 0,0,1d);
		RenderState.TransparencyState renderstate$transparencystate;
		RenderState.TextureState renderstate$texturestate;
		RenderState.TexturingState renderstate$texturing;
		if (iterationIn == 0) {
			SkyIsles.createBFPSGraphSection("skyisles:Get Skybox Render State Base", 1d,0d,1d);
			renderstate$transparencystate = TRANSLUCENT_TRANSPARENCY;
			renderstate$texturestate = new RenderState.TextureState(tileEntity.getSkyTexture(tileEntity.getBlockState().getBlock()), false, false);
			renderstate$texturing = stateBase;
		} else if (iterationIn <= -1) {
			SkyIsles.createBFPSGraphSection("skyisles:Get Skybox Render State Stars", 1d,0.5,1d);
			renderstate$transparencystate = ADDITIVE_TRANSPARENCY;
			renderstate$texturestate = new RenderState.TextureState(tileEntity.getStarsPassTexture(tileEntity.getBlockState().getBlock()), false, false);
			renderstate$texturing = lookup.getAndSetState(iterationIn,false);
		} else {
			SkyIsles.createBFPSGraphSection("skyisles:Get Skybox Render State Clouds", 1d,1d,1d);
			renderstate$transparencystate = ADDITIVE_TRANSPARENCY;
			renderstate$texturestate = new RenderState.TextureState(tileEntity.getPassTexture(tileEntity.getBlockState().getBlock()), false, false);
			renderstate$texturing = lookup.getAndSetState(iterationIn,true);
		}
		SkyIsles.endBFPSGraphSection();
		
		return RenderType.makeType("skybox", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.getBuilder().transparency(renderstate$transparencystate).texture(renderstate$texturestate).texturing(renderstate$texturing).fog(FOG).build(false));
	}
	
	protected static final RenderState.TransparencyState ADDITIVE_TRANSPARENCY = new RenderState.TransparencyState("additive_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	
	protected static final RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = new RenderState.TransparencyState("translucent_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
	}, RenderSystem::disableBlend);
	
	protected static final RenderState.FogState FOG = new RenderState.FogState("fog", () -> {
		FogRenderer.applyFog();
		RenderSystem.enableFog();
	}, RenderSystem::disableFog);
	
	private static class TexturingStateLookup {
		private final HashMap<Integer, RenderState.TexturingState> map = new HashMap<>();
		
		public RenderState.TexturingState getAndSetState(Integer iteration, boolean clouds) {
			if (!map.containsKey(iteration)) {
				if (clouds) {
					map.put(iteration, new SkyboxTexturingStateClouds(iteration));
				} else {
					map.put(iteration, new SkyboxTexturingStateStars(iteration));
				}
			}
			return map.get(iteration);
			
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static final class SkyboxTexturingStateClouds extends RenderState.TexturingState {
		private final int iteration;
		
		public SkyboxTexturingStateClouds(int p_i225986_1_) {
			super("skybox_texturing_clouds", () -> SkyboxTexturingStateClouds.pre(p_i225986_1_),
			() -> {
				RenderSystem.matrixMode(5890);
				RenderSystem.popMatrix();
				RenderSystem.matrixMode(5888);
				RenderSystem.clearTexGen();
			});
			this.iteration = p_i225986_1_;
		}
		
		private static void pre(int p_i225986_1_) {
			SkyIsles.createBFPSGraphSection("skyisles:Texturing State Clouds", 0.25,0.1,0.75);
			RenderSystem.matrixMode(5890);
			RenderSystem.pushMatrix();
			RenderSystem.loadIdentity();
			RenderSystem.translatef(0.5F, 0.5F, 0.0F);
			RenderSystem.scalef(0.5F, 0.5F, 1.0F);
			RenderSystem.translatef((17.0F / (float)p_i225986_1_) + ((float)(Util.milliTime()) / 4000.0F), (2.0F + (float)p_i225986_1_ / 1.5F) * ((float)(Util.milliTime()) / 800000.0F), 0.0F);
			RenderSystem.rotatef(((float)(p_i225986_1_ * p_i225986_1_) * 43.0F + (float)p_i225986_1_ * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
			RenderSystem.scalef(4.5F - (float)p_i225986_1_ / 4.0F, 4.5F - (float)p_i225986_1_ / 4.0F, 1.0F);
			RenderSystem.scalef(1, -1, 0);
			RenderSystem.translated(getPosX()/16f, getPosY()/16f, getPosZ()/16f);
			assert Minecraft.getInstance().player != null;
			RenderSystem.rotatef((180-Minecraft.getInstance().player.rotationYaw)/8f, 0, -1, 0);
			RenderSystem.rotatef(Minecraft.getInstance().player.rotationPitch/8f, 1, 0, 0);
			RenderSystem.translatef(-Minecraft.getInstance().player.rotationYaw/1024f, -Minecraft.getInstance().player.rotationPitch/1024f, 0);
//			RenderSystem.scalef((1f/(Math.abs((Minecraft.getInstance().player.rotationYaw)%360)+90)/180f)*2560f, -1, 0);
			RenderSystem.mulTextureByProjModelView();
			RenderSystem.matrixMode(5888);
			RenderSystem.setupEndPortalTexGen();
		}
		
		public boolean equals(Object p_equals_1_) {
			if (this == p_equals_1_) {
				return true;
			} else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
				SkyboxTexturingStateClouds renderstate$portaltexturingstate = (SkyboxTexturingStateClouds)p_equals_1_;
				return this.iteration == renderstate$portaltexturingstate.iteration;
			} else {
				return false;
			}
		}
		
		public int hashCode() {
			return Integer.hashCode(this.iteration);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static final class SkyboxTexturingStateStars extends RenderState.TexturingState {
		private final int iteration;
		
		public SkyboxTexturingStateStars(int p_i225986_1_) {
			super("skybox_texturing_stars", () -> SkyboxTexturingStateStars.pre(p_i225986_1_),
			() -> {
				RenderSystem.matrixMode(5890);
				RenderSystem.popMatrix();
				RenderSystem.matrixMode(5888);
				RenderSystem.clearTexGen();
			});
			this.iteration = p_i225986_1_;
		}
		
		private static void pre(int p_i225986_1_) {
			SkyIsles.createBFPSGraphSection("skyisles:Texturing State Stars", 0.1,0.25,0.75);
			RenderSystem.matrixMode(5890);
			RenderSystem.pushMatrix();
			RenderSystem.loadIdentity();
			RenderSystem.translatef(0.5F, 0.5F, 0.0F);
			RenderSystem.scalef(0.5F, 0.5F, 1.0F);
			RenderSystem.translatef((17.0F / (float)p_i225986_1_) + ((float)(Util.milliTime() % 400000L) / 400000.0F), (2.0F + (float)p_i225986_1_ / 1.5F) * ((float)(Util.milliTime() % 80000L) / 80000.0F), 0.0F);
			RenderSystem.rotatef(((float)(p_i225986_1_ * p_i225986_1_) * 4334.0F + (float)p_i225986_1_ * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
			RenderSystem.scalef(4.5F - (float)p_i225986_1_ / 4.0F, 4.5F - (float)p_i225986_1_ / 4.0F, 1.0F);
			RenderSystem.scalef(1, -1, 0);
			RenderSystem.translated(getPosX()/16f, getPosY()/16f, getPosZ()/16f);
			assert Minecraft.getInstance().player != null;
			RenderSystem.rotatef((180-Minecraft.getInstance().player.rotationYaw)/8f, 0, -1, 0);
			RenderSystem.rotatef(Minecraft.getInstance().player.rotationPitch/8f, 1, 0, 0);
			RenderSystem.translatef(-Minecraft.getInstance().player.rotationYaw/1024f, -Minecraft.getInstance().player.rotationPitch/1024f, 0);
//			RenderSystem.scalef((1f/(Math.abs((Minecraft.getInstance().player.rotationYaw)%360)+90)/180f)*2560f, -1, 0);
			RenderSystem.mulTextureByProjModelView();
			RenderSystem.matrixMode(5888);
			RenderSystem.setupEndPortalTexGen();
		}
		
		public boolean equals(Object p_equals_1_) {
			if (this == p_equals_1_) {
				return true;
			} else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
				SkyboxTexturingStateStars renderstate$portaltexturingstate = (SkyboxTexturingStateStars)p_equals_1_;
				return this.iteration == renderstate$portaltexturingstate.iteration;
			} else {
				return false;
			}
		}
		
		public int hashCode() {
			return Integer.hashCode(this.iteration);
		}
	}
	
	private static double getPosX() {
		assert Minecraft.getInstance().player != null;
		return Minecraft.getInstance().player.getPosX();
	}
	
	private static double getPosY() {
		assert Minecraft.getInstance().player != null;
		return Minecraft.getInstance().player.getPosY();
	}
	
	private static double getPosZ() {
		assert Minecraft.getInstance().player != null;
		return Minecraft.getInstance().player.getPosZ();
	}
}
