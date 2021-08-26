package com.hd.common;

public enum RetCode {
    // 成功
    SUCCESS(200),

    // 失败
    FAIL(202),

    // 未认证（签名错误）
    UNAUTHORIZED(401),
    //禁止访问，权限不足
    FORBIDDEN(403),
    //客户端错误请求
    BAD_REQUEST(400),
    // 接口不存在
    NOT_FOUND(404),

    // 服务器内部错误
    INTERNAL_SERVER_ERROR(500),
    //请求超时
    REQUEST_TIMEOUT(408);
    public int code;

    RetCode(int code) {
        this.code = code;
    }
}

