package org.guge.coursebackend.utils.result;

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

    public static Result buildAuthorzationFailedResult(Object data) {
        return buildResult(ResultCode.AUTHORIZATION, "authorization failed", data);
    }

    public static Result ServerErrorResult(Object data) {
        return buildResult(ResultCode.SERVERERROR, "server error", data);
    }

    public static Result NotFoundResult(Object data) {
        return buildResult(ResultCode.NOTFOUND, "Not found", data);
    }
}
