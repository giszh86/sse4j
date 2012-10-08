package org.sse.io;

/**
 * @author dux(duxionggis@126.com)
 */
public class Enums {
	public enum QueryType {
		STANDARD, FUZZY, IK
	}

	public enum OccurType {
		AND, OR, NOT
	}

	public enum AnalyzerType {
		STANDARD, SMARTCN, IK
	}
}
