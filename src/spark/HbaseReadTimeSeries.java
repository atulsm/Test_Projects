package spark;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.netiq.sentinel.cdh.SentinelEvent;

public class HbaseReadTimeSeries {
	
	private static class Consts {
		public final static String HBASE_MASTER = "sentinel-dev-16.labs.blr.novell.com:60000";
		public final static String ZOOKKEEPER_QUORUM = "sentinel-dev-16.labs.blr.novell.com";
		//public final static String SPARK_NODE = "spark://edh-datanode-1.esecurity.net:7077";
		public final static String TARGET_TABLE = "netiq:sentinel-events";
	};
	
	private static Configuration initializeHBaseConfig() {

		Configuration hbaseConfig = HBaseConfiguration.create();

		hbaseConfig.set(TableInputFormat.INPUT_TABLE, Consts.TARGET_TABLE);
		hbaseConfig.set(TableInputFormat.SCAN_BATCHSIZE, "5000");
		hbaseConfig.set(TableInputFormat.SCAN_CACHEDROWS, "10000");
		hbaseConfig.set(TableInputFormat.SCAN_MAXVERSIONS, "1");
		hbaseConfig.set(TableInputFormat.SCAN_COLUMNS, "base:pCol");

		hbaseConfig.set("hbase.distributed.cluster", "true");
		hbaseConfig.set("hbase.zookeeper.quorum", Consts.ZOOKKEEPER_QUORUM);

		hbaseConfig.set("mapreduce.job.maps", "4");
		hbaseConfig.set("mapred.map.tasks", "4");
		hbaseConfig.set("hbase.mapreduce.splitsPerRegion", "4");

		return hbaseConfig;
	}

	public static void main(String[] args) throws Exception {

		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local");
		sparkConf.setAppName("TestSpark");		
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);

		//HBaseConnection connection = new HBaseConnection(Consts.HBASE_MASTER, Consts.ZOOKKEEPER_QUORUM);		
		Configuration hbaseConfig = initializeHBaseConfig();
		long start = System.currentTimeMillis();		
		
		hbaseConfig.set(TableInputFormat.SCAN_TIMERANGE_START, "1444194420157");
		hbaseConfig.set(TableInputFormat.SCAN_TIMERANGE_END, "1444194520157");
		
		JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hbaseConfig, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);		
		JavaRDD<Map<String,String>> events = rdd.map(new Function<Tuple2<ImmutableBytesWritable, Result>, Map<String,String>>() {
			@Override
			public Map<String,String> call(Tuple2<ImmutableBytesWritable, Result> rowData) throws Exception {
				SentinelEvent ev = new SentinelEvent(new SentinelEventDecoder().fromBytes(rowData._2.value()));				
				return ev.valueMap;
			}
		});
		
		JavaRDD<Map<String,String>> persist = events.persist(StorageLevel.MEMORY_AND_DISK_2());		
		long total  = persist.count();		
		System.out.println("Toal " + total + " records read");	
		
		try {
			//JavaEsSpark.saveToEs(events, "events2/event");
			System.out.println(persist.take((int)total));
		}
		catch(Exception es) {
			System.err.println("****** Exception: " + es.toString());
		}
		
		long sec = (System.currentTimeMillis() - start)/1000;
		System.out.println("Took " + sec + " seconds at the rate of " + total/sec + " EPS");
			
		persist.unpersist();					
	}
	
	private static class SentinelEventDecoder {
		private Kryo kryo;
		public SentinelEventDecoder() {
			kryo = new Kryo();
			kryo.register(HashMap.class, new MapSerializer());
		}

		public HashMap<String, String> fromBytes(byte[] rawData) {
			HashMap<String, String> map = null;
			try {
				map = kryo.readObject(new Input(new ByteArrayInputStream(rawData)), HashMap.class);
			} catch (Exception e) {
				map = new HashMap<String, String>();
			}

			return map;
		}
	}
}