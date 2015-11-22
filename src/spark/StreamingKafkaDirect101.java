package spark;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

/**
 * read from a folder.
 * Keep adding new file with data into the folder specified.
 */
public final class StreamingKafkaDirect101 {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) {
	  Logger.getLogger("org").setLevel(Level.WARN);
	  Logger.getLogger("akka").setLevel(Level.WARN);
	  
	  SparkConf sparkConf = new SparkConf().setMaster("spark://10.204.100.206:7077").setAppName("StreamingKafkaDirect101");

	  //Only for running from eclipse
	  if(System.getProperty("dev") != null)
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
	
	    
	  HashSet<String> topicsSet = new HashSet<String>();
	  topicsSet.add("loadtest");
	  
	  HashMap<String, String> kafkaParams = new HashMap<String, String>();
	  kafkaParams.put("metadata.broker.list", "10.204.100.180:19092");
	  
	  JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
			  ssc,
		        String.class,
		        String.class,
		        StringDecoder.class,
		        StringDecoder.class,
		        kafkaParams,
		        topicsSet
		    );
	  
	  
	  JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
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
