package lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class TestLuceneIndexThenSearch {

	private static final String AUTHOR = "author";
	private static final String FIRST_NAME = "atul";
	private static final String LAST_NAME = "soman";
	private static final String FULL_NAME = FIRST_NAME + ' ' + LAST_NAME;
	
	public static void main(String args[]) throws ParseException, IOException{
		MemoryIndex index = new MemoryIndex();
		Analyzer analyzer = new StandardAnalyzer();
		StringField field3 = new StringField(AUTHOR, FULL_NAME, Store.YES);
		index.addField(field3, analyzer);
		
		Query query = new TermQuery(new Term(AUTHOR,FULL_NAME));
		search(index,query);
		
		query = new TermQuery(new Term(AUTHOR,FIRST_NAME));
		search(index,query);
		
		query = new TermQuery(new Term(AUTHOR,LAST_NAME));
		search(index,query);	
	}
			
	protected static void search(MemoryIndex index, Query query) {
		float score = index.search(query);
		if (score > 0.0f) {
			System.out.println("it's a match for " + query);
		} else {
			System.out.println("no match found for " + query);
		}
	}
}
