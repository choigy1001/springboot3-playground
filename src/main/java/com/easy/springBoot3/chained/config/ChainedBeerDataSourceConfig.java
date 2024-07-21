package com.easy.springBoot3.chained.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "beerEntityManagerFactory",
        transactionManagerRef = "beerTransactionManager",
        basePackages = "com.easy.springBoot3.domain.beer.repository"
)
@Profile("chained")
public class ChainedBeerDataSourceConfig {

    @Value("${dataSource.username}")
    private String username;

    @Value("${dataSource.password}")
    private String password;

    @Value("${dataSource.url.beer}")
    private String beerUrl;

    @Primary
    @Bean
    public DataSource beerDataSource() {
        HikariDataSource hikariDataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .username(username)
                .password(password)
                .url(beerUrl)
                .build();
        hikariDataSource.setMaximumPoolSize(10);
        return hikariDataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean beerEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("beerDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.easy.springBoot3.domain.beer")
                .persistenceUnit("beerEntityManager")
                .properties(setProperties())
                .build();
    }


    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Primary
    @Bean
    public PlatformTransactionManager beerTransactionManager(
            @Qualifier("beerEntityManagerFactory") LocalContainerEntityManagerFactoryBean beerEntityManagerFactory) {
        return new JpaTransactionManager(beerEntityManagerFactory.getObject());
    }

    private HashMap<String, String> setProperties() {
        HashMap<String, String> propertiesMap = new HashMap<>();

        propertiesMap.put("hibernate.hbm2ddl.auto", "update");
        propertiesMap.put("hibernate.jdbc.time_zone", "Asia/Seoul");
        return propertiesMap;
    }
}
