package spark;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;

/**
 * Pair RDD
 * @author satul
 *
 */
public class WordCount {

	private static List<String> data = Arrays.asList(new String[] 
			{
					"my name is atul soman",
					"my name is alen atul",
					"evan atul",
					"anjana paulose"
			}); 
	
	public static void main(String[] args) {
		
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local");
		sparkConf.setAppName("TestSpark");
		
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		JavaRDD<String> input = sc.parallelize(data);
		
		JavaRDD<String> words =  input.flatMap(in -> Arrays.asList(in.split(" ")));
		
		JavaPairRDD<String, Integer> inputPair = words.mapToPair(
				new PairFunction<String, String, Integer>() {
					@Override
					public Tuple2<String, Integer> call(String x)	throws Exception {
						return new Tuple2<String, Integer>(x,1);
					}
				}
		);
		JavaPairRDD<String, Integer> persistPair = inputPair.reduceByKey( (x,y) -> x+y ).persist(StorageLevel.MEMORY_AND_DISK());
		
		System.out.println(persistPair.sortByKey().take(100));
		
		//sort by value
		System.out.println(persistPair.mapToPair(x -> x.swap()).sortByKey(false).mapToPair(x -> x.swap()).take(100));				
	}

}
