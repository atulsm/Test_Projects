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
		sparkConf.setMaster("spark://10.204.100.206:7077");
		//sparkConf.setMaster("spark://idcdvstl224:7077");
		sparkConf.setAppName("TestSpark");
		
		//sparkConf.setJars(new String[] { "target\\original-TestProjects-1.0-SNAPSHOT.jar" });
		
		//sparkConf.set("spark.driver.host", "IND-SATUL.microfocus.com");
				
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		JavaRDD<String> input = sc.parallelize(data);

		String out =  input.first();
		System.out.println(out);
	}

}
