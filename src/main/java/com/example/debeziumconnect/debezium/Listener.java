package com.example.debeziumconnect.debezium;

import com.example.debeziumconnect.entityCDC.CountryEntity;
import com.example.debeziumconnect.repository.CountryCDCRepository;
import com.example.debeziumconnect.repository.CountryRepository;
import com.example.debeziumconnect.repositoryCDC.CountryRepositoryCDC;
import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Listener {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

//    @Autowired
//    CountryCDCRepository countryCDCRepository;

    @Autowired
    CountryRepositoryCDC countryRepositoryCDC;


    public Listener(Configuration eventsConnectorConfig) {
        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(eventsConnectorConfig.asProperties())
                .notifying(this::handleChangeEvent)
                .build();
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        log.info("debezium found change in database {} ", sourceRecordRecordChangeEvent);

        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        String key = sourceRecord.key().toString();
        Struct sourceRecordChangeValue = (Struct) sourceRecord.value();

        log.info("SourceRecordChangeValue = {}", sourceRecordChangeValue);
        if (sourceRecordChangeValue != null) {
            Envelope.Operation operation = Envelope.operationFor(sourceRecord);
            if (sourceRecordChangeValue.schema().field("after") != null) {
                Struct payload = sourceRecordChangeValue.getStruct("after");
                log.info("payload {}", payload.get("name"));
                CountryEntity countryEntity = new CountryEntity();
                if(payload.get("name") != null) {
                    countryEntity.setName(payload.get("name").toString());
                    countryRepositoryCDC.save(countryEntity);
                }

//                Map<Object, Object> payloadMap = payload.schema().fields().stream()
//                        .filter(f -> payload.get(f) != null)
//                        .collect(Collectors.toMap(payload::get, Field::name));
//                for (Map.Entry<Object,Object> entry : payloadMap.entrySet()) {
//                    CountryEntity countryCDCEntity = new CountryEntity();
////                    countryCDCEntity.setName(entry.getKey());
//                    log.info("name11: {}", entry.getValue());
////                    countryRepositoryCDC.save(countryCDCEntity);
//                }
            }
        }
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (Objects.nonNull(this.debeziumEngine)) {
            this.debeziumEngine.close();
        }
    }


}
