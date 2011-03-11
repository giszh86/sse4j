package org.sse.test;

import java.util.ArrayList;
import java.util.List;

public class MemeryTest {

	public static void main(String[] args) {
		int size = 100000;

		Runtime.getRuntime().gc();
		long t1 = System.currentTimeMillis();
		long s1 = Runtime.getRuntime().freeMemory();
		int[] m1 = new int[size];
		for (int i = 0; i < size; i++) {
			m1[i] = i;
		}
		System.out.println("mem:" + (Runtime.getRuntime().freeMemory() - s1));
		System.out.println("time:" + (System.currentTimeMillis() - t1));

		System.gc();
		t1 = System.currentTimeMillis();
		s1 = Runtime.getRuntime().freeMemory();
		List<Integer> m2 = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			m2.add(i);
		}
		System.out.println("mem:" + (Runtime.getRuntime().freeMemory() - s1));
		System.out.println("time:" + (System.currentTimeMillis() - t1));

	}

}
