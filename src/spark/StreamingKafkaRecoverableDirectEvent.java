package spark;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function0;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.api.java.JavaStreamingContextFactory;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

import eventsimulator.ObjectSerializer;
import kafka.serializer.DefaultDecoder;
import scala.Tuple2;

/**
 * read from a folder. Keep adding new file with data into the folder specified.
 */
public final class StreamingKafkaRecoverableDirectEvent {
	private static String checkpointDir = "hdfs://idcdvstl233:8020/tmp/StreamingKafkaDirectEvent";
	private static boolean streamCheckPoint = false;
	private static int duration = 10;
	
	public static void main(String[] args) {
		Logger.getLogger("org").setLevel(Level.WARN);
		Logger.getLogger("akka").setLevel(Level.WARN);				
		
		if (args.length > 0) {
			try {
				duration = Integer.parseInt(args[0]);
				System.out.println("duration changed to " + duration);
			} catch (Exception e) {
				System.out.println("Duration reset to defaults");
			}
			
			if(args.length>1){
				try{
					streamCheckPoint = Boolean.getBoolean(args[1]);
					System.out.println("streamCheckPoint changed to " + streamCheckPoint);
				}catch (Exception e) {
					System.out.println("streamCheckPoint reset to defaults");
				}
			}
			
			if(args.length>2){
				checkpointDir = args[2];
				System.out.println("checkpointDir changed to " + checkpointDir);
			}			
		}
		
		
		JavaStreamingContext ssc = JavaStreamingContext.getOrCreate(checkpointDir,new JavaStreamingContextFactory() {			
			@Override
			public JavaStreamingContext create() {
				return createContext(checkpointDir, streamCheckPoint, duration);
			}
		});
		
		ssc.start();
		ssc.awaitTermination();
	}
	
	
	
	public static JavaStreamingContext createContext(String checkpointDirectory, boolean streamCheckPoint, int duration) {
		SparkConf sparkConf = new SparkConf().setAppName("StreamingKafkaRecoverableDirectEvent");

		// Only for running from eclipse
		if (System.getProperty("dev") != null)
			sparkConf.setJars(new String[] { "target\\TestProjects-1.0-SNAPSHOT.jar" });

		sparkConf.set("spark.executor.memory", "4G");

		// for elasticsearch
		sparkConf.set("es.nodes", "10.204.102.200");
		sparkConf.set("es.index.auto.create", "true");

		final int streamingDuration = duration;

		JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(streamingDuration));		
		ssc.checkpoint(checkpointDir);
		
		HashSet<String> topicsSet = new HashSet<String>();
		topicsSet.add("loadtest");

		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", "10.204.100.180:19092");

		JavaPairInputDStream<byte[], byte[]> messages = KafkaUtils.createDirectStream(ssc, byte[].class, byte[].class,
				DefaultDecoder.class, DefaultDecoder.class, kafkaParams, topicsSet);
		
		if(streamCheckPoint){
			messages.checkpoint(Durations.minutes(1));
		}

		JavaDStream<Map<String, String>> lines = messages
				.map(new Function<Tuple2<byte[], byte[]>, Map<String, String>>() {
					@Override
					public Map<String, String> call(Tuple2<byte[], byte[]> tuple2) {
						Map<String, String> ret = (Map<String, String>) ObjectSerializer.getEvent(tuple2._2());

						process(ret);
						return ret;
					}

					private void process(Map<String, String> ret) {
						ret.put("obscountry", "US");
					}
				});

		lines.foreachRDD(new Function<JavaRDD<Map<String, String>>, Void>() {
			@Override
			public Void call(JavaRDD<Map<String, String>> rdd) throws Exception {
				long start = System.currentTimeMillis();

				long count = 1; // rdd.count();

				long countTime = System.currentTimeMillis() - start;
				start = System.currentTimeMillis();

				// System.out.println(new Date() + " Total records read: "
				// +count );
				if (count > 0) {
					try {
						JavaEsSpark.saveToEs(rdd, "events/event");

						long esSaveTime = System.currentTimeMillis() - start;
						// System.out.println(new Date() + " Stats: countTime:"
						// +countTime + " esSaveTime:" + esSaveTime + " total: "
						// + count);
						System.out.println(new Date() + "  Stats: esSaveTime:" + esSaveTime);
					} catch (Exception es) {
						es.printStackTrace();
					}
				}
				return null;
			}
		});

		return ssc;
	}
}
