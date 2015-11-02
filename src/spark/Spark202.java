package spark;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Demonstrating various set operations in RDD's
 * 
 * @author satul
 *
 */
public class Spark202 {

	private static List<String> data1 = Arrays.asList(new String[] {
			"atul soman", "anjana paulose" });

	private static List<String> data2 = Arrays.asList(new String[] {
			"alen atul", "evan atul", });

	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local");
		sparkConf.setAppName("TestSpark");

		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		JavaRDD<String> input1 = sc.parallelize(data1);
		JavaRDD<String> input2 = sc.parallelize(data2);

		JavaRDD<String> set  = input1.flatMap(in -> Arrays.asList(in.split(" ")))
				.intersection(
				input2.flatMap(in -> Arrays.asList(in.split(" "))));

		System.out.println(set.take(100));

	}

}
