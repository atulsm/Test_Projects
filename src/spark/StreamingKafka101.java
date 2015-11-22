package spark;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/**
 * read from a folder.
 * Keep adding new file with data into the folder specified.
 */
public final class StreamingKafka101 {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) {
	  Logger.getLogger("org").setLevel(Level.WARN);
	  Logger.getLogger("akka").setLevel(Level.WARN);
	  
	  SparkConf sparkConf = new SparkConf().setMaster("spark://10.204.100.206:7077").setAppName("StreamingKafka101");
	  sparkConf.setJars(new String[] { "target\\TestProjects-1.0-SNAPSHOT.jar" });
	
	  //sparkConf.setExecutorEnv("executor-memory", "8G");
	  //sparkConf.setExecutorEnv("spark.executor.memory", "8G");
	  sparkConf.set("spark.executor.memory", "4G");
	  //sparkConf.set("executor-memory", "8G");
		
	  int duration = 2;
	  if(args.length > 0){
		  try{
			  duration = Integer.parseInt(args[0]);
			  System.out.println("duration changed to " + duration);
		  }catch(Exception e){
			  System.out.println("Duration reset to defaults");
		  }
	  }
	  
	  JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(duration));
	
	    
	  Map<String, Integer> topicMap = new HashMap<String, Integer>();
	  topicMap.put("loadtest", 4);
	  JavaPairReceiverInputDStream<String, String> kafkaStream = KafkaUtils.createStream(ssc,"10.204.100.172:2182","kafka-group1",topicMap);
	    
	  JavaDStream<String> lines = kafkaStream.map(new Function<Tuple2<String, String>, String>() {
	      @Override
	      public String call(Tuple2<String, String> tuple2) {
	        return tuple2._2();
	      }
	  });
	    
	  lines.foreachRDD(new Function<JavaRDD<String>, Void>() {
		  @Override
		  public Void call(JavaRDD<String> rdd) throws Exception {
			  System.out.println(new Date() + "  Total records read: " + rdd.count() );
			  return null;
		  }
	  });
	    	
	  ssc.start();
	  ssc.awaitTermination();
  	}
}
