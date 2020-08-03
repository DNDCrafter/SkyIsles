package com.thelastflames.skyisles.utils;

import net.minecraft.block.Blocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;

import java.util.HashMap;

public class StatsHelper {
	private static final HashMap<Item, ToolStats> statsHashMap = new HashMap<>();
	
	public static ToolStats get(Item item) {
		if (statsHashMap.containsKey(item)) {
			return statsHashMap.get(item);
		} else {
			if (item instanceof PickaxeItem) {
				double damage = 0;
				double damageBase = -998234032;
				for (AttributeModifier attributeModifier : item.getAttributeModifiers(EquipmentSlotType.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
					if (damageBase == -998234032) {
						damageBase = attributeModifier.getAmount();
					}
					if (attributeModifier.getOperation().equals(AttributeModifier.Operation.ADDITION)) {
						damage += attributeModifier.getAmount();
					} else if (attributeModifier.getOperation().equals(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
						damage *= attributeModifier.getAmount();
					} else if (attributeModifier.getOperation().equals(AttributeModifier.Operation.MULTIPLY_BASE)) {
						damage += (damageBase * attributeModifier.getAmount()) - damageBase;
					}
				}
				return new ToolStats(item.getMaxDamage(), damage, item.getDestroySpeed(new ItemStack(item), Blocks.STONE.getDefaultState()), item.getUseDuration(new ItemStack(item)));
			}
		}
		return null;
	}
	
	public void setup() {
		statsHashMap.put(Items.QUARTZ, new ToolStats(32, 8, 0, 3));
	}
	
	public static class ToolStats {
		public final int durability;
		public final double attack;
		public final double miningSpeed;
		public final int attackSpeed;
		
		public ToolStats(int durability, double attack, double miningSpeed, int attackSpeed) {
			this.durability = durability;
			this.attack = attack;
			this.miningSpeed = miningSpeed;
			this.attackSpeed = attackSpeed;
		}
	}
}
