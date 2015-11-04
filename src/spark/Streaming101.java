package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class Streaming101 {

	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local");
		sparkConf.setAppName("TestSpark");

		// Create a StreamingContext with a 1-second batch size from a SparkConf
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf,	Durations.seconds(1));
		
		
		// Create a DStream from all the input on port 7777
		JavaDStream<String> lines = jssc.socketTextStream("localhost", 7777);
		// Filter our DStream for lines with "error"
		JavaDStream<String> errorLines = lines
				.filter(new Function<String, Boolean>() {
					public Boolean call(String line) {
						return line.contains("error");
					}
				});
		// Print out the lines with errors
		errorLines.print();
		
		
		// Start our streaming context and wait for it to "finish"
		jssc.start();
		// Wait for the job to finish
		jssc.awaitTermination();
	}

}
