package com.easy.springBoot3.chained.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableTransactionManagement
@Profile("chained")
public class ChainedTransactionManagerConfig {

    @Bean(name = "chainedTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("studyTransactionManager") PlatformTransactionManager studyTransactionManager,
            @Qualifier("beerTransactionManager") PlatformTransactionManager beerTransactionManager) {
        return new ChainedTransactionManager(studyTransactionManager, beerTransactionManager);
    }

    @Bean
    public TransactionTemplate transactionTemplate(
            @Qualifier("chainedTransactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
