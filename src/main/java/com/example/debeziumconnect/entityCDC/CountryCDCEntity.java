package com.example.debeziumconnect.entityCDC;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "countrycdc")
@Getter
@Setter
public class CountryCDCEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
}
