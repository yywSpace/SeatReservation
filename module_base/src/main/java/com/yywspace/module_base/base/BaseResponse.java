package com.yywspace.module_base.base;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * BaseResponse<T>
 */
public class BaseResponse<T> {

    @SerializedName("message")
    private final String msg;
    @SerializedName("code")
    private final int code;
    private T data;

    public BaseResponse(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        return "BaseResponse{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
