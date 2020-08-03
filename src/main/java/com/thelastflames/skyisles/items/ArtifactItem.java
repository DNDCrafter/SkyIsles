package com.thelastflames.skyisles.items;

import com.thelastflames.skyisles.items.properties.ArtifactProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ArtifactItem extends Item {
	
	private final Item fuel;
	private final Consumer<PlayerEntity> function;
	
	public ArtifactItem(Item fuel, Consumer<PlayerEntity> function) {
		super(new ArtifactProperties());
		this.fuel = fuel;
		this.function = function;
	}
	
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull PlayerEntity playerIn, @Nonnull Hand handIn) {
		int fuelSlot = playerIn.inventory.getSlotFor(new ItemStack(fuel));
		if (fuelSlot >= 0 && playerIn.inventory.getStackInSlot(fuelSlot).getItem().equals(fuel)) {
			playerIn.getCooldownTracker().setCooldown(this, 20);
			function.accept(playerIn);
			playerIn.addStat(Stats.ITEM_USED.get(this));
			if (!playerIn.abilities.isCreativeMode) {
				playerIn.inventory.getStackInSlot(fuelSlot).shrink(1);
			}
			return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
