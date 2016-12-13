package kafka;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import eventsimulator.ObjectSerializer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
 
public class KafkaEventConsumer {
    private final ConsumerConnector consumer;
    private final String topic;
    private  ExecutorService executor;
    
    public AtomicLong total = new AtomicLong(5000000);
    public AtomicLong tStart = new AtomicLong(0);

    
    private static long start = System.currentTimeMillis();
 
    public KafkaEventConsumer(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
    }
 
    public void shutdown() {
    	long sec =(System.currentTimeMillis()-start)/1000;
    	System.out.println("Total EPS = " + 5000000/sec);
    	
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
   }
 
    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(a_numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
 
        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);
 
        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerTest(stream, threadNumber));
            threadNumber++;
        }
        start = System.currentTimeMillis();
    }
 
    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper); 
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");
 
        return new ConsumerConfig(props);
    }
 
    public static void main(String[] args) {
        String zooKeeper = args[0];
        String groupId = args[1];
        String topic = args[2];
        int threads = Integer.parseInt(args[3]);
 
        KafkaEventConsumer example = new KafkaEventConsumer(zooKeeper, groupId, topic);
        example.run(threads);
 
        /*
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
 
        }
        example.shutdown();
        */
    }
    
    
    class ConsumerTest implements Runnable {
        private KafkaStream m_stream;
        private int m_threadNumber;
     
        public ConsumerTest(KafkaStream a_stream, int a_threadNumber) {
            m_threadNumber = a_threadNumber;
            m_stream = a_stream;
            tStart.set(System.currentTimeMillis());
        }
     
        public void run() {
            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
            while (it.hasNext()){
            	byte[] eventBytes = it.next().message();
            	Object event = ObjectSerializer.getEvent(eventBytes);
            	//System.out.println(event);
            	            	
            	if(total.incrementAndGet() % 100000 == 0){
            		long sec = (System.currentTimeMillis() - tStart.get())/1000;
            		System.out.println(new Date() + " EPS " + 100000/sec);
            		tStart.set(System.currentTimeMillis());
            		System.out.println(event);
            	}
            }
            System.out.println("Shutting down Thread: " + m_threadNumber);
        }
    }
}

