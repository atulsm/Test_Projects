package spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.google.common.collect.Iterables;

/**
 * Page Rank example implemented in spark
 * 
 * 1. Initialize each page’s rank to 1.0. 2. On each iteration, have page p send
 * a contribution of rank(p)/numNeighbors(p) to its neighbors (the pages it has
 * links to). 3. Set each page’s rank to 0.15 + 0.85 * contributionsReceived
 * 
 * @author Atul
 *
 */
public class PageRank {

	private static List<String> data = Arrays.asList(new String[] {
			"novell.com netware.com", "novell.com groupwise.com",
			"novell.com suse.com", "novell.com sentinel.com",
			"netiq.com novell.com", "netiq.com attachmate.com",
			"netiq.com suse.com", "microfocus.com netiq.com",
			"microfocus.com suse.com", "microfocus.com novell.com",
			"novell.com microfocus.com"});

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local");
		sparkConf.setAppName("TestSpark");

		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		JavaRDD<String> input = sc.parallelize(data);

		// represent the input data as a key value
		JavaPairRDD<String, Iterable<String>> links = input.mapToPair(
				new PairFunction<String, String, String>() {
					@Override
					public Tuple2<String, String> call(String x)
							throws Exception {
						String[] array = x.split(" ");
						return new Tuple2(array[0], array[1]);
					}
				}).groupByKey();
		links.partitionBy(new HashPartitioner(5));

		// initialize ranks
		JavaPairRDD<String, Double> ranks = links.mapValues(x -> 1.0);

		// Calculates and updates URL ranks continuously using PageRank algorithm.
		for (int current = 0; current < 10; current++) {
			// Calculates URL contributions to the rank of other URLs.
			JavaRDD<Tuple2<Iterable<String>, Double>> values = links.join(ranks).values();

			JavaPairRDD<String, Double> contribs = values
					.flatMapToPair(new PairFlatMapFunction<Tuple2<Iterable<String>, Double>, String, Double>() {
						@Override
						public Iterable<Tuple2<String, Double>> call(Tuple2<Iterable<String>, Double> s) {
							int urlCount = Iterables.size(s._1);
							List<Tuple2<String, Double>> results = new ArrayList<Tuple2<String, Double>>();
							for (String n : s._1) {
								results.add(new Tuple2<String, Double>(n, s._2() / urlCount));
							}
							return results;
						}
					});

			// Re-calculates URL ranks based on neighbor contributions.
			ranks = contribs
						.reduceByKey((a, b) -> a + b)
						.mapValues(x -> 0.15 + x * 0.85);
			
			//print(ranks);
		}

		print(ranks);

		sc.stop();
		System.out.println((System.currentTimeMillis() - start)/1000 + " seconds");
	}

	private static void print(JavaPairRDD<String, Double> ranks) {
		// Collects all URL ranks and dump them to console.
		List<Tuple2<String, Double>> output = ranks.collect();
		for (Tuple2<?, ?> tuple : output) {
			System.out.println(tuple._1() + " has rank: " + tuple._2() + ".");
		}
	}
}
