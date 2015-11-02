package spark;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;

/**
 * Demonstrating various transformations and actions in RDD's
 * @author satul
 *
 */
public class Spark201 {

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
		
		//filter transform
		JavaRDD<String> filter = input.filter(
				new Function<String, Boolean>() {
					
					@Override
					public Boolean call(String x) throws Exception {
						return x.contains("atul");
					}
				}
		);
		
		//Map acts on all elements
		JavaRDD<String> capitalMap = filter.map(
				new Function<String, String>() {
					@Override
					public String call(String x) throws Exception {
						return x.toUpperCase();
					}
				}
		);

		JavaRDD<String> persist = capitalMap.persist(StorageLevel.MEMORY_AND_DISK());
		
		String out =  persist.first();	
		System.out.println(out);
		
		System.out.println(persist.count());
		System.out.println(persist.take(10));
		
		JavaRDD<String> flatMap = persist.flatMap(
				new FlatMapFunction<String, String>() {
					@Override
					public Iterable<String> call(String x) throws Exception {
						return Arrays.asList(x.split(" "));
					}					
				}				
		);
		
		System.out.println(flatMap.take(100));

		
	}

}
