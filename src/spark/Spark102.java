package spark;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

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
		sparkConf.setMaster("local");
		//sparkConf.setMaster("spark://10.204.100.206:7077");
		//sparkConf.setMaster("spark://idcdvstl224:7077");
		sparkConf.setAppName("TestSpark");
		
		//sparkConf.setJars(new String[] { "target\\original-TestProjects-1.0-SNAPSHOT.jar" });
		
		//sparkConf.set("spark.driver.host", "IND-SATUL.microfocus.com");
				
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		JavaRDD<String> input = sc.parallelize(data);
		
		JavaRDD<String> filter = input.filter(new Function<String, Boolean>() {
			int i = 0;
			@Override
			public Boolean call(String v1) throws Exception {
				return i++ % 2 == 0;
			}
		});
		
		JavaRDD<Map<String,String>> result = input.map(new Function<String, Map<String, String>>() {
			@Override
			public Map<String, String> call(String v1) throws Exception {
				Map<String, String> ret = new HashMap<>();
				int i=0;
				for(String val : v1.split(" ")){
					ret.put("key"+i++, val);
				}
				return ret;
			}			
		});

		//String out =  result.first();
		System.out.println(filter.collect());
	}

}
