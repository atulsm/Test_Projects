import java.io.IOException;
import java.text.MessageFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import esecurity.ccs.comp.event.indexedlog.LowercaseKeywordAnalyzer;


public class TestLuceneQueryIndexThenSearch {

	private static final String authorFullName = "Khushbu agarwal";

	
	public static void main(String args[]) throws ParseException, IOException{
		Analyzer standardAnalyzer = new StandardAnalyzer();		
		Analyzer keywordAnalyzer = new KeywordAnalyzer();
		Analyzer lowercaseWhitespace = new LowercaseWhitespaceAnalyzer();
		Analyzer lowerkeywordAnalyzer = new LowercaseKeywordAnalyzer();
		Analyzer whitespace = new WhitespaceAnalyzer();
		
		Analyzer[] analyzers = {standardAnalyzer, keywordAnalyzer, lowercaseWhitespace, whitespace , null};

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
	
	}
	
	
	public static void tesLuceneOptions(Analyzer analyzer,String name, String value, boolean stored, boolean tokenized, boolean omitnorms,
			IndexOptions options) throws ParseException, IOException {

		MemoryIndex index = new MemoryIndex();
		System.out.println(MessageFormat.format("Analyzer {0} , name {1} , value {2} , stored {3} , tokenized {4} , omitnorms {5} , options {6} "
				,(analyzer==null?"null":analyzer.toString()),name, value, stored+"", tokenized+"", omitnorms+"", options.toString()));
		
		Field field = getField(name, value, stored, tokenized, omitnorms, options);
		index.addField(field, analyzer);

		QueryParser parser = new QueryParser("author", analyzer);
		Query query = parser.parse(authorFullName);
		search(index, query);

		for (String splitName : authorFullName.split(" ")) {
			query = parser.parse(splitName);
			search(index, query);
		}
		System.out.println();
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
