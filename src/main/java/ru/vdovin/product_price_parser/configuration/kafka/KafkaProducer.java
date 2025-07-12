package ru.vdovin.product_price_parser.configuration.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<Long, Object> kafkaTemplate;

    public void send(Object data, String kafkaTopic) {
        if (kafkaTopic == null || data == null) {
            throw new IllegalArgumentException("Kafka service. Some of required params is null.");
        }
        ProducerRecord<Long, Object> producerRecord = new ProducerRecord<>(kafkaTopic, data);

        kafkaTemplate.send(producerRecord);
    }
}
