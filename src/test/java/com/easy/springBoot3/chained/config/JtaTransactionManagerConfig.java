package com.easy.springBoot3.chained.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
public class JtaTransactionManagerConfig {

    @Bean(name = "atomikosTransactionManager")
    public PlatformTransactionManager transactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        return new JtaTransactionManager(userTransactionImp, userTransactionManager);
    }
}
