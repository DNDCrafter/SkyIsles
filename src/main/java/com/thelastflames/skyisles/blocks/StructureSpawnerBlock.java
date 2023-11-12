//package com.thelastflames.skyisles.blocks;
//
//import com.thelastflames.skyisles.island_structures.Structure;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.material.MaterialColor;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.TickPriority;
//import net.minecraft.world.World;
//import net.minecraft.world.server.ServerWorld;
//
//import java.util.Random;
//
//public class StructureSpawnerBlock extends Block {
//	private final Structure structure;
//
//	public StructureSpawnerBlock(Structure structure) {
//		super(Properties.create(Material.AIR, MaterialColor.AIR).doesNotBlockMovement().noDrops().notSolid());
//		this.structure = structure;
//	}
//
//	@Override
//	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
//		super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
//		worldIn.getPendingBlockTicks().scheduleTick(pos, this, 1, TickPriority.EXTREMELY_HIGH);
//	}
//
//	@Override
//	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
//		super.tick(state, worldIn, pos, rand);
//		PlayerEntity playerEntity = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ());
//		if (playerEntity != null) {
//			if (playerEntity.getPositionVec().squareDistanceTo(new Vec3d(pos)) <= (2048 * 32)) {
//				this.structure.generate(pos.getX(), pos.getY(), pos.getZ(), worldIn);
//			}
//		}
//		worldIn.getPendingBlockTicks().scheduleTick(pos, this, 32, TickPriority.EXTREMELY_HIGH);
//	}
//}
