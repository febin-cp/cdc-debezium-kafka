package com.example.debeziumconnect.controllers;

import com.example.debeziumconnect.kafka.KafkaProducer;
import com.example.debeziumconnect.models.Country;
import com.example.debeziumconnect.models.CountryCDC;
import com.example.debeziumconnect.services.CountryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/v1")
public class EnphaseController {

    private final KafkaProducer kafkaProducer;

    @Autowired
    private CountryServices countryServices;

    @Autowired
    public EnphaseController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/send-message")
    public ResponseEntity getDummyController(@RequestBody CountryCDC data) {
        kafkaProducer.sendMessage(data);
        return ResponseEntity.ok("Messade send to Kafka Topic");
    }

    @PostMapping("/add-country")
    public ResponseEntity addCountryToSystem(@RequestParam String country) {
        countryServices.addCountry(country);
        return ResponseEntity.ok("Succesfully updated database");
    }
}
