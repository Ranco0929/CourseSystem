package org.guge.coursebackend.utils.result;

public enum ResultCode {

    SUCCESS(20000),// 成功

    FAIL(40000),// 失败

    VERIFYCODEERROR(40022),// 验证码错误

    NOTFOUND(40004), // not found 错误

    SERVERERROR(50000),// 所有服务器运行错误

    AUTHORIZATION(40001);// 验证错误

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
