package com.thelastflames.skyisles.utils.client;

import com.tfc.better_fps_graph.API.Profiler;

public class BetterFPSGraphWrapper {
	public static void createSection(String name, double r, double g, double b) {
		Profiler.addSection(name, r, g, b);
	}
	
	public static void createSection(String name) {
		Profiler.addSection(name);
	}
	
	public static void endSection() {
		Profiler.endSection();
	}
}
