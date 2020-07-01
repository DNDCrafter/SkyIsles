package com.thelastflames.skyisles.utils;

import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class NonNullListWrapper<T> extends NonNullList<T> {
	private final NonNullList<T> thisList;
	public NonNullListWrapper(NonNullList<T> list) {
		thisList=list;
	}
	
	@Nonnull
	@Override
	public T get(int p_get_1_) {
		return thisList.get(p_get_1_);
	}
	
	@Override
	public T set(int p_set_1_, T p_set_2_) {
		return thisList.set(p_set_1_,p_set_2_);
	}
	
	@Override
	public void add(int p_add_1_, T p_add_2_) {
		thisList.add(p_add_1_,p_add_2_);
	}
	
	@Override
	public T remove(int p_remove_1_) {
		return thisList.remove(p_remove_1_);
	}
	
	@Override
	public int size() {
		return thisList.size();
	}
	
	@Override
	public void clear() {
		thisList.clear();
	}
}
