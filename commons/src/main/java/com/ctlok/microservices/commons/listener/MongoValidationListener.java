package com.ctlok.microservices.commons.listener;

import com.ctlok.microservices.commons.service.ValidationService;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

/**
 * Created by Lawrence Cheung on 2015/4/12.
 */
public class MongoValidationListener extends AbstractMongoEventListener<Object> {

    private final ValidationService validationService;

    public MongoValidationListener( final ValidationService validationService ) {
        this.validationService = validationService;
    }

    @Override
    public void onBeforeSave(Object source, DBObject dbo) {
        validationService.validate(source);
    }

}