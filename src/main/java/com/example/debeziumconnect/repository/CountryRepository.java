package com.example.debeziumconnect.repository;

import com.example.debeziumconnect.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

}
