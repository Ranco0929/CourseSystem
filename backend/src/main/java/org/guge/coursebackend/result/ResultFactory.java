package org.guge.coursebackend.result;

public class ResultFactory {

    public static Result buildResult(ResultCode resultCode, String message, Object data) {
        return new Result(resultCode.code, message, data);
    }

    public static Result buildSuccessResult(Object data) {
       return buildResult(ResultCode.SUCCESS, "success", data);
    }

    public static Result buildFailResult(Object data) {
        return buildResult(ResultCode.FAIL, "fail", data);
    }
}
