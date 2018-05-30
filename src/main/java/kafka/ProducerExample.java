package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serdes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

;

public class ProducerExample {

    private static Logger log = LoggerFactory.getLogger(ProducerExample.class);

    public static void main(String... args){
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass().getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass().getName());

        KafkaProducer producer = new KafkaProducer(prop);

        ProducerRecord<String, String> record = new ProducerRecord("mytopic", "key-sync","value00");

        // sync
        try {
            RecordMetadata metadata = (RecordMetadata) producer.send(record).get();
            log.info("metadata returned: {}", metadata);
        }catch (Exception ex){
            log.error("sync send failed: {}", record);
            ex.printStackTrace();
        }

        ProducerRecord<String, String> recordAsync = new ProducerRecord("mytopic", "key-async","value11");
        // async
        producer.send(recordAsync, (recordMetadata, ex) -> {
            if (ex != null){
                log.error("async send message failed: {}", recordAsync);
                ex.printStackTrace();
            }else {
                log.info("async sent ok and metadata returned: {}", recordMetadata);
            }
        });


    }

}
