package spark;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

/**
 * Simple Application to Demonstrate Spark
 * @author satul
 */
public class WordCount {

	private static List<String> data = Arrays.asList(new String[] {
				"atul soman",
				"alen atul",
				"evan atul",
				"anjana paulose"
			}); 
	
	public static void main(String[] args) throws InterruptedException {		
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local");
		sparkConf.setAppName("TestSpark");
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		
		JavaRDD<String> input = sc.parallelize(data);
		JavaPairRDD<String, Integer> result = input
				.flatMap(in -> Arrays.asList(in.split(" ")))
				.mapToPair( x -> new Tuple2<String, Integer>(x, 1))
				.reduceByKey( (x,y) -> x+y );
		
			
		System.out.println("************************");
		System.out.println(result.collect());
		System.out.println("************************");
				
		sc.stop();
	}

}
