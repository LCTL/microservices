package com.ctlok.microservices.commons.utils;

import java.math.BigInteger;
import java.net.UnknownHostException;

/**
 * Created by Lawrence Cheung on 2015/4/11.
 */
public class LogUtils {

    public static String generateLogId() {
        final long now = DateUtils.now().getTime();
        final String ip = getLastPartIp();
        final StringBuilder sb = new StringBuilder();
        sb
            .append( Base62.encode( BigInteger.valueOf( Integer.valueOf( ip ).intValue() ) ))
            .append( "_" )
            .append( Base62.encode( BigInteger.valueOf( now ) ) )
            .append( "_" )
            .append( Base62.encode( new BigInteger( StringUtils.generateId().substring( 0, 10 ), 16 ) ) );
        return sb.toString();
    }

    private static String getLastPartIp(){
        try {
            return AddressUtils.getIpAddress().split( "\\." )[3];
        } catch ( UnknownHostException e ) {
            return "999";
        }
    }

}
