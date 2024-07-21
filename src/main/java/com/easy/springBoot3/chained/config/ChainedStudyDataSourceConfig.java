package com.easy.springBoot3.chained.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "studyEntityManagerFactory",
        transactionManagerRef = "studyTransactionManager",
        basePackages = "com.easy.springBoot3.domain.study.repository")
@Profile("chained")
public class ChainedStudyDataSourceConfig {

    @Value("${dataSource.username}")
    private String username;

    @Value("${dataSource.password}")
    private String password;

    @Value("${dataSource.url.study}")
    private String studyUrl;

    @Bean
    public DataSource studyDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .username(username)
                .password(password)
                .url(studyUrl)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean studyEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("studyDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.easy.springBoot3.domain.study")
                .persistenceUnit("studyEntityManager")
                .properties(setProperties())
                .build();
    }

    @Bean
    public PlatformTransactionManager studyTransactionManager(
            @Qualifier("studyEntityManagerFactory") LocalContainerEntityManagerFactoryBean studyEntityManagerFacotry
    ) {
        return new JpaTransactionManager(studyEntityManagerFacotry.getObject());
    }


    private HashMap<String, String> setProperties() {
        HashMap<String, String> propertiesMap = new HashMap<>();

        propertiesMap.put("hibernate.hbm2ddl.auto", "update");
        propertiesMap.put("hibernate.jdbc.time_zone", "Asia/Seoul");
        return propertiesMap;
    }
}
