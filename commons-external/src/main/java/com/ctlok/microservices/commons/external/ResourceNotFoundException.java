package com.ctlok.microservices.commons.external;

/**
 * Created by Lawrence Cheung on 2015/4/11.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException( final String message ) {
        super( message );
    }

    public ResourceNotFoundException( final String message, final Throwable cause ) {
        super( message, cause );
    }

    public ResourceNotFoundException( final Throwable cause ) {
        super( cause );
    }

    public ResourceNotFoundException( final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
