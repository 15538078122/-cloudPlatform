package com.hd.common.model;

/**
 * @Author: liwei
 * @Description: 数据权限标识类
 */
public enum DataPrivilege {
    // 所有权限
    ALL(0),

    // 本级及以下
    LEVEL_AND_BELOW(1),

    // 仅本级
    LEVEL_ONLY(2);

    private int value;

    DataPrivilege(int value) {
        this.value = value;
    }
    public int getValue(){
        return  value;
    }
    public  static DataPrivilege getByValue(int value){
        DataPrivilege[] enumConstants = DataPrivilege.class.getEnumConstants();
        for(DataPrivilege item:enumConstants){
            if(item.getValue()==value){
                return item;
            }
        }
        return  null;
    }
}