package lucene;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

public class TestLuceneQueryInMap {

	public static void main(String[] args) throws ParseException {
		 Analyzer analyzer = new StandardAnalyzer();
		 MemoryIndex index = new MemoryIndex();
		 Map<String, String> event = new HashMap<String, String>();
		 event.put("content", "Readings about Salmons and other select Alaska fishing Manuals");
		 event.put("author", "Tales of James");
		 
		 for(Entry<String, String> entry : event.entrySet()){
			 index.addField(entry.getKey(), entry.getValue(),analyzer);
		 }
		 
		 QueryParser parser = new QueryParser("content", analyzer);
		 Query query = parser.parse("+author:james +salmon~ +fish* manual~");
		 
		 float score = index.search(query);
		 if (score > 0.0f) {
		     System.out.println("it's a match");
		 } else {
		     System.out.println("no match found");
		 }
		 System.out.println("indexData=" + index.toString());

	}

}
