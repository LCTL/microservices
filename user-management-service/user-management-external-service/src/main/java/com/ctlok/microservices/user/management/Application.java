package com.ctlok.microservices.user.management;

import com.ctlok.microservices.commons.external.CommonExceptionHandler;
import com.ctlok.microservices.commons.listener.MongoValidationListener;
import com.ctlok.microservices.commons.service.ValidationService;
import com.ctlok.microservices.commons.service.impl.Jsr303ValidationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import reactor.Environment;
import reactor.rx.broadcast.Broadcaster;
import reactor.spring.context.config.EnableReactor;

import javax.validation.Validator;

/**
 * Created by Lawrence Cheung on 2015/4/10.
 */
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories
@EnableReactor
@EnableAsync
@EnableWebMvc
public class Application extends WebMvcConfigurerAdapter {

    public static void main( final String[] args ) {
        SpringApplication.run( Application.class, args );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Broadcaster<DeferredResult> userStream( final Environment env ) {
        return Broadcaster.create( env );
    }

    @Bean
    public Jsr303ValidationService jsr303ValidationService( Validator validator ) {
        return new Jsr303ValidationService( validator );
    }

    @Bean
    public MongoValidationListener mongoValidationListener( ValidationService validationService ) {
        return new MongoValidationListener( validationService );
    }

    @ControllerAdvice
    public static class ExceptionHandler extends CommonExceptionHandler {

    }

}
