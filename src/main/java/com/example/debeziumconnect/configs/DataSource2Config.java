package com.example.debeziumconnect.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "cdcEntityManager",
        transactionManagerRef = "cdcTransactionManager",
        basePackages = "com.example.debeziumconnect.repositoryCDC")
public class DataSource2Config {
    @Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean cdcEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(cdcDataSource());
        em.setPackagesToScan(
                new String[] { "com.example.debeziumconnect.entityCDC" });

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect",
                env.getProperty("spring.jpa.properties.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource cdcDataSource() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.secondary.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.secondary.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.secondary.password"));

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager cdcTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                cdcEntityManager().getObject());
        return transactionManager;
    }
}
