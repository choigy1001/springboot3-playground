package com.easy.springBoot3.jta.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "beerEntityManagerFactory",
        transactionManagerRef = "beerTransactionManager",
        basePackages = "com.easy.springBoot3.domain.beer.repository"
)
@Profile("atomikos")
public class JtaBeerDataSourceConfig {

    @Bean(name = "beerDataSource")
    public DataSource beerDataSource(Environment env) {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("beer");
        dataSource.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        String prefix = "spring.jta.atomikos.datasource.beer";

        Properties properties = new Properties();
        properties.put("URL", env.getProperty(prefix + ".xa-properties.url"));
        properties.put("user", env.getProperty(prefix + ".xa-properties.user"));
        properties.put("password", env.getProperty(prefix + ".xa-properties.password"));

        dataSource.setXaProperties(properties);
        return dataSource;
    }

    @Bean(name = "beerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean beerEntityManagerFactory(
            EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("beerDataSource") DataSource dataSource
    ) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.easy.springBoot3.domain.beer")
                .persistenceUnit("beerEntityManager")
                .properties(hibernateProperties())
                .build();
    }

    private HashMap<String, String> hibernateProperties() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        return properties;
    }

    @Bean
    public PlatformTransactionManager beerTransactionManager(
            @Qualifier("beerEntityManagerFactory") LocalContainerEntityManagerFactoryBean beerEntityManagerFactory) {
        return new JpaTransactionManager(beerEntityManagerFactory.getObject());
    }
}
