package com.example.debeziumconnect.services;

import com.example.debeziumconnect.entity.CountryEntity;
import com.example.debeziumconnect.repository.CountryRepository;
import com.example.debeziumconnect.repositoryCDC.CountryRepositoryCDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CountryServices {


    private final CountryRepository countryRepository;


    @Autowired
    public CountryServices(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public void addCountry(String country){
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country);
        countryRepository.save(countryEntity);
    }
}
