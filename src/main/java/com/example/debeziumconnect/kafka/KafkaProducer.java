package com.example.debeziumconnect.kafka;

import com.example.debeziumconnect.models.Country;
import com.example.debeziumconnect.models.CountryCDC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {

    private KafkaTemplate<String, Country> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Country> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CountryCDC data) {
        log.info("message send {}", data.getName());

        Message<CountryCDC> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, "events-ingestion-json")
                .build();

        log.info("messg");

        kafkaTemplate.send(message);
    }

}
