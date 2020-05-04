package org.guge.coursebackend.result;

public enum ResultCode {

    SUCCESS(20000),// 成功

    FAIL(40000);// 失败

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
