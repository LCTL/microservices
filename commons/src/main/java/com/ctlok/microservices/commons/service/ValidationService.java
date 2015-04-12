package com.ctlok.microservices.commons.service;

import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import java.util.Set;

/**
 * Created by Lawrence Cheung on 2015/4/12.
 */
public interface ValidationService {

    void validate(Object obj);

    void validate(Object obj, Class<? extends Payload> payload);

    void validate(Object obj, Iterable<Class<? extends Payload>> payloads);

    Set<ConstraintViolation<Object>> validateWithoutException(Object obj);

    Set<ConstraintViolation<Object>> validateWithoutException(
            Object obj, Class<? extends Payload> payload);

    Set<ConstraintViolation<Object>> validateWithoutException(
            Object obj, Iterable<Class<? extends Payload>> payloads);

}
