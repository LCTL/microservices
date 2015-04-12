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
public class RestError implements Serializable {

    private String id;
    private String code;
    private String message;

}
