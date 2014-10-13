import java.io.File;
import java.util.Date;

import org.apache.lucene.analysis.miscellaneous.LimitTokenCountAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.apache.lucene.util.Version;

public class TestLuceneIndexMerge {

	public static void main(String[] args) throws Exception {
		File INDEXES_DIR = new File("\\docsOP2");
		File INDEX_DIR = new File("\\docs");

		Date start = new Date();

		IndexWriterConfig writerConfig = new IndexWriterConfig(
				Version.LUCENE_43, new StandardAnalyzer(Version.LUCENE_43));
		writerConfig.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
		LogByteSizeMergePolicy mergePolicy = new LogByteSizeMergePolicy();

		mergePolicy.setMergeFactor(100);
		writerConfig.setMergePolicy(mergePolicy);
		Directory directory = new NIOFSDirectory(INDEX_DIR,
				new SimpleFSLockFactory());

		IndexWriter writer = new IndexWriter(directory, writerConfig);

		Directory indexes[] = new Directory[INDEXES_DIR.list().length];
		directory.listAll();

		for (int i = 0; i < INDEXES_DIR.list().length; i++) {
			System.out.println("Adding: " + INDEXES_DIR.list()[i]);
			/*
			 * indexes[i] =
			 * FSDirectory.getDirectory(INDEXES_DIR.getAbsolutePath() + "/" +
			 * INDEXES_DIR.list()[i]);
			 */
			System.out.println(indexes[i]);
		}

		System.out.print("Merging added indexes...");
		writer.addIndexes(indexes);
		System.out.println("done");

		System.out.print("Optimizing index...");
		// writer.optimize();
		writer.close();
		System.out.println("done");

		Date end = new Date();
		System.out.println("It took: "
				+ ((end.getTime() - start.getTime()) / 1000) + "\"");
	}

}
