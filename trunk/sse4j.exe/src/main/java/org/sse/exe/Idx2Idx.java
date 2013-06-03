package org.sse.exe;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.FSDirectory;
import org.sse.idx.IdxWriter;
import org.sse.idx.base.Enums.AnalyzerType;

public class Idx2Idx {

	public static void main(String[] args) throws IOException {
		if (args == null || args.length != 2) {
			System.out.println("Idx2Idx [source lucene path] [target lucene path]");
			return;
		}
		
		File source = new File(args[0]);
		if (!source.exists()) {
			System.out.println("input path is invalid!");
			return;
		}

		System.out.println("------------------start transfer: " + new Date() + "------------------");

		IdxWriter writer = new IdxWriter(args[1], AnalyzerType.CN);
		IndexReader sourceReader = IndexReader.open(FSDirectory.open(source));
		TermDocs sourceDocs = sourceReader.termDocs(null);
		int idx = 0;
		while (sourceDocs.next()) {
			idx++;
			if (idx % 1e6 == 0) {
				System.out.println("current num: " + idx + " time: " + new Date());
			}
			Document tdoc = sourceReader.document(sourceDocs.doc());
			writer.getWriter().addDocument(tdoc);
		}
		sourceDocs.close();

		System.out.println("------------------end transfer: " + new Date() + "------------------");

		writer.close();
	}
}
