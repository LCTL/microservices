package com.ctlok.microservices.commons.external.utils;

import com.ctlok.microservices.commons.external.ResourceNotFoundException;
import com.ctlok.microservices.commons.external.RestResponse;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.Environment;
import reactor.rx.Promise;
import reactor.rx.Stream;
import reactor.rx.Streams;

import java.util.Optional;
import java.util.concurrent.Callable;


/**
 * Created by Lawrence Cheung on 2015/4/11.
 */
public class DeferredResultUtils {

    public static <T> DeferredResult<RestResponse<T>> executeAsync( final Callable<T> fn ) {
        Environment.initializeIfEmpty();
        return executeAsync( Environment.get(), fn );
    }

    public static <T> DeferredResult<RestResponse<T>> executeAsync( final Environment env, final Callable<T> fn ) {

        final DeferredResult<RestResponse<T>> deferredResult = new DeferredResult<>();

        Streams
                .just( deferredResult )
                .dispatchOn( env )
                .map( ( s ) -> {
                    try {
                        return Optional.ofNullable( fn.call() );
                    } catch ( Throwable t ) {
                        throw new IllegalStateException( t );
                    }
                } )
                .map( ( output ) -> {
                    if ( output.isPresent() ) {
                        return RestResponse.wrap( output.get() );
                    } else {
                        throw new ResourceNotFoundException();
                    }
                } )
                .when( Throwable.class, deferredResult:: setErrorResult )
                .consume( deferredResult:: setResult );

        return deferredResult;

    }

    public static <T> DeferredResult<RestResponse<T>> executeAsync( final Stream<T> stream ) {

        return executeAsync( stream.next() );

    }

    public static <T> DeferredResult<RestResponse<T>> executeAsyncWithOptional(
            final Stream<Optional<T>> stream ) {

        return executeAsyncWithOptional( stream.next() );

    }

    public static <T> DeferredResult<RestResponse<T>> executeAsync( final Promise<T> promise ) {

        final DeferredResult<RestResponse<T>> deferredResult = new DeferredResult<>();

        promise
                .onSuccess( ( output ) -> {
                    if ( output == null ) {
                        throw new ResourceNotFoundException();
                    } else {
                        deferredResult.setResult( RestResponse.wrap( output ) );
                    }
                } )
                .onError( deferredResult:: setErrorResult );

        return deferredResult;

    }

    public static <T> DeferredResult<RestResponse<T>> executeAsyncWithOptional(
            final Promise<Optional<T>> promise ) {

        final DeferredResult<RestResponse<T>> deferredResult = new DeferredResult<>();

        promise
                .onSuccess( ( output ) -> {
                    if ( output.isPresent() ) {
                        deferredResult.setResult( RestResponse.wrap( output.get() ) );
                    } else {
                        throw new ResourceNotFoundException();
                    }
                } )
                .onError( deferredResult:: setErrorResult );

        return deferredResult;

    }

}
