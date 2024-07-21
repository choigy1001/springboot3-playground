package com.easy.springBoot3.chained.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "studyEntityManagerFactory",
        transactionManagerRef = "studyTransactionManager",
        basePackages = "com.easy.springBoot3.domain.study.repository"
)
public class JtaStudyDataSourceConfig {

    @Primary
    @Bean(name = "studyDataSource")
    public DataSource studyDataSource(Environment env) {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("study");
        dataSource.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        String prefix = "spring.jta.atomikos.datasource.study";

        Properties properties = new Properties();
        properties.put("URL", env.getProperty(prefix + ".xa-properties.url"));
        properties.put("user", env.getProperty(prefix + ".xa-properties.user"));
        properties.put("password", env.getProperty(prefix + ".xa-properties.password"));

        dataSource.setXaProperties(properties);
        return dataSource;
    }

    @Primary
    @Bean(name = "studyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean studyEntityManagerFactory(
            EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("studyDataSource") DataSource dataSource
    ) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.easy.springBoot3.domain.study")
                .persistenceUnit("studyEntityManager")
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
    public PlatformTransactionManager studyTransactionManager(
            @Qualifier("studyEntityManagerFactory") LocalContainerEntityManagerFactoryBean studyEntityManagerFacotry
    ) {
        return new JpaTransactionManager(studyEntityManagerFacotry.getObject());
    }
}
