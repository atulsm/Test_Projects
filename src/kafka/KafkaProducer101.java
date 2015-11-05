package kafka;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.producer.KeyedMessage;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

 
public class KafkaProducer101 {
    private static final String TOPIC = "page_visits";

	public static void main(String[] args) {
        long events = 10;
        Random rnd = new Random();
 
        Properties props = new Properties();
        props.put("bootstrap.servers", "164.99.175.163:9092");
        
       // props.put("request.required.acks", "1");
        //props.put("producer.type","async");

 
        Serializer<String> keySerializer =  new StringSerializer();
		Serializer<String> valueSerializer =  new StringSerializer();
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props, keySerializer, valueSerializer);
        int totalPartitions = producer.partitionsFor(TOPIC).size();
        
        for (long nEvents = 0; nEvents < events; nEvents++) { 
               long runtime = new Date().getTime();  
               String ip = "192.168.2." + rnd.nextInt(255); 
               String msg = runtime + ",www.example.com," + ip; 
               ProducerRecord<String, String> data = new ProducerRecord<String, String>(TOPIC, getPartition(ip, totalPartitions),ip, msg);
               producer.send(data);
        }
        producer.close();
    }
	
    public static int getPartition(String stringKey, int totalPartitions) {
        int partition = 0;
        int offset = stringKey.lastIndexOf('.');
        if (offset > 0) {
           partition = Integer.parseInt( stringKey.substring(offset+1)) % totalPartitions;
        }
       return partition;
  }
}
