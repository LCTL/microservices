package com.ctlok.microservices.commons.utils;

import java.util.UUID;

/**
 * Created by Lawrence Cheung on 2015/4/11.
 */
public class StringUtils {

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll( "-", "" );
    }

}
