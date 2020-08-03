package com.thelastflames.skyisles.utils;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class StringyHashMap<T,H> {
	public ArrayList<T> keys = new ArrayList<>();
	public ArrayList<H> objects = new ArrayList<>();
	
	public StringyHashMap() {
	}
	
	public boolean containsKey(T key) {
		boolean check1 = keys.contains(key);
		if (!check1) {
			for (T key1 : keys) {
				if (key1.toString().equals(key.toString())) {
					return true;
				}
			}
		}
		return check1;
	}
	
	public H get(T key) {
		if (keys.contains(key)) {
			return objects.get(keys.indexOf(key));
		}
		for (int i = 0; i < keys.size(); i++) {
			T key1 = keys.get(i);
			if (key1.toString().equals(key.toString())) {
				return objects.get(i);
			}
		}
		return null;
	}
	
	public void remove(T key) {
		int i = 0;
		try {
			for (T key1 : keys) {
				if (key1.toString().equals(key.toString())) {
					objects.remove(i);
					keys.remove(i);
					return;
				}
				i++;
			}
		} catch (Exception ignored) {
		}
	}
	
	public void add(T key, H object) {
		keys.add(key);
		objects.add(object);
	}
	
	public void forEach(BiConsumer<T, H> consumer) {
		for (int i = 0; i < keys.size(); i++) {
			consumer.accept(keys.get(i), objects.get(i));
		}
	}
}
