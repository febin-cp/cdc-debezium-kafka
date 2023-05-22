package com.example.debeziumconnect.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class DebeziumConnectorConfig {

    @Bean
    public io.debezium.config.Configuration eventsConnector() throws IOException {
        var offsetStorageTempFile = File.createTempFile("offsets_", ".dat");
        var dbHistoryTempFile = File.createTempFile("dbhistory123_", ".dat");

        return io.debezium.config.Configuration.create()
                .with("name", "debezium_mysql_connector")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
                .with("offset.flush.interval.ms", "60000")
                .with("schema.history.internal.kafka.topic", "events-rds-destination-topic")
                .with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")
                .with("topic.prefix", "my-prefix_")
                .with("database.hostname", "localhost")
                .with("database.port", "3306") //defaults to 3306
                .with("database.user", "root")
                .with("database.password", "password")
                .with("database.dbname", "debezium")
                .with("database.include.list", "debezium")
                .with("include.schema.changes", "true")
                .with("database.allowPublicKeyRetrieval", "true")
                .with("database.server.id", "10181")
                .with("database.server.name", "debezium-connect-demo")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", dbHistoryTempFile.getAbsolutePath())
                .build();
    }
}
