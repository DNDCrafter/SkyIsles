package com.thelastflames.skyisles.utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import java.util.UUID;

public class NBTUtil {
	public static CompoundNBT createNBT(NBTObjectHolder<?>... objects) {
		CompoundNBT nbt=new CompoundNBT();
		for (NBTObjectHolder<?> object:objects) {
			if (object.getObject() instanceof String) {
				nbt.putString(object.key,(String) object.object);
			} else if (object.getObject() instanceof INBT) {
				nbt.put(object.key,(INBT) object.object);
			} else if (object.getObject() instanceof Long) {
				nbt.putLong(object.key,(Long) object.object);
			} else if (object.getObject() instanceof Float) {
				nbt.putFloat(object.key,(Float) object.object);
			} else if (object.getObject() instanceof Double) {
				nbt.putDouble(object.key,(Double) object.object);
			} else if (object.getObject() instanceof Integer) {
				nbt.putInt(object.key,(Integer) object.object);
			} else if (object.getObject() instanceof Boolean) {
				nbt.putBoolean(object.key,(Boolean) object.object);
			} else if (object.getObject() instanceof Byte) {
				nbt.putByte(object.key,(Byte) object.object);
			} else if (object.getObject() instanceof Short) {
				nbt.putShort(object.key,(Short) object.object);
			} else if (object.getObject() instanceof UUID) {
				nbt.putUniqueId(object.key,(UUID) object.object);
			}
		}
		return nbt;
	}
	
	public static class NBTObjectHolder<T> {
		String key;
		T object;
		
		public NBTObjectHolder(String key, T object) {
			this.key = key;
			this.object = object;
		}
		
		public T getObject() {
			return object;
		}
		
		public CompoundNBT Package() {
			return createNBT(this);
		}
	}
}
