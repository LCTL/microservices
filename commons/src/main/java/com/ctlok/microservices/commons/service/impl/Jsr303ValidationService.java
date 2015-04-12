package com.ctlok.microservices.commons.service.impl;

import com.ctlok.microservices.commons.service.ValidationService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Payload;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Lawrence Cheung on 2015/4/12.
 */
public class Jsr303ValidationService implements ValidationService {

    private final Validator validator;

    public Jsr303ValidationService(Validator validator) {
        super();
        this.validator = validator;
    }

    @Override
    public void validate(Object obj) {

        throwExceptionWhenSetNotEmpty(validateWithoutException(obj));

    }

    @Override
    public void validate(Object obj, Class<? extends Payload> payload) {

        throwExceptionWhenSetNotEmpty(validateWithoutException(obj, payload));

    }

    @Override
    public void validate(Object obj, Iterable<Class<? extends Payload>> payloads) {

        throwExceptionWhenSetNotEmpty(validateWithoutException(obj, payloads));

    }

    @Override
    public Set<ConstraintViolation<Object>> validateWithoutException(Object obj) {

        return validator.validate(obj);

    }

    @Override
    public Set<ConstraintViolation<Object>> validateWithoutException(
            Object obj, Class<? extends Payload> payload) {

        return validateWithoutException(obj, new HashSet<>(Arrays.asList(payload)));

    }

    @Override
    public Set<ConstraintViolation<Object>> validateWithoutException(
            Object obj, Iterable<Class<? extends Payload>> payloads) {

        final Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();

        for (final Iterator<Class<? extends Payload>> iterator = payloads.iterator(); iterator.hasNext(); ) {

            constraintViolations.addAll(validator.validate(obj, iterator.next()));

        }

        return constraintViolations;

    }

    protected void throwExceptionWhenSetNotEmpty(final Set<ConstraintViolation<Object>> constraintViolations) {

        if (!constraintViolations.isEmpty()) {

            throw new ConstraintViolationException(constraintViolations);

        }

    }

}
