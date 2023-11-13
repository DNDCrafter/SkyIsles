package com.thelastflames.skyisles.block_entity;

import com.thelastflames.skyisles.registry.SkyTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

//literally just a copy of vanilla
public class ChestTE extends RandomizableContainerBlockEntity implements LidBlockEntity {
    private static final int EVENT_SET_OPEN_COUNT = 1;
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level p_155357_, BlockPos p_155358_, BlockState p_155359_) {
            ChestTE.playSound(p_155357_, p_155358_, p_155359_, SoundEvents.CHEST_OPEN);
        }

        protected void onClose(Level p_155367_, BlockPos p_155368_, BlockState p_155369_) {
            ChestTE.playSound(p_155367_, p_155368_, p_155369_, SoundEvents.CHEST_CLOSE);
        }

        protected void openerCountChanged(Level p_155361_, BlockPos p_155362_, BlockState p_155363_, int p_155364_, int p_155365_) {
            ChestTE.this.signalOpenCount(p_155361_, p_155362_, p_155363_, p_155364_, p_155365_);
        }

        protected boolean isOwnContainer(Player p_155355_) {
            if (!(p_155355_.containerMenu instanceof ChestMenu)) {
                return false;
            } else {
                Container container = ((ChestMenu) p_155355_.containerMenu).getContainer();
                return container == ChestTE.this || container instanceof CompoundContainer && ((CompoundContainer) container).contains(ChestTE.this);
            }
        }
    };
    private final ChestLidController chestLidController = new ChestLidController();

    public ChestTE(BlockPos p_155331_, BlockState p_155332_) {
        this(SkyTileEntities.CHEST_TE.get(), p_155331_, p_155332_);
    }

    public ChestTE(BlockEntityType<?> type, BlockPos p_155331_, BlockState p_155332_) {
        super(type, p_155331_, p_155332_);
    }

    public int getContainerSize() {
        return 27;
    }

    protected Component getDefaultName() {
        return Component.translatable("container.chest");
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }

    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }

    }

    public static void lidAnimateTick(Level p_155344_, BlockPos p_155345_, BlockState p_155346_, ChestTE p_155347_) {
        p_155347_.chestLidController.tickLid();
    }

    static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent event) {
        ChestType chesttype = state.getValue(ChestBlock.TYPE);
        if (chesttype != ChestType.LEFT) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.5D;
            double d2 = (double) pos.getZ() + 0.5D;
            if (chesttype == ChestType.RIGHT) {
                Direction direction = ChestBlock.getConnectedDirection(state);
                d0 += (double) direction.getStepX() * 0.5D;
                d2 += (double) direction.getStepZ() * 0.5D;
            }

            level.playSound(null, d0, d1, d2, event, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    public boolean triggerEvent(int event, int data) {
        if (event == EVENT_SET_OPEN_COUNT) {
            this.chestLidController.shouldBeOpen(data > 0);
            return true;
        } else {
            return super.triggerEvent(event, data);
        }
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public float getOpenNess(float pct) {
        return this.chestLidController.getOpenness(pct);
    }

    public static int getOpenCount(BlockGetter world, BlockPos pos) {
        BlockState blockstate = world.getBlockState(pos);
        if (blockstate.hasBlockEntity()) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof ChestTE) {
                return ((ChestTE) blockentity).openersCounter.getOpenerCount();
            }
        }

        return 0;
    }

    public static void swapContents(ChestTE chest0, ChestTE chest1) {
        NonNullList<ItemStack> nonnulllist = chest0.getItems();
        chest0.setItems(chest1.getItems());
        chest1.setItems(nonnulllist);
    }

    protected AbstractContainerMenu createMenu(int p_59082_, Inventory p_59083_) {
        return ChestMenu.threeRows(p_59082_, p_59083_, this);
    }

    private LazyOptional<IItemHandlerModifiable> chestHandler;

    @Override
    public void setBlockState(BlockState state) {
        super.setBlockState(state);
        if (this.chestHandler != null) {
            LazyOptional<?> oldHandler = this.chestHandler;
            this.chestHandler = null;
            oldHandler.invalidate();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (this.chestHandler == null)
                this.chestHandler = LazyOptional.of(this::createHandler);
            return this.chestHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler() {
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof ChestBlock)) {
            return new InvWrapper(this);
        }
        Container inv = ChestBlock.getContainer((ChestBlock) state.getBlock(), state, getLevel(), getBlockPos(), true);
        return new InvWrapper(inv == null ? this : inv);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (chestHandler != null) {
            chestHandler.invalidate();
            chestHandler = null;
        }
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int p_155336_, int count) {
        Block block = state.getBlock();
        level.blockEvent(pos, block, EVENT_SET_OPEN_COUNT, count);
    }
}
