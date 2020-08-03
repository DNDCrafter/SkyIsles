package com.thelastflames.skyisles.utils.random;


// From a C program I wrote quickly while on a google meeting
// https://cplayground.com/?p=ferret-ape-rhinoceros
public class Random1 {
	protected static long seed = 0;
	
	public static void setSeed(long newSeed) {
		seed = newSeed;
	}
	
	public static void randomizeSeed() {
		setSeed(getIntFull());
	}
	
	public static long getSeed() {
		return seed;
	}
	
	public static int getIntFast() {
		long input = getSeed() * 30;
		return (int) (((input * input - (input * 2)) / (input ^ 3) * (input ^ 6)) % Integer.MAX_VALUE);
	}
	
	public static int getIntFull() {
		long seedOld = getSeed();
		long output = getIntFast();
		long num2 = output;
		float float1 = 1;
		setSeed(output);
		output = (long) (getIntFast() * (float1 / num2));
		setSeed(num2);
		output += getIntFast();
		setSeed(output);
		output += getIntFast();
		setSeed(output + 3749);
		output += getIntFast();
		setSeed(seedOld);
		return (int) output;
	}
	
	public static int getIntWithLimit(int limit, boolean onlyPositive) {
		int output = getIntFull() % limit;
		return onlyPositive ? Math.abs(output) : output;
	}
	
	public static int getIntWithLimit(int limit) {
		return getIntWithLimit(limit, true);
	}
	
	public static double getDouble(int precision, int divisor) {
		return ((double) getIntWithLimit(precision)) / (double) divisor;
	}
	
	public static double getDouble(int precision) {
		return ((double) getIntWithLimit(precision)) / (double) precision;
	}
	
	public static boolean getBoolean(double chance) {
		return chance == 100 || getIntWithLimit((int) (100 - chance)) == 0;
	}
}
