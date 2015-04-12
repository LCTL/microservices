package com.ctlok.microservices.commons.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Lawrence Cheung on 2015/4/11.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> implements Serializable {

    private T value;
    private RestError error;

    public static <I> RestResponse<I> wrap( I value ) {
        return new RestResponse<>( value, null );
    }

    public static <I> RestResponse<I> wrapError( String id, String code, String message ) {
        return new RestResponse<>( null, new RestError( id, code, message ) );
    }

}
