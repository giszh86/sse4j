package org.sse.test.io;

import java.io.IOException;

import org.apache.lucene.index.TermDocs;
import org.sse.io.IdxReader;

public class IdxDocsTest {

	public static void main(String[] args) throws IOException {
		IdxReader reader = new IdxReader(
				"data/idx/110000/Poi,data/idx/110000/Poi");

		for (int i = 0; i < reader.getReader().numDocs(); i++) {
			System.out.println(reader.getReader().document(i));
		}

		// TODO
		TermDocs docs = reader.getReader().termDocs();
		while (docs.next()) {
			reader.getReader().document(docs.doc());
		}
		docs.close();

		System.out.println("Num:" + reader.getReader().numDocs() + " max:"
				+ reader.getReader().maxDoc());
	}

}
