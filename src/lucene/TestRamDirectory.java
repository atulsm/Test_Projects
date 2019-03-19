package lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class TestRamDirectory {

	public static void main(String[] args) throws Exception {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		//index
		IndexWriter w = new IndexWriter(index, config);
		addDoc(w, "atulsm", "RC007, pruksa silvana");
		addDoc(w, "maneesh", "RC008, pruksa silvana");
		addDoc(w, "shajan", "RC009, pruksa silvana");

		addDoc(w, "anjanap", "F5, Chennu Homes");
		w.close();

		//query
		String querystr = args.length > 0 ? args[0] : "rc0*";
		Query q = new QueryParser("address", analyzer).parse(querystr);
		
		//search
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;
		
		//display results
		System.out.println("Found " + hits.length + " hits.");
		for(int i=0;i<hits.length;++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    System.out.println((i + 1) + ". " + d.get("address") + "\t" + d.get("name"));
		}
	}

	private static void addDoc(IndexWriter w, String name, String address) throws IOException {
		Document doc = new Document();
		doc.add(new StringField("name", name, Field.Store.YES));  //StringField: Dont tokenize
		doc.add(new TextField("address", address, Field.Store.YES)); //TextField: Tokenize
		w.addDocument(doc);
	}
}
