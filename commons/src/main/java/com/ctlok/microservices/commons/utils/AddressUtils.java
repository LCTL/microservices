package com.ctlok.microservices.commons.utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by Lawrence Cheung on 2015/4/11.
 */
public class AddressUtils {

    public static String getIpAddress() throws UnknownHostException {
        return Inet4Address.getLocalHost().getHostAddress();
    }

}
