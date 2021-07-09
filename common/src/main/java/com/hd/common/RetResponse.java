package com.hd.common;

/**
 * @author liwei
 */
public class RetResponse {
    //自定义message 失败信息
    public static <T> RetResult<T> makeErrRsp(String message) {
        return new RetResult<T>(RetCode.FAIL.code,message,null);
    }
    public static <T> RetResult<T> makeTimeOutRsp(String message) {
        return new RetResult<T>(RetCode.REQUEST_TIMEOUT.code,message,null);
    }
    public static <T> RetResult<T> makeRsp(String message,T data) {
        return new RetResult<T>(RetCode.SUCCESS.code,message,data);
    }
    public static <T> RetResult<T> makeRsp(T data) {
        return new RetResult<T>(RetCode.SUCCESS.code,"",data);
    }
    public static <T> RetResult<T> makeRsp(String message) {
        return new RetResult<T>(RetCode.SUCCESS.code,message,null);
    }
    //自定义code,msg 返回数据
    public static <T> RetResult<T> makeRsp(int code, String msg) {
        return new RetResult<T>(code,msg,null);
    }
    //自定义code,msg,data 返回数据
    public static <T> RetResult<T> makeRsp(int code, String msg, T data) {
        return new RetResult<T>(code,msg,data);
    }
}


