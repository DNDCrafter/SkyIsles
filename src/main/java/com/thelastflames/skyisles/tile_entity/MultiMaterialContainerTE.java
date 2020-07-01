package com.thelastflames.skyisles.tile_entity;

import com.thelastflames.skyisles.registry.SkyTileEntities;
import com.thelastflames.skyisles.utils.MaterialList;
import com.thelastflames.skyisles.utils.NonNullListWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.thelastflames.skyisles.blocks.DrawerBlock.*;

public class MultiMaterialContainerTE extends LockableLootTileEntity implements IMultiMaterialTE, INamedContainerProvider {
	public MaterialList materialList=new MaterialList();
	
	NonNullListWrapper<ItemStack> listWrapperTop=new NonNullListWrapper<>(NonNullList.withSize(9,ItemStack.EMPTY));
	NonNullListWrapper<ItemStack> listWrapperBottom=new NonNullListWrapper<>(NonNullList.withSize(9,ItemStack.EMPTY));
	
	public MultiMaterialContainerTE(TileEntityType<?> typeIn) {
		super(typeIn);
	}
	
	public MultiMaterialContainerTE() {
		this(SkyTileEntities.DRAWER_TE.get());
	}
	
	@Override
	public void read(@Nonnull CompoundNBT compound) {
		super.read(compound);
		if (!compound.contains("x")) {
			try {
				this.pos=new BlockPos(0,-9999,0);
			} catch (Exception ignored) {}
		}
		ItemStackHelper.loadAllItems(compound.getCompound("items_top"),listWrapperTop);
		ItemStackHelper.loadAllItems(compound.getCompound("items_bottom"),listWrapperBottom);
		materialList=MaterialList.fromString(compound.getString("materials"));
	}
	
	private int part=0;
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		part=0;
		double distance=999999;
		if (player!=null) {
			Vec3d start=player.getEyePosition(0);
			Vec3d stop=start.add(player.getLookVec().scale(9));
			for (AxisAlignedBB bb:raytraceShapeTop.toBoundingBoxList()) {
				Optional<Vec3d> vec3dOptional=bb.offset(pos).rayTrace(start,stop);
				if (vec3dOptional.isPresent()) {
					if (vec3dOptional.get().distanceTo(start)<distance) {
						distance=vec3dOptional.get().distanceTo(start);
						part=1;
					}
				}
			}
			for (AxisAlignedBB bb:raytraceShapeBottom.toBoundingBoxList()) {
				Optional<Vec3d> vec3dOptional=bb.offset(pos).rayTrace(start,stop);
				if (vec3dOptional.isPresent()) {
					if (vec3dOptional.get().distanceTo(start)<distance) {
						distance=vec3dOptional.get().distanceTo(start);
						part=2;
					}
				}
			}
			for (AxisAlignedBB bb:raytraceShape.toBoundingBoxList()) {
				Optional<Vec3d> vec3dOptional=bb.offset(pos).rayTrace(start,stop);
				if (vec3dOptional.isPresent()) {
					if (vec3dOptional.get().distanceTo(start)<distance) {
						distance=vec3dOptional.get().distanceTo(start);
						part=0;
					}
				}
			}
		}
		
		if (!worldIn.isRemote&&part!=0) {
			INamedContainerProvider inamedcontainerprovider = this;
			player.openContainer(inamedcontainerprovider);
			player.addStat(this.getOpenStat());
		}
		return ActionResultType.SUCCESS;
	}
	
	protected Stat<ResourceLocation> getOpenStat() {
		return Stats.CUSTOM.get(com.thelastflames.skyisles.other.Stats.OPEN_DRAWER);
	}
	
	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound) {
		super.write(compound);
		compound.putString("materials",materialList.toString());
		CompoundNBT top=new CompoundNBT();
		CompoundNBT bottom=new CompoundNBT();
		ItemStackHelper.saveAllItems(top,listWrapperTop);
		ItemStackHelper.saveAllItems(bottom,listWrapperBottom);
		compound.put("items_top",top);
		compound.put("items_bottom",bottom);
		return compound;
	}
	
	public void onRemove() {
		part=1;
		InventoryHelper.dropInventoryItems(world, pos, new Inventory(listWrapperTop));
		part=2;
		InventoryHelper.dropInventoryItems(world, pos, new Inventory(listWrapperBottom));
		world.updateComparatorOutputLevel(pos, this.getBlockState().getBlock());
	}
	
	private net.minecraftforge.common.util.LazyOptional<net.minecraftforge.items.IItemHandlerModifiable> chestHandler;
	
	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, Direction side) {
		if (!this.removed && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (this.chestHandler == null)
				this.chestHandler = net.minecraftforge.common.util.LazyOptional.of(this::createHandler);
			return this.chestHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	private net.minecraftforge.items.IItemHandlerModifiable createHandler() {
//		BlockState state = this.getBlockState();
//		if (!(state.getBlock() instanceof ChestBlock)) {
//			return new net.minecraftforge.items.wrapper.InvWrapper(this);
//		}
//		ChestType type = state.get(ChestBlock.TYPE);
//		if (type != ChestType.SINGLE) {
//			BlockPos opos = this.getPos().offset(ChestBlock.getDirectionToAttached(state));
//			BlockState ostate = this.getWorld().getBlockState(opos);
//			if (state.getBlock() == ostate.getBlock()) {
//				ChestType otype = ostate.get(ChestBlock.TYPE);
//				if (otype != ChestType.SINGLE && type != otype && state.get(ChestBlock.FACING) == ostate.get(ChestBlock.FACING)) {
//					TileEntity ote = this.getWorld().getTileEntity(opos);
//					if (ote instanceof ChestTileEntity) {
//						IInventory top    = type == ChestType.RIGHT ? this : (IInventory)ote;
//						IInventory bottom = type == ChestType.RIGHT ? (IInventory)ote : this;
//						return new net.minecraftforge.items.wrapper.CombinedInvWrapper(
//								new net.minecraftforge.items.wrapper.InvWrapper(top),
//								new net.minecraftforge.items.wrapper.InvWrapper(bottom));
//					}
//				}
//			}
//		}
		return new net.minecraftforge.items.wrapper.InvWrapper(this);
	}
	
	@Override
	public MaterialList getMaterialList() {
		return materialList;
	}
	
	@Nonnull
	@Override
	protected NonNullList<ItemStack> getItems() {
		return listWrapperTop;
	}
	
	@Override
	public void read(CompoundNBT nbt, ItemStack stack, BlockState state) {
		read(nbt);
	}
	
	@Override
	protected void setItems(@Nonnull NonNullList<ItemStack> itemsIn) {
	}
	
	@Nonnull
	@Override
	protected ITextComponent getDefaultName() {
		return new StringTextComponent("Drawer");
	}
	
	@Nonnull
	@Override
	protected Container createMenu(int id, @Nonnull PlayerInventory player) {
		return new ChestContainer(ContainerType.GENERIC_9X1,id,player,new Inventory((part==2)?listWrapperBottom:listWrapperTop),listWrapperTop.size()/9);
	}
	
	@Override
	public int getSizeInventory() {
		return 0;
	}
}
