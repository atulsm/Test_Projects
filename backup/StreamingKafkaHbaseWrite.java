package spark;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.cloudera.spark.hbase.JavaHBaseContext;

import eventsimulator.ObjectSerializer;
import kafka.serializer.DefaultDecoder;
import scala.Tuple2;

/**
 * read from a folder.
 * Keep adding new file with data into the folder specified.
 */
public final class StreamingKafkaHbaseWrite {
  private static final Pattern SPACE = Pattern.compile(" ");
  
  public final static String HBASE_MASTER = "10.204.100.232:60000";
  public final static String ZOOKKEEPER_QUORUM = "10.204.102.237";
  //public final static String SPARK_NODE = "spark://edh-datanode-1.esecurity.net:7077";
  public final static String TARGET_TABLE = "netiq:sentinel-events";	
	
	private static Configuration initializeHBaseConfig() {

		Configuration hbaseConfig = HBaseConfiguration.create();

		hbaseConfig.set(TableInputFormat.INPUT_TABLE, TARGET_TABLE);
		hbaseConfig.set(TableInputFormat.SCAN_BATCHSIZE, "5000");
		hbaseConfig.set(TableInputFormat.SCAN_CACHEDROWS, "10000");
		hbaseConfig.set(TableInputFormat.SCAN_MAXVERSIONS, "1");
		hbaseConfig.set(TableInputFormat.SCAN_COLUMNS, "base:pCol");

		hbaseConfig.set("hbase.distributed.cluster", "true");
		hbaseConfig.set("hbase.zookeeper.quorum", ZOOKKEEPER_QUORUM);

		hbaseConfig.set("mapreduce.job.maps", "4");
		hbaseConfig.set("mapred.map.tasks", "4");
		hbaseConfig.set("hbase.mapreduce.splitsPerRegion", "4");

		return hbaseConfig;
	}

  public static void main(String[] args) {
	  //Logger.getLogger("org").setLevel(Level.WARN);
	  //Logger.getLogger("akka").setLevel(Level.WARN);
	  
	  SparkConf sparkConf = new SparkConf().setMaster("spark://10.204.100.206:7077").setAppName("StreamingKafkaDirect101");

	  //Only for running from eclipse
	  if(System.getProperty("dev") != null)
		  sparkConf.setJars(new String[] { "target\\TestProjects-1.0-SNAPSHOT.jar" });
	
	  //sparkConf.setExecutorEnv("executor-memory", "8G");
	  //sparkConf.setExecutorEnv("spark.executor.memory", "8G");
	  sparkConf.set("spark.executor.memory", "4G");
	  //sparkConf.set("executor-memory", "8G");
	  
	  //for elasticsearch
	  sparkConf.set("es.nodes", "10.204.102.200");
	  sparkConf.set("es.index.auto.create", "true");
		
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
	  
	  JavaHBaseContext hbaseContext = new JavaHBaseContext(ssc.sc(), initializeHBaseConfig());

	
	    
	  HashSet<String> topicsSet = new HashSet<String>();
	  topicsSet.add("loadtest");
	  
	  HashMap<String, String> kafkaParams = new HashMap<String, String>();
	  kafkaParams.put("metadata.broker.list", "10.204.100.180:19092");
	  
	  JavaPairInputDStream<byte[], byte[]> messages = KafkaUtils.createDirectStream(
			  ssc,
			  	byte[].class,
				byte[].class,
				DefaultDecoder.class,
				DefaultDecoder.class,
		        kafkaParams,
		        topicsSet
		    );
	  
	  
	  JavaDStream<Map<String,String>> lines = messages.map(new Function<Tuple2<byte[], byte[]>, Map<String,String>>() {
	      @Override
	      public Map<String,String> call(Tuple2<byte[], byte[]> tuple2) {
	    	  Map<String,String> ret = (Map<String,String>) ObjectSerializer.getEvent(tuple2._2());
	    	  
	    	  process(ret);
	    	  
	    	  return ret;
	      }

		private void process(Map<String, String> ret) {
			ret.put("obscountry", "US");
		}
	  });
	    
	  lines.foreachRDD(new Function<JavaRDD<Map<String,String>>, Void>() {
		  @Override
		  public Void call(JavaRDD<Map<String,String>> rdd) throws Exception {
			  long start = System.currentTimeMillis();
			  
			  long count = 1; //rdd.count();
			  			  
			  long countTime = System.currentTimeMillis() - start;
			  start = System.currentTimeMillis();
			  
			  //System.out.println(new Date() + "  Total records read: " +count );
			  if(count>0){			  
				  try {
					  

					  hbaseContext.bulkPut(rdd, TARGET_TABLE, new PutFunction(), true);
					  
					  
					//JavaEsSpark.saveToEs(rdd, "events/event");
						
						long esSaveTime = System.currentTimeMillis() - start;
						 //System.out.println(new Date() + "  Stats: countTime:" +countTime + " esSaveTime:" + esSaveTime + " total: " + count);
						System.out.println(new Date() + "  Stats: esSaveTime:" + esSaveTime);
					}
					catch(Exception es) {
						es.printStackTrace();
					}
				  }
			  return null;
		  }
	  });
	    	
	  ssc.start();
	  ssc.awaitTermination();
  	}
  
  public static class PutFunction implements Function<Map<String,String>, Put> {

	    private static final long serialVersionUID = 1L;

	    public Put call(Map<String,String> event) throws Exception {
	    	Put put = new Put(event.get("rv39").getBytes());
	        
	        for(Map.Entry<String, String> entry:event.entrySet())
	        {
	              if(entry.getValue() == null || entry.getValue().isEmpty())
	                     continue;
	              
	              put.add(Bytes.toBytes("cf"), entry.getKey().getBytes(), entry.getValue().getBytes());
	        }
	      return put;
	    }
	 }
}
