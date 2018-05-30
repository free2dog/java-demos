package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class ConsumerExample {
    private static Logger log = LoggerFactory.getLogger(ConsumerExample.class);

    public static void main(String... args) throws InterruptedException {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // random group id for consuming from beginning when restart application
        // just for debug convenience
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, String.valueOf(new Random().nextDouble()));
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass().getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass().getName());

        KafkaConsumer consumer = new KafkaConsumer<>(prop);

        consumer.subscribe(Arrays.asList("mytopic"));

        while(true) {
            ConsumerRecords records = consumer.poll(2000);

            records.forEach(record -> {
                log.info("message from kafka: {}", record.toString());
            });
            Thread.sleep(3000);
        }
    }
}
