package com.ctlok.microservices.user.management.test;

import com.ctlok.microservices.commons.listener.MongoValidationListener;
import com.ctlok.microservices.commons.service.ValidationService;
import com.ctlok.microservices.commons.service.impl.Jsr303ValidationService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.spring.context.config.EnableReactor;

import javax.validation.Validator;

/**
 * Created by Lawrence Cheung on 2015/4/12.
 *
 * @author Lawrence Cheung
 */
@Configuration
@EnableAutoConfiguration
@EnableMongoRepositories
@EnableReactor
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public Jsr303ValidationService jsr303ValidationService( Validator validator ) {
        return new Jsr303ValidationService( validator );
    }

    @Bean
    public MongoValidationListener mongoValidationListener( ValidationService validationService ) {
        return new MongoValidationListener( validationService );
    }

}
