package org.guge.coursebackend.result;

import java.util.Objects;

public class Result {

    private int code;// 响应状态码

    private String message; // 响应提示信息

    private Object data; // 响应结果对象

    Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result result = (Result) o;
        return getCode() == result.getCode() &&
                getMessage().equals(result.getMessage()) &&
                Objects.equals(getData(), result.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMessage(), getData());
    }
}
