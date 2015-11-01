package spark;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * First spark application ran on hadoop environment 
 * @author satul
 *
 */
public class Spark102 {

	private static List<String> data = Arrays.asList(new String[] 
			{
					"my name is atul soman",
					"my name is alen atul",
					"evan atul",
					"anjana paulose"
			}); 
	
	public static void main(String[] args) {
		
		SparkConf sparkConf = new SparkConf();
		//sparkConf.setMaster("local");
		sparkConf.setMaster("spark://sentinel-dev-23.labs.blr.novell.com:7077");
		sparkConf.setAppName("TestSpark");
		
		//sparkConf.setJars(new String[] { "target\\TestProjects-1.0-SNAPSHOT.jar" });		
		//sparkConf.set(key, value)
				
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		JavaRDD<String> input = sc.parallelize(data);

		String out =  input.first();
		System.out.println(out);
	}

}
