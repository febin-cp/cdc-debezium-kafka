package com.example.debeziumconnect.repositoryCDC;

import com.example.debeziumconnect.entityCDC.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepositoryCDC extends JpaRepository<CountryEntity, Long> {

}
