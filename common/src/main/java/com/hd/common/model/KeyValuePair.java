package com.hd.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: liwei
 * @Description:
 */
@Setter
@Getter
@AllArgsConstructor
public class KeyValuePair implements Serializable {
    String key;
    String value;
}
