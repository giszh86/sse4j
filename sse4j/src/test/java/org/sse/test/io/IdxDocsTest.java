package org.sse.test.io;

import java.io.IOException;

import org.apache.lucene.index.TermDocs;
import org.sse.io.IdxReader;

public class IdxDocsTest {

	public static void main(String[] args) throws IOException {
		IdxReader reader = new IdxReader(
				"data/idx/110000/Poi,data/idx/110000/Poi");

		// TODO
		TermDocs docs = reader.getReader(0).termDocs();
		while (docs.next()) {
			reader.getReader(0).document(docs.doc());
		}
		docs.close();

		System.out.println("Num:" + reader.getReader(0).numDocs() + " max:"
				+ reader.getReader(0).maxDoc());
	}

}
