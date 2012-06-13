package org.sse.io;

/**
 * 
 * @author dux(duxionggis@126.com)
 * 
 */
public class Enums {
	public enum QueryType {
		Standard, Fuzzy, IK
	}

	public enum OccurType {
		And, Or, Not
	}

	public enum AnalyzerType {
		Standard, SmartCN, IK
	}
}
