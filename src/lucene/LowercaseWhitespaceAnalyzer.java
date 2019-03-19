package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

public final class LowercaseWhitespaceAnalyzer extends Analyzer
{
	@Override
	public TokenStreamComponents createComponents( String strFieldName)
	{
		Tokenizer	tokenizer = new WhitespaceTokenizer();
		TokenStream	stream = new LowerCaseFilter(tokenizer);
		return new TokenStreamComponents( tokenizer, stream);
	}
}