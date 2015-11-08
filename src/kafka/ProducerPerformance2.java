package kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.record.Records;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerPerformance2 extends Thread{
	
	private String[] args;
	private static final int CLIENT_COUNT=1;
	
	public ProducerPerformance2(String args[]){
		this.args = args;
	}
	
	public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("USAGE: java " + ProducerPerformance2.class.getName() + " url num_records record_size");
            System.exit(1);
        }
        int numRecords = Integer.parseInt(args[1]);
        int recordSize = Integer.parseInt(args[2]);
        long start = System.currentTimeMillis();

        ProducerPerformance2 ins[] = new  ProducerPerformance2[CLIENT_COUNT];
        for(int i =0;i<ins.length;i++){
        	ins[i] = new ProducerPerformance2(args);
        	ins[i].start();
        }

        for(int i =0;i<ins.length;i++){
           	ins[i].join();
        }
                       
        long ellapsed = System.currentTimeMillis() - start;
        double msgsSec = 1000.0 * numRecords / (double) ellapsed;
        double mbSec = msgsSec * (recordSize + Records.LOG_OVERHEAD) / (1024.0 * 1024.0);
        System.out.printf("%d records sent in %d ms ms. %.2f records per second (%.2f mb/sec).", numRecords, ellapsed, msgsSec, mbSec);
	}
	
    public void run() {
        String url = args[0];
        int numRecords = Integer.parseInt(args[1])/CLIENT_COUNT;
        int recordSize = Integer.parseInt(args[2]);
        Properties props = new Properties();
        props.setProperty(ProducerConfig.ACKS_CONFIG, "0");
        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "8196");
        props.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "67108864");
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        props.setProperty(ProducerConfig.METADATA_FETCH_TIMEOUT_CONFIG, Integer.toString(5 * 1000));
        props.setProperty(ProducerConfig.TIMEOUT_CONFIG, Integer.toString(Integer.MAX_VALUE));
        KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props,new StringSerializer(), new ByteArraySerializer());
        //KafkaProducer<Object, byte[]> producer = new KafkaProducer<Object, byte[]>();
        
        Callback callback = new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null)
                    e.printStackTrace();
            }
        };
        byte[] payload = new byte[recordSize];
        Arrays.fill(payload, (byte) 1);
        ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>("loadtest", payload);
        long maxLatency = -1L;
        long totalLatency = 0;
        int reportingInterval = 1000000;
        for (int i = 0; i < numRecords; i++) {
            long sendStart = System.currentTimeMillis();
            producer.send(record, callback);
            //producer.send(record);
            long sendEllapsed = System.currentTimeMillis() - sendStart;
            maxLatency = Math.max(maxLatency, sendEllapsed);
            totalLatency += sendEllapsed;
            if (i % reportingInterval == 0) {
                System.out.printf("%d  max latency = %d ms, avg latency = %.5f\n",
                                  i,
                                  maxLatency,
                                  (totalLatency / (double) reportingInterval));
                totalLatency = 0L;
                maxLatency = -1L;
            }
        }
        producer.close();
    }
}
