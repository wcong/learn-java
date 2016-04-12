package org.wcong.test.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.nio.file.Paths;

public class LuceneTest {

	public static void main(String[] args) throws Exception {
		Analyzer analyzer = new StandardAnalyzer();

		// Store the index in memory:
		//		Directory directory = new RAMDirectory();
		// To store an index on disk, use this instead:
		Directory directory = FSDirectory.open(Paths.get("./testIndex"));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		Document doc = new Document();
		String text = "哈哈 哈哈.";
		doc.add(new Field("id", "1", TextField.TYPE_STORED));
		doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
		doc.add(new Field("fieldname2", text, TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		byte[] id = {0,0,0,1};
//		iwriter.deleteDocuments(new Term("id", "1"));
		iwriter.commit();
		iwriter.maybeMerge();
		iwriter.close();

		// Now search the index:
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser("fieldname", analyzer);
		Query query = parser.parse("哈哈");
		ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;
		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			System.out.println(hitDoc.getField("fieldname").stringValue());
		}
		ireader.close();
		directory.close();
	}

}
