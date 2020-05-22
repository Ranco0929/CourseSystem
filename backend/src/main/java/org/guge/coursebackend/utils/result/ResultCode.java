package org.guge.coursebackend.utils.result;

public enum ResultCode {

    SUCCESS(20000),// 成功

    FAIL(40000),// 失败

    SERVERERROR(50000),

    AUTHORIZATION(40001);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
