package com.hd.common.model;

import com.google.common.base.CaseFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: liwei
 * @Description:
 */
@Data
public class QueryExpression implements Serializable {
    private static final long serialVersionUID = -5099378457111419832L;
    /**
     * 数据库字段名
     */
    private String column;
    public  String getCamelColumn(){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column);
    }
    /**
     * 字段值
     */
    private String value;
    /**
     * 连接类型，如like,equals,gt,ge,lt,le
     */
    private String type;
    public String getValue(){
        if(value==null) {
            return  "";
        }
        return  value;
    }

}
