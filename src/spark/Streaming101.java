package spark;

import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

import com.google.common.collect.Lists;

/**
 * read from a folder.
 * Keep adding new file with data into the folder specified.
 */
public final class Streaming101 {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) {

    // Create the context with a 1 second batch size
    SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("Streaming102");
	//SparkConf sparkConf = new SparkConf().setMaster("spark://10.204.100.206:7077").setAppName("Streaming102");
	sparkConf.setJars(new String[] { "target\\original-TestProjects-1.0-SNAPSHOT.jar" });
    JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));

    String folder = "./stream/";
    if(args.length == 1){
    	folder = args[0];
    }

    JavaDStream<String> lines = ssc.textFileStream(folder);
    JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterable<String> call(String x) {
    	  System.out.println(x);
    	  return Lists.newArrayList(SPACE.split(x));
      }
    });
    
    JavaPairDStream<String, Integer> wordCounts = words.mapToPair(
      new PairFunction<String, String, Integer>() {
        @Override
        public Tuple2<String, Integer> call(String s) {
          return new Tuple2<String, Integer>(s, 1);
        }
      }).reduceByKey(new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer call(Integer i1, Integer i2) {
          return i1 + i2;
        }
    });

    wordCounts.print();
    ssc.start();
    ssc.awaitTermination();
  }
}
