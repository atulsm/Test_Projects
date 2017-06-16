import java.io.IOException;
import java.text.MessageFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import esecurity.ccs.comp.event.indexedlog.LowercaseKeywordAnalyzer;


public class TestLuceneIndexThenSearchOptions {

	private static final String authorFullName = "khushbu agarwal";

	
	public static void main(String args[]) throws ParseException, IOException{
		Analyzer standardAnalyzer = new StandardAnalyzer();		
		Analyzer keywordAnalyzer = new KeywordAnalyzer();
		Analyzer lowercaseWhitespace = new LowercaseWhitespaceAnalyzer();
		Analyzer lowerkeywordAnalyzer = new LowercaseKeywordAnalyzer();
		Analyzer whitespace = new WhitespaceAnalyzer();
		
		Analyzer[] analyzers = {};//{standardAnalyzer, keywordAnalyzer, lowercaseWhitespace, whitespace};

		String name = "author";
		String value = authorFullName;
		
		for(Analyzer analyzer : analyzers) { 
		
			tesLuceneOptions(analyzer, name, value, true, true, true, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, true, true, true, IndexOptions.DOCS);
			
			tesLuceneOptions(analyzer, name, value, true, true, false, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, true, true, false, IndexOptions.DOCS);
			
			tesLuceneOptions(analyzer, name, value, true, false, true, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, true, false, true, IndexOptions.DOCS);
					
			tesLuceneOptions(analyzer, name, value, false, true, true, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, false, true, true, IndexOptions.DOCS);
			
			tesLuceneOptions(analyzer, name, value, false, false, true, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, false, false, true, IndexOptions.DOCS);
			
			tesLuceneOptions(analyzer, name, value, false, true, false, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, false, true, false, IndexOptions.DOCS);
			
			tesLuceneOptions(analyzer, name, value, true, false, false, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, true, false, false, IndexOptions.DOCS);
			
			tesLuceneOptions(analyzer, name, value, false, false, false, IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
			tesLuceneOptions(analyzer, name, value, false, false, false, IndexOptions.DOCS);
		}
		
		MemoryIndex index = new MemoryIndex();
		Analyzer analyzer = new StandardAnalyzer();
		StringField field3 = new StringField("author", authorFullName, Store.YES);
		index.addField(field3, analyzer);
		
		Query query = new TermQuery(new Term("author",authorFullName));
		search(index,query);
		
		query = new TermQuery(new Term("author","khushbu"));
		search(index,query);
		
		query = new TermQuery(new Term("author","agarwal"));
		search(index,query);
	
	}
	
	
	public static void tesLuceneOptions(Analyzer analyzer,String name, String value, boolean stored, boolean tokenized, boolean omitnorms,
			IndexOptions options) throws ParseException, IOException {

		MemoryIndex index = new MemoryIndex();
		System.out.println(MessageFormat.format("Analyzer {0} , name {1} , value {2} , stored {3} , tokenized {4} , omitnorms {5} , options {6} "
				,(analyzer==null?"null":analyzer.toString()),name, value, stored+"", tokenized+"", omitnorms+"", options.toString()));
		
		Field field = getField(name, value, stored, tokenized, omitnorms, options);
		index.addField(field, analyzer);

		/*
		QueryParser parser = new QueryParser("author", analyzer);
		Query query = parser.parse(authorFullName);
		search(index, query);
		 */
		
		for (String splitName : authorFullName.split(" ")) {
			//create the query objects
			BooleanQuery bquery = new BooleanQuery();
			PhraseQuery q2 = new PhraseQuery();
			q2.add(new Term("author", splitName));
			//finally, add it to the BooleanQuery object
			bquery.add(q2, BooleanClause.Occur.MUST);
			
			search(index, bquery);
		}
		
		//create the query objects
		BooleanQuery bquery = new BooleanQuery();
		PhraseQuery q2 = new PhraseQuery();
		//grab the search terms from the query string
		String[] str = authorFullName.split(" ");
		//build the query
		for(String word : str) {
		  //brand is the field I'm searching in
		  q2.add(new Term("author", word.toLowerCase()));
		}

		//finally, add it to the BooleanQuery object
		bquery.add(q2, BooleanClause.Occur.MUST);

		search(index, bquery);
	}

	protected static Field getField(String name, String value, boolean stored, boolean tokenized, boolean omitnorms,
			IndexOptions options) {
		FieldType fieldType = new FieldType();
		fieldType.setStored(stored);
		fieldType.setTokenized(tokenized);
		// We never want norms for ANY of our fields
		fieldType.setOmitNorms(omitnorms);
		fieldType.setIndexOptions(options);
		Field field = new Field(name, value, fieldType);
		return field;
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
