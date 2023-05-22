package com.example.debeziumconnect.kafka;

import com.example.debeziumconnect.entity.CountryEntity;
import com.example.debeziumconnect.models.Country;
import com.example.debeziumconnect.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    private CountryRepository countryRepository;

    public KafkaConsumer(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }

    @KafkaListener(topics = "events-ingestion-json", groupId = "debeziumDemo")
    public void kafkaListener(Country message){
        CountryEntity country = new CountryEntity();
        country.setName(message.getName());

        countryRepository.save(country);
        log.info("Message received {}", message);
    }
}

