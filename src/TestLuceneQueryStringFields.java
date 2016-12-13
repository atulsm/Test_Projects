import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test case to validate field name calculation from lucene query string
 * @author SAtul
 *
 */
@RunWith(Parameterized.class)
public class TestLuceneQueryStringFields {

	private String query;
	private String expectedFields;

	public TestLuceneQueryStringFields(String query, String expectedFields) {
		this.query = query;
		this.expectedFields = expectedFields;
	}

	@Parameters
	public static Collection<Object[]> generateData() {
		Object[][] array = { 
					{ "sev:1", "sev" }, 
					{ "port:1023", "port" }, 
					{ "sev:1 OR port:1024", "sev,port" } 
				};
		return Arrays.asList(array);
	}
	
	@Test
	public void testQueryFields() {
		List<String> expectedFieldList = Arrays.asList(expectedFields.split(","));
		Collections.sort(expectedFieldList);		
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser(null, analyzer);
		
		try {
			Set<String> actualFields = calculateQueryFieldsRecursively(parser.parse(query));			
			Assert.assertEquals(true, expectedFieldList.containsAll(actualFields) && actualFields.containsAll(expectedFieldList));			
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Actual implementation 
	 * 
	 * @param query
	 * @return
	 * @throws ParseException
	 */
	private static Set<String> calculateQueryFieldsRecursively(Query query) throws ParseException {
		Set<String> fields = new TreeSet<String>();

		if (query instanceof TermQuery) {
			TermQuery tQuery = (TermQuery) query;
			Term term = tQuery.getTerm();
			fields.add(term.field());
		} else if (query instanceof BooleanQuery) {
			BooleanQuery bQuery = (BooleanQuery) query;
			List<BooleanClause> clauses = bQuery.clauses();
			for (BooleanClause clause : clauses) {
				Query innerQuery = clause.getQuery();
				Set<String> innerFields = calculateQueryFieldsRecursively(innerQuery);
				if (innerFields == null) {
					return null;
				} else {
					fields.addAll(innerFields);
				}
			}
		} else { // TODO support more lucene query types
			return null;
		}
		return fields;
	}

}
