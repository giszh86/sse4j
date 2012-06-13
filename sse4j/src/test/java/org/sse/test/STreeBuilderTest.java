package org.sse.test;

import java.util.Date;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

public class STreeBuilderTest {

	public static void main(String[] args) throws Exception {
		// 千万数据级
		Envelope env = new Envelope(10, 1000, 5, 500);

		Date date1 = new Date();
		STRtree tree1 = new STRtree();
		for (int i = 1; i <= 10000; i++)
			for (int j = 1; j <= 1000; j++) {
				tree1.insert(new Envelope(i, i + 1, j, j + 1), i * 10000 + j);
			}
		tree1.build();
		System.out.println("rtree build:"
				+ ((new Date()).getTime() - date1.getTime()));
		date1 = new Date();
		System.out.println(tree1.query(env).size());
		System.out.println("rtree query:"
				+ ((new Date()).getTime() - date1.getTime()));

		// tree1 = null;
		// Quadtree tree2 = new Quadtree();
		// for (int i = 1; i <= 1000; i++)
		// for (int j = 1; j <= 1000; j++) {
		// tree2.insert(new Envelope(i, i + 1, j, j + 1), i * 1000 + j);
		// }
		// System.out.println("qtree build:"
		// + ((new Date()).getTime() - date1.getTime()));
		// date1 = new Date();
		// System.out.println(tree2.query(env).size());
		// System.out.println("qtree query:"
		// + ((new Date()).getTime() - date1.getTime()));

		// date1 = new Date();
		// KdTree tree3 = new KdTree();
		// for (int i = 1; i <= 1000; i++)
		// for (int j = 1; j <= 1000; j++) {
		// tree3.insert(new Coordinate(i, j), i * 10000 + j);
		// }
		// System.out.println("kdtree build:"
		// + ((new Date()).getTime() - date1.getTime()));
		// date1 = new Date();
		// System.out.println(tree3.query(env).size());
		// System.out.println("kdtree query:"
		// + ((new Date()).getTime() - date1.getTime()));

	}
}
