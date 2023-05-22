package com.example.debeziumconnect.repository;

import com.example.debeziumconnect.entity.CountryCDCEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCDCRepository extends JpaRepository<CountryCDCEntity, Long> {

}