package com.hd.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: liwei
 * @Description:
 */
@Setter
@Getter
public class KeyValuePair implements Serializable {

    String key;
    String value;
}
