package com.ctlok.microservices.commons.external;

import com.ctlok.microservices.commons.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * Created by Lawrence Cheung on 2015/4/11.
 */
@Slf4j
public class CommonExceptionHandler {

    @ResponseBody
    @ResponseStatus( HttpStatus.NOT_FOUND )
    @ExceptionHandler( ResourceNotFoundException.class )
    public RestResponse notFound( final ResourceNotFoundException resourceNotFoundException ) {
        return exceptionLog( resourceNotFoundException, HttpStatus.NOT_FOUND.name(), false );
    }

    @ResponseBody
    @ExceptionHandler( HttpRequestMethodNotSupportedException.class )
    @ResponseStatus( value = HttpStatus.BAD_REQUEST )
    public RestResponse<?> handleHttpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException ) {

        return exceptionLog( httpRequestMethodNotSupportedException, HttpStatus.BAD_REQUEST.name(), false );

    }

    @ResponseBody
    @ExceptionHandler( ConstraintViolationException.class )
    @ResponseStatus( value = HttpStatus.BAD_REQUEST )
    public RestResponse<?> handleConstraintViolationException(
            final ConstraintViolationException constraintViolationException ) {

        final Optional<ConstraintViolation<?>> optional = constraintViolationException.getConstraintViolations().stream().findFirst();
        final StringBuilder logMessageBuilder = new StringBuilder( "" );

        if ( optional.isPresent() ) {

            final ConstraintViolation constraintViolation = optional.get();
            logMessageBuilder
                    .append( constraintViolation.getPropertyPath().toString() )
                    .append( ":" )
                    .append( constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName() );

        }

        return exceptionLog(
                constraintViolationException,
                CommonErrorCode.FIELD_VALIDATION_ERROR,
                logMessageBuilder.toString(),
                false );

    }

    @ResponseBody
    @ExceptionHandler( BindException.class )
    @ResponseStatus( value = HttpStatus.BAD_REQUEST )
    public RestResponse<?> handleBindException( final BindException bindException ) {

        return exceptionLog(
                bindException,
                CommonErrorCode.FIELD_VALIDATION_ERROR,
                createLogMessage( bindException.getBindingResult() ),
                false );

    }

    @ResponseBody
    @ExceptionHandler( MethodArgumentNotValidException.class )
    @ResponseStatus( value = HttpStatus.BAD_REQUEST )
    public RestResponse<?> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException methodArgumentNotValidException ) {

        return exceptionLog(
                methodArgumentNotValidException,
                CommonErrorCode.FIELD_VALIDATION_ERROR,
                createLogMessage( methodArgumentNotValidException.getBindingResult() ),
                false );

    }

    @ResponseBody
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    @ExceptionHandler( Throwable.class )
    public RestResponse unknownError( final Throwable throwable ) {

        return exceptionLog( throwable, HttpStatus.INTERNAL_SERVER_ERROR.name(), true );

    }

    protected String createLogMessage( final BindingResult bindingResult ) {

        final Optional<FieldError> optional = bindingResult.getFieldErrors().stream().findFirst();
        final StringBuilder logMessageBuilder = new StringBuilder( "" );

        if ( optional.isPresent() ) {

            final FieldError fieldError = optional.get();
            logMessageBuilder
                    .append( fieldError.getField() )
                    .append( ":" )
                    .append( fieldError.getCode() );

        }

        return logMessageBuilder.toString();

    }

    protected RestResponse exceptionLog( final Throwable throwable, final String errorCode, final boolean stackTrace ) {

        return exceptionLog( throwable, errorCode, throwable.toString(), stackTrace );

    }

    protected RestResponse exceptionLog( final Throwable throwable, final String errorCode, final String logMessage, final boolean stackTrace ) {

        final String logId = LogUtils.generateLogId();

        log.error( "LOG ID: [{}]; Exception Type: [{}]; Message: [{}]", logId, throwable.getClass(), logMessage );

        if ( stackTrace ) {
            log.error( "LOG ID: [" + logId + "]. Stack Trace: ", throwable );
        }

        return RestResponse.wrapError( logId, errorCode, logMessage );

    }

}
